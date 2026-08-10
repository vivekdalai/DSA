# Design a Google Docs-style Collaborative Editor — HLD

Design a shared rich-text editor where several people edit one document concurrently.
The central challenge is convergence: everyone must end up with identical content even
when edits are concurrent, out of order, or retried. This is a Google Docs-like design,
not a claim about Google's implementation. It uses a per-document sequencer plus
**Operational Transformation (OT)**; CRDT is the principal alternative.

---

## Q&A: Functional Requirements

**Q: What must it do?**

- Create, open, share, and persist documents.
- Enforce \`viewer\`, \`commenter\`, and \`editor\` roles.
- Display concurrent edits in near real time.
- Display active users, cursors, and selections.
- Keep revision history and recover clients after reconnect.

**Q: What is out of scope?**

Offline-first behavior, plugins, comments/suggestions, search, and full rich layout.
Call these extensions; focus the core design on the editing protocol.

## Q&A: Non-Functional Requirements

| Requirement | Concrete meaning |
|---|---|
| Latency | Local typing feels instant; remote edits normally appear in <200 ms in-region. |
| Correctness | Clients processing the same committed operations converge to identical content. |
| Ordering | One document needs a total order of accepted edits; unrelated documents do not. |
| Durability | An acknowledged edit survives a process failure. |
| Scale | Millions of documents, mostly low activity, plus a few very hot documents. |
| Security | ACL checks on HTTP/socket actions; no unauthorized presence leaks. |

For a document, choose ordered consistent writes over committing writes through a
partition. A disconnected client can type locally, but commits after reconnect/rebase.

---

## Core Entities and APIs

| Entity | Key fields |
|---|---|
| \`Document\` | \`documentId\`, owner, latestRevision, snapshot pointer, ACL version |
| \`Snapshot\` | document, revision, canonical rich-text state, checksum |
| \`Operation\` | \`opId\`, document, \`baseRevision\`, author, client sequence, mutation |
| \`CommittedOperation\` | revision, transformed mutation, \`opId\`, author |
| \`Presence\` | user, connection, cursor/selection, expiry |

Small plain-text operations:

\`\`\`json
{ "type": "insert", "pos": 12, "text": "hello" }
{ "type": "delete", "pos": 12, "length": 5 }
\`\`\`

Rich text generalizes these to document-run/tree mutations: insert/delete a range,
format a range, or change a block. Never send a full document per keystroke.

\`\`\`http
POST /v1/documents                  → { documentId, revision: 0 }
GET  /v1/documents/{documentId}     → { metadata, snapshot, revision, myRole }
PATCH /v1/documents/{id}/permissions
\`\`\`

\`\`\`text
WebSocket /v1/documents/{documentId}/session  (Bearer JWT)
client → server: JOIN { lastKnownRevision }
server → client: SNAPSHOT { revision, content } + OPS { ...latest }
client → server: OP { opId, clientSeq, baseRevision, mutation }
server → all:   COMMIT { revision, opId, authorId, mutation }
client ↔ server: PRESENCE { cursor, selection }
\`\`\`

\`opId\` makes retries idempotent. The authenticated connection, not the payload,
supplies the identity and role.

---

## HLD Walkthrough — Built Incrementally

### Step 1 — One user saves a whole document

\`\`\`mermaid
flowchart LR
    C[Browser / Mobile Editor] -->|GET, PUT entire document| API[Document API]
    API --> DB[(Document DB)]
\`\`\`

**What breaks?** Alice and Bob load revision 10. Alice saves revision 11, then Bob
saves his stale copy. Last-writer-wins destroys Alice's change.

### Step 2 — Deltas plus revision history

\`\`\`mermaid
flowchart LR
    C[Editor] --> API[Document Service]
    API --> S[(Latest Snapshot)]
    API --> L[(Append-only Operation Log)]
\`\`\`

The log preserves edits and supports restore/audit. The snapshot is the fast read
path. A stale \`insert(pos=10)\` still needs adjustment after another edit shifts text.

### Step 3 — WebSocket real-time fan-out

\`\`\`mermaid
flowchart LR
    A[Alice Editor] <-->|WebSocket| G[Collaboration Gateway]
    B[Bob Editor] <-->|WebSocket| G
    G --> DS[Document Service]
    DS --> S[(Snapshot)]
    DS --> L[(Operation Log)]
\`\`\`

Clients apply keystrokes optimistically and retain a small queue of unacknowledged
operations, so typing remains responsive.

### Step 4 — One logical writer per document

Hash \`documentId\` to a collaboration partition. It owns the authoritative operation
order; different documents scale independently.

\`\`\`mermaid
flowchart LR
    A[Alice] --> G1[WebSocket Gateway]
    B[Bob] --> G2[WebSocket Gateway]
    G1 --> R[Document Router<br/>hash documentId]
    G2 --> R
    R --> P1[Collab Partition 17<br/>leader for doc-42]
    R --> P2[Other document partitions]
    P1 --> L[(Replicated Operation Log)]
    P1 --> S[(Snapshot Store)]
    P1 --> G1
    P1 --> G2
\`\`\`

**Why not lock a DB row per character?** It serializes writes but does not solve stale
positions/reconciliation, makes typing a DB round trip, and is a poor hot-doc bottleneck.

### Step 5 — Operational Transformation

At revision 10, the document is \`cat\`. Alice inserts \`big \` at position 0; Bob,
working from revision 10, inserts \`!\` at position 3. If Alice commits first, Bob's
operation becomes \`insert(7, "!")\`; the final document is \`big cat!\`.

\`\`\`mermaid
sequenceDiagram
    participant A as Alice (base 10)
    participant B as Bob (base 10)
    participant P as Document Partition
    participant L as Durable Log
    A->>P: insert(0, "big ")
    P->>L: append revision 11
    P-->>A: COMMIT 11
    P-->>B: COMMIT 11
    B->>P: insert(3, "!"), base=10
    P->>P: transform against rev 11 → insert(7, "!")
    P->>L: append revision 12
    P-->>A: COMMIT 12
    P-->>B: COMMIT 12
\`\`\`

The partition transforms an incoming operation at revision \`r\` against commits
\`r+1...current\`, persists canonical form, assigns the next revision, then broadcasts.
Clients likewise transform remote commits against their pending optimistic operations.

### Step 6 — Keep presence ephemeral

\`\`\`mermaid
flowchart LR
    C[Editor] --> G[WebSocket Gateway]
    G --> P[Document Partition<br/>ordered ops + OT]
    P --> LOG[(Durable Op Log)]
    P --> SNAP[(Snapshot Store)]
    G --> PR[(Presence Store<br/>Redis + TTL)]
    PR --> G
\`\`\`

Cursors can move dozens of times per second. Rate-limit them and use TTL-backed
memory; a missing cursor is acceptable, a missing committed edit is not.

### Step 7 — Final production design

\`\`\`mermaid
flowchart TB
    C[Web / Mobile Clients] --> E[CDN + HTTPS API]
    C <-->|WebSocket| W[Global WebSocket Gateways]
    E --> AUTH[Auth + ACL Service]
    E --> META[Document Metadata Service]
    META --> MDB[(Metadata / ACL DB)]
    W --> AUTH
    W --> R[Document-aware Router]
    R --> CP[Collaboration Partitions<br/>leader + standby]
    CP --> OT[OT / Revision Engine]
    OT --> LOG[(Replicated Operation Log)]
    CP --> PRES[(Presence: Redis TTL)]
    LOG --> COMPACT[Snapshot / Compaction Workers]
    COMPACT --> OBJ[(Immutable Snapshot Store)]
    LOG --> BUS[Committed Operations Stream]
    BUS --> ASYNC[Search, audit, analytics]
\`\`\`

---

## Deep Dive 1 — OT and Convergence

- **Convergence:** same committed operations means same content.
- **Causality:** an edit made after observing another is ordered after it.
- **Intention preservation:** an edit affects logical text its author saw.

**Same-position inserts:** use a deterministic tie-breaker such as server receive
order plus \`authorId\`/\`opId\`. Both survive; the rule only orders them.

**Overlapping deletes:** transform functions shrink a delete by overlap already
deleted, preventing a double-delete. Insert/delete and formatting-range rules need
equally explicit rules and property tests: OT defects are data-corruption defects.

**Stale clients:** retain a bounded operation window. If \`baseRevision\` is older,
return \`RESYNC_REQUIRED\`: fetch a newer snapshot, replay pending local edits, then
submit. This bounds memory without silently losing work.

## Deep Dive 2 — Storage and Recovery

**Why both log and snapshot?** The append-only log supports cheap durable history,
replay, audit, and version restore. A snapshot avoids replaying millions of edits:
open the latest snapshot plus its newer-operation tail.

**Safe compaction:**

1. Replay through revision \`N\` and write immutable snapshot \`N\`.
2. Validate its checksum and mark it complete.
3. Atomically advance the document's snapshot pointer.
4. Retain/archive old logs according to restore and compliance policy.

Never overwrite a live snapshot in place.

**Idempotency:** persist \`opId\` with unique \`(documentId, opId)\`. A timeout retry
returns its original revision, not a second edit. Acknowledge only after replicated
durable commit; broadcasting first risks an edit vanishing after leader loss.

## Deep Dive 3 — Hot Documents, Failure, Multi-Region

**Thousands of viewers:** order each edit once, then gateways fan it out through a
document room/topic. Backpressure slow consumers; laggards receive a new snapshot,
rather than growing an unbounded queue.

**Leader failure:** a standby takes a fenced lease only after catching up through the
durable committed revision. Gateways reconnect from last known revision; \`opId\`
makes retries safe.

**Multi-region:** assign each document a home region for writes. Nearby gateways relay
to it, preserving a simple total order; replicate logs/snapshots for DR. This costs
distant editors cross-region latency but does not pretend active-active writers are
simple.

**Disconnect:** retain a bounded local queue and show offline state. On reconnect,
fetch missing commits, rebase local edits, and resubmit. If too stale, preserve the
local draft and resync rather than discard it.

---

## OT vs. CRDT

| Choice | OT (chosen) | CRDT |
|---|---|---|
| Core idea | Transform stale position-based edits against ordered history | Stable element IDs and merge rules combine replicas |
| Strength | Natural centralized server model; compact payloads | Strong offline, P2P, and multi-primary behavior |
| Cost | Complex transform/revision protocol | Metadata/tombstones and rich-text compaction |
| Use when | Online-first, server-authoritative editor | Offline-first or true active-active collaboration |

---

## Trade-offs and Cheat Sheet

| Decision | Benefit | Cost |
|---|---|---|
| WebSocket vs. polling | Low-latency bidirectional updates | Stateful connection/reconnect fleet |
| Per-document sequencer | Simple total order; scales across documents | Hot-document edits are serialized |
| OT vs. last-writer-wins | Preserves concurrent intent | Protocol/testing complexity |
| Operation log + snapshots | History, recovery, fast open | Compaction pipeline |
| TTL presence store | Cheap high-rate state | Presence may briefly vanish on failure |

- Clarify online-first versus offline-first first.
- Use WebSockets and small operations, never full-document saves.
- Sequence per document; transform, durably commit, revision, then broadcast.
- Separate durable content from ephemeral presence.
- Use \`opId\` for retries and snapshot-plus-rebase for lagging clients.
- Name CRDT when offline correctness or multi-primary writes is required.


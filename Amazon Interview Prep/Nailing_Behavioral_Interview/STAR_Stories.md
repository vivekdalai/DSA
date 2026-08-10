# My STAR Stories

Personal stories from real work experience, structured in STAR, tagged against the
[Leadership Principles](Leadership_Principles.md) they demonstrate. This is the "8-12
stories" bank that document recommends preparing (see its intro) — add to it as more
stories come up rather than writing a new one per LP.

**Template for adding a new story:** Situation (1-2 sentences of stakes/context) -> Task
(what you specifically owned) -> Action (the bulk — first-person "I," not "we") -> Result
(quantified, plus manager/peer reaction if any) -> one-line "what I'd do differently."
Then tag the LPs it hits and which common interview questions it answers.

------------------------------------------------------------------------

## Story 1: The HashMap Ordering Bug (Pipeline Orchestration, under load)

**Primary LP:** [Dive Deep](Leadership_Principles.md#12-dive-deep)
**Secondary LPs:** [Ownership](Leadership_Principles.md#2-ownership),
[Insist on the Highest Standards](Leadership_Principles.md#7-insist-on-the-highest-standards)

**Also answers (non-Amazon contexts):** JPMC's "what race conditions/production issues
have you debugged" (`JPMC Interview Prep/Behavioral_Leadership_Questions.md` §0.6 and
`JAVA Fundametals/Interview-Questions/JPMC_Java_Interview_Questions.md` Q17), and any
generic "tell me about a bug that only showed up in production" question.

---

**Situation:** I worked on a pipeline orchestration service where a pipeline is broken
down into individual tasks, with support for running multiple tasks in parallel within a
single pipeline for performance. The pipeline logic worked correctly in both dev and QA —
every test passed, every manual verification looked right — but it occasionally failed in
production, and only under heavy load. It never reproduced in lower environments no matter
how we tried.

**Task:** I needed to find the actual root cause of a bug that had two properties that
make it genuinely hard to chase: it was load-dependent (couldn't just attach a debugger and
step through it) and environment-dependent (dev/QA never showed it at all), so there was no
reliable way to reproduce it on demand.

**Action:** Instead of writing it off as one-off production flakiness — which would have
been the easy, defensible thing to do given it never reproduced anywhere else — I dug into
how the engine tracked task ordering internally. I found that tasks were stored in a
`HashMap<Integer, Block>`, keyed by the task's sequence number and mapped to its block
object, and some downstream logic implicitly assumed that iterating `keySet()` would come
back in sequence order.

That assumption happened to *look* true in dev/QA: with fewer tasks and lower concurrency,
`HashMap`'s internal bucket layout coincidentally landed close enough to sequential order to
mask the bug. Under production's heavier load — more tasks running in parallel, more
resizes as the map grew — `HashMap`'s real behavior surfaced: iteration order is
fundamentally unordered and implementation-dependent (driven by which bucket a key's hash
lands in, not by insertion or numeric order), so tasks were sometimes picked up out of
sequence, and the pipeline failed.

I root-caused it precisely to that incorrect ordering assumption, and fixed it by swapping
`HashMap<Integer, Block>` for `TreeMap<Integer, Block>` — which guarantees keys are always
iterated in sorted order (backed by a red-black tree) regardless of load, insertion
pattern, or internal resizing, so the fix removes the bug's *cause* rather than just
reducing its likelihood.

**Result:** The heavy-load production failures stopped. I got direct praise from my
managers for the depth of the root-cause analysis — specifically for catching a subtle
correctness bug that had been hiding behind "it works in dev/QA," rather than settling for
a surface-level retry-and-hope fix.

**What I'd do differently:** Treat "does this code rely on iteration order, and if so, does
the Map implementation actually guarantee it" as a decision made deliberately at write time
— not something to discover under production load. A `HashMap` used anywhere order matters
is a latent bug waiting for enough scale to expose it.

**Why this story is strong for Dive Deep specifically:** it has the concrete, almost
mundane detail interviewers are trained to listen for — not "I investigated the issue" but
the exact mechanism (`keySet()` iteration order, bucket layout, resize behavior). If asked
a technical follow-up, be ready to explain *why* dev/QA didn't show it (see
[`HashMaps.md`](../../JAVA%20Fundametals/Interview-Questions/HashMaps.md) Q45/Q46 for the
HashMap-vs-TreeMap ordering guarantee this story hinges on, and Q28-29 for why resize
behavior specifically is what shifts apparent ordering under load).

**Likely follow-ups to be ready for:**
- "Why didn't this show up in code review?" — have an honest answer (e.g. the assumption
  was implicit/undocumented, not a visible line of "wrong" code).
- "Why not just sort at read time instead of switching the data structure?" — good chance
  to show you considered alternatives: sorting on every read is O(n log n) repeated work,
  versus `TreeMap` maintaining sorted order incrementally (O(log n) per insert) — the fix
  addresses the actual access pattern instead of patching around it.
- "How did you verify the fix before shipping it?" — fill in what you actually did (load
  test in a lower environment, code review, monitoring after rollout) — have the real
  answer ready, don't let this be the one gap in an otherwise concrete story.

------------------------------------------------------------------------

## Story 2: Fixing Cache Split-Brain Across Pods (Pipeline Service Caching Layer)

**Primary LP:** [Invent and Simplify](Leadership_Principles.md#3-invent-and-simplify)
**Secondary LPs:** [Dive Deep](Leadership_Principles.md#12-dive-deep),
[Ownership](Leadership_Principles.md#2-ownership)

**Resume anchor:** this is the deeper story behind "Architected and integrated a
high-throughput caching layer within a core pipeline orchestration service, reducing
role-fetch latency from 400ms to 2ms (~200x performance optimization)" in
`VIVEK_DALAI_RESUME.pdf` — good to have both the perf number *and* this correctness story
ready, since interviewers often ask "was there a tradeoff/complication?" right after you
state a clean win like 200x.

---

**Situation:** On the same pipeline orchestration service, I built the caching layer
mentioned above. Instead of a cache-aside pattern (app checks a shared external cache like
Redis, falls back to the source on a miss, populates the cache — so every pod reads/writes
the same shared store), we used a write-through cache implemented *inside the service
itself* — each pod kept its own local in-memory cache, updated synchronously on writes.

**Task:** Because the cache was local to each pod rather than shared, a write on one pod
only updated that pod's own copy. In a horizontally-scaled deployment with multiple pod
replicas behind a load balancer, this created a split-brain problem: whichever pod handled
the write had fresh data, but every other pod kept serving its stale local value until its
own TTL happened to expire. I needed to fix this correctness gap without giving up the
performance the local write-through cache was there for in the first place.

**Action:** I researched how to keep multiple independent in-memory caches consistent
across pods without falling back to a fully shared external cache (which would reintroduce
a network hop per read and undercut the exact latency win the local cache existed for).
Kubernetes' regular `Service` only round-robins to *one* pod at a time via a virtual IP —
there's no built-in way to address "every pod" through it. I introduced a **headless
service** (a Kubernetes `Service` with `clusterIP: None`), which resolves via DNS directly
to the individual pod IPs instead of load-balancing through a single virtual IP, and used it
to build a small cache-orchestrator: on a write, the service enumerates every live pod
through the headless service's DNS resolution and pushes a cache update/invalidation to each
one directly, so every replica's local cache converges immediately instead of waiting on
TTL expiry to self-correct.

**Result:** The split-brain window was eliminated — all pods converged on fresh data
essentially immediately after a write, instead of "eventually consistent, up to TTL." This
preserved the local in-memory cache's latency win (the same 400ms -> 2ms result) while
closing the correctness gap that a naive per-pod write-through cache introduces the moment
you scale beyond one replica.

**What I'd do differently:** I'd evaluate the cache-aside-with-shared-store alternative more
formally up front (quantify the added network-hop latency vs. the complexity cost of
building and maintaining a custom orchestration layer) rather than only reaching for it
after the split-brain issue surfaced — the headless-service approach was the right call
here, but it's a build-vs-reuse tradeoff worth stating explicitly if asked.

**Why this story is strong for Invent and Simplify specifically:** the fix isn't "we
switched to the standard shared-cache pattern" (the obvious, textbook answer) — it's a
non-obvious use of a Kubernetes primitive (headless service, normally just for direct pod
addressing/StatefulSet discovery) repurposed as a cache-orchestration mechanism. Be ready to
explain *why* the obvious fix (shared external cache) was rejected — that's the part that
proves genuine invention rather than just picking a known pattern off a shelf.

**Likely follow-ups to be ready for:**
- "Why not just use a shorter TTL instead of building orchestration?" — good chance to
  explain the tradeoff: a shorter TTL only shrinks the staleness window, it doesn't
  eliminate it, and for pipeline orchestration data that drives execution decisions, even a
  short stale window can cause incorrect behavior — same category of bug as Story 1.
- "What happens if a pod joins or leaves while a cache update is being broadcast?" — have a
  real answer ready (e.g. a newly-joined pod's cache is cold and populates on first
  read/next TTL cycle; a departing pod simply won't receive the broadcast, which is
  harmless since it's gone) — this is the natural "what about edge cases" follow-up.
- "How is this different from just using Redis?" — be ready to state the tradeoff plainly:
  Redis (or another shared external cache) is simpler to reason about and avoids building
  custom orchestration, at the cost of a network hop on every read; the local-cache +
  headless-service-broadcast approach keeps reads in-process (hence the 2ms number) at the
  cost of the orchestration complexity you built.

------------------------------------------------------------------------

## Story 3: Automating CSV-Compatible ETL Migration to FRCDP

**Primary LP:** [Customer Obsession](Leadership_Principles.md#1-customer-obsession)
**Secondary LPs:** [Ownership](Leadership_Principles.md#2-ownership),
[Invent and Simplify](Leadership_Principles.md#3-invent-and-simplify),
[Insist on the Highest Standards](Leadership_Principles.md#7-insist-on-the-highest-standards)

---

**Situation:** Customers migrating ETL data-loading workloads to FRCDP needed to retain
their established CSV file formats. Requiring them to change file layouts would have added
cost, delayed migration, and risked disrupting downstream processes that depended on their
existing file patterns and column mappings.

**Task:** I owned building an automated migration capability that could create the required
EDDs and connector configurations from existing stage-table metadata, while validating that
the source schema was compatible with the corresponding FRCDP connector entities before
deployment.

**Action:** I developed the EDD and Connector services and integrated them into one FRCDP
migration workflow. The workflow generated EDD and connector payloads from versioned
configuration and source table metadata, preserving each customer's existing file patterns
and column mappings.

I added a compatibility layer that compared source-stage attributes with FRCDP connector
entities before submitting configurations to DSA APIs, so schema mismatches could be found
before deployment instead of becoming production data-loading failures. I also built the
operational safeguards required for a migration workflow: prerequisite validation,
idempotent handling for safe re-runs, detailed audit records, per-table success/failure
reporting, and batch-context logging for end-to-end traceability.

**Result:** Customers could migrate their ETL data-loading processes to FRCDP without
changing their existing CSV files. The workflow automated EDD and connector creation,
surfaced schema incompatibilities before deployment, supported safe retries, and gave
operators clear visibility into the outcome of every EDD and connector migration.

**What I'd do differently:** I would add a pre-migration compatibility report that teams
could review before starting a batch, so they could resolve all known schema exceptions in
advance instead of discovering them table by table during execution.

**Likely follow-ups to be ready for:**
- "How did you define compatibility between a stage table and a connector entity?" Be
  ready to describe the exact attributes you validated, such as column names, types,
  nullability, order, or mandatory FRCDP fields.
- "What made a re-run idempotent?" Explain how the workflow identified configurations
  already created or safely retried only incomplete or failed tables.
- "How did you handle a partially successful batch?" Describe how per-table status, audit
  records, and batch-context logs allowed an operator to isolate and retry failures without
  repeating successful work.

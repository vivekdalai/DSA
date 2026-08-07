# Redis Deep Dive (Amazon HLD Interviews)

Redis is the other component you'll drop into almost every HLD answer — as a cache
in front of a database, a rate limiter, a distributed lock, a leaderboard, or a
session store. This note covers the internals interviewers actually probe once you
say "we'd add a Redis cache here," plus the caching-pattern and hot-key follow-ups
that come up constantly at Amazon.

---

## 1. What Redis Actually Is

Redis (**RE**mote **DI**ctionary **S**erver) is an **in-memory data structure
store**. The "data structure" part is the key differentiator from a plain
key-value cache like Memcached — Redis values aren't just blobs, they're typed
structures the server understands and can operate on atomically.

```text
String      -> "user:42:name" -> "Vivek"
Hash        -> "user:42"      -> { name: "Vivek", age: 28 }
List        -> "queue:jobs"   -> [job1, job2, job3]           (push/pop from ends)
Set         -> "tags:post:9"  -> {golang, backend, aws}
Sorted Set  -> "leaderboard"  -> member -> score, ordered by score
Bitmap      -> "active:2026-08-07" -> bit per user_id (seen today?)
HyperLogLog -> "unique_visitors:today" -> approximate distinct count, tiny memory
Stream      -> "orders:stream" -> append-only log (Kafka-lite, consumer groups)
Geo         -> "drivers:geo"  -> lat/lon, radius queries
```

Because these are native server-side types, operations like "increment this
counter," "add to a sorted set and get the new rank," or "pop from a queue" happen
**atomically in one round trip**, without the client needing to read-modify-write
and risk a race.

---

## 2. Single-Threaded Event Loop Architecture

The single most important internal fact about Redis: **the core command
execution is single-threaded.**

```text
                     +-----------------------------+
   Client A -------->|                             |
   Client B -------->|   Single Event Loop         |
   Client C -------->|   (epoll/kqueue based I/O   |----> executes ONE
   Client D -------->|    multiplexing)             |      command at a time,
                     |                             |      fully atomically,
                     +-----------------------------+      no locks needed
                                   |
                                   v
                          In-memory data structures
```

Why this is actually a *feature*, not a limitation:

```text
No lock contention between commands -- every command runs to completion before
  the next one starts, so you get free atomicity on any single command (INCR,
  LPUSH, ZADD, etc.) without explicit locking.
Data lives entirely in RAM -- no disk seek latency on the read/write path.
I/O multiplexing (epoll) means one thread can still juggle thousands of
  concurrent client connections without blocking on any single socket.
```

The trade-off, and a favorite follow-up: **a single slow command blocks every
other client.** `KEYS *`, unbounded `SORT`, or a huge `SMEMBERS`/`LRANGE` on a
giant collection can stall the entire server for the duration of that command.
This is why production Redis usage bans `KEYS` in favor of `SCAN` (cursor-based,
non-blocking, incremental), and why very large collections are avoided or split.
(Redis 6+ does offload some work — like expiring lazily-freed large keys and I/O
threading for network reads/writes — to background threads, but command
*execution* itself remains single-threaded.)

---

## 3. Persistence: RDB vs. AOF

Redis is in-memory, so by default a restart loses everything unless you configure
persistence. Two mechanisms, often used together:

```text
RDB (snapshot):
  Periodically forks the process and writes a compact point-in-time binary
  snapshot of the whole dataset to disk (fork uses copy-on-write, so the
  parent keeps serving traffic while the child writes the snapshot).
  + Fast to restart from, compact file
  - Data since the last snapshot is lost on crash (minutes of data, typically)

AOF (Append-Only File):
  Logs every write command as it happens, replayed on restart to rebuild state
  (conceptually Redis's version of a write-ahead log).
  + Much smaller data-loss window; fsync policy controls it:
      always    -> fsync every write   (safest, slowest)
      everysec  -> fsync once a second (default, lose up to ~1s of writes)
      no        -> let the OS decide   (fastest, least safe)
  - Larger file than RDB; needs periodic AOF rewrite/compaction in the background

Redis 4+ default recommendation: both -- RDB for fast full restarts/backups,
AOF for a tight recovery point, with `appendfsync everysec` as the common
durability/performance balance.
```

Say explicitly in interviews: *"if Redis is your source of truth (not just a
cache), you need AOF with a tight fsync policy — if it's just a cache in front of
a real database, RDB or even no persistence is often fine, because a cold cache
just means a burst of cache misses, not data loss."* This framing is exactly the
kind of trade-off Amazon interviewers want to hear articulated, not just named.

---

## 4. Eviction Policies (`maxmemory`)

When Redis hits its configured memory limit, it needs a policy for what to evict:

```text
noeviction      -> reject writes with an error once full (safe, but breaks the app)
allkeys-lru     -> evict least-recently-used key, across ALL keys
volatile-lru    -> evict LRU among keys that have a TTL set
allkeys-lfu     -> evict least-frequently-used key (better for skewed access
                   patterns where "recently used once" shouldn't beat "used
                   constantly for days")
volatile-ttl    -> evict the key with the nearest expiry first
```

Redis doesn't do *exact* LRU/LFU — tracking a perfect global order would need
per-access bookkeeping on every read, which conflicts with staying fast and
memory-light. Instead it uses **approximated LRU/LFU via random sampling**: to
evict, it samples a small number of random keys (`maxmemory-samples`, default 5)
and evicts the "oldest-looking" one among the sample. Larger sample size gets
closer to true LRU at the cost of more CPU per eviction — a direct
accuracy-vs-performance knob worth naming.

---

## 5. Replication (Leader-Follower)

```text
        Master
     +----------+
     | Redis    |  <---- all writes go here
     | (Primary)|
     +----------+
        |    |
   async|    |async  (replication is asynchronous by default)
        v    v
  +--------+  +--------+
  |Replica1|  |Replica2|  <---- reads can be served from here
  +--------+  +--------+
```

```text
Full resync:   replica connects fresh -> master sends a full RDB snapshot,
               then streams subsequent write commands
Partial resync (PSYNC): if a replica briefly disconnects (network blip) and
               reconnects within the replication backlog window, the master
               replays just the missed commands instead of a full RDB transfer
               -- much cheaper for short outages.
```

**Consistency trade-off**: replication is asynchronous — the master doesn't wait
for a replica to acknowledge before confirming a write to the client. That means
**a failover can lose the last few writes** that hadn't replicated yet. Redis
offers the `WAIT` command to block until N replicas confirm, trading latency for
durability on specific writes, but it's opt-in per-command, not the default —
contrast this directly with Kafka's `acks=all` + ISR model in the Kafka deep dive,
which is a stronger default guarantee at the cost of write latency.

---

## 6. High Availability: Redis Sentinel

For automatic failover without manual intervention:

```text
   +-----------+   +-----------+   +-----------+
   | Sentinel 1|   | Sentinel 2|   | Sentinel 3|   <- monitor master + replicas,
   +-----------+   +-----------+   +-----------+     gossip with each other

   Sentinels continuously ping the master. If a QUORUM of Sentinels agree the
   master is unreachable, they elect one Sentinel to promote a replica to the
   new master, and reconfigure the other replicas + notify clients of the new
   master address.
```

Quorum-based failure detection exists specifically so that one Sentinel's own
network blip doesn't trigger a false failover — this mirrors the split-brain
concerns you'd raise for any leader-election system (comparable to Kafka's
controller election, or a Zookeeper/etcd-backed system).

---

## 7. Scaling Out: Redis Cluster (Hash Slots)

Sentinel gives you HA but not horizontal scale (single master still handles all
writes). **Redis Cluster** shards data across multiple masters:

```text
Keyspace is divided into 16384 fixed hash slots:
  slot = CRC16(key) % 16384

     Node A: slots 0-5460        Node B: slots 5461-10922      Node C: slots 10923-16383
    +---------------+           +---------------+            +---------------+
    | Master A      |           | Master B      |            | Master C      |
    | + Replica A'  |           | + Replica B'  |            | + Replica C'  |
    +---------------+           +---------------+            +---------------+

Client sends GET "user:42" -> computed slot doesn't live on the node it asked
  -> node replies "MOVED <slot> <address>" -> client redirects to the right node
  (during slot migration, "ASK" is used for the specific keys being moved)

Nodes gossip cluster topology/slot ownership among themselves (like Kafka's
  controller keeping metadata, or Dynamo-style gossip in the key-value store
  note) so every node eventually knows where every slot lives.
```

**Hash tags** let you force related keys into the same slot for multi-key
operations (which Cluster otherwise restricts to keys on the same node):
`{user:42}:profile` and `{user:42}:sessions` share the `{user:42}` tag, so both
hash to the same slot and can be operated on together (e.g. via a Lua script or
`MULTI`).

---

## 8. Caching Patterns (the practical Amazon-interview core)

```text
Cache-Aside (lazy loading) -- by far the most common pattern:
  1. App reads cache. Hit -> return.
  2. Miss -> read from DB -> write result into cache with a TTL -> return.
  + Simple, cache only holds what's actually requested
  - First request after a miss/expiry pays full DB latency (thundering herd
    risk if many requests miss at once -- see below)

Write-Through:
  App writes to cache AND DB synchronously on every write.
  + Cache never stale
  - Every write pays cache-write latency too; wasted work for data that's
    written but rarely read

Write-Behind (write-back):
  App writes to cache immediately, cache asynchronously flushes to DB later.
  + Fastest writes
  - Risk of data loss if cache crashes before flushing; more complex to build
    correctly (ordering, retry, dedup on flush)
```

**Cache stampede / thundering herd**: when a hot key expires, many concurrent
requests can all miss at once and hammer the database simultaneously to
recompute the same value.

```text
Mitigations:
  1. Add jitter to TTLs (TTL +/- random%) so hot keys don't all expire in sync
  2. Request coalescing / mutex-per-key: first request to miss acquires a short
     lock (e.g. SETNX) and repopulates the cache; others wait or serve stale
     briefly instead of all hitting the DB
  3. "Probabilistic early expiration": recompute slightly before actual TTL,
     proportional to how expensive the recompute is, so it happens gradually
     instead of all at once
  4. Never let a key expire in a way that leaves a total gap: e.g. keep
     serving a stale value for a short grace period while a background refresh
     happens
```

---

## 9. The Hot Key Problem

Same underlying issue as in the Kafka partitioning section, different mechanism:
in Redis Cluster, one key always lives on exactly one node. If a single key gets
disproportionate traffic (a viral post, a flash-sale item, a celebrity profile),
that one node's CPU/network becomes the bottleneck no matter how many nodes the
cluster has.

```text
Mitigations:
  1. Client-side (local, in-process) caching of the hottest keys, so most reads
     never even reach Redis
  2. Read replicas specifically for that node, with reads spread across them
  3. Key splitting: "product:999:views" -> "product:999:views:{0..9}",
     increment a random shard, sum shards when reading the total
  4. Detect hot keys proactively (sampling commands, or a sidecar that tracks
     access frequency) rather than discovering them from a paged incident
```

---

## 10. Redis Beyond Caching

```text
Distributed lock:
  SET lock:resource unique_token NX PX 30000   (atomic set-if-not-exists + TTL)
  release: compare token, then DEL (via Lua script, to make check+delete atomic)
  Caveat: this is safe for the common case, but Redlock (the multi-instance
  variant meant to be safer under partial failures) has been credibly disputed
  (Kleppmann's critique vs. antirez's rebuttal) -- for correctness-critical
  locking, reach for a system with a real consensus protocol (etcd/ZooKeeper)
  instead, and say so if asked.

Rate limiting:
  Fixed window: INCR request_count:{user}:{minute}, EXPIRE 60 -- simple, but
    bursts can double at window boundaries
  Sliding window: sorted set of request timestamps, ZREMRANGEBYSCORE to drop
    old entries, ZCARD to count -- more accurate, more memory per key
  Token bucket: Lua script combining a counter + last-refill timestamp for
    atomicity in one round trip

Leaderboard:
  ZADD leaderboard <score> <user_id>   -- O(log N) insert/update
  ZREVRANGE leaderboard 0 9            -- top 10, O(log N + 10)
  ZRANK leaderboard <user_id>          -- a specific user's rank, O(log N)

Pub/Sub:
  Fire-and-forget messaging, no persistence/replay (unlike Kafka) -- fine for
  ephemeral fan-out like "notify connected WebSocket servers of a new event,"
  wrong choice if a subscriber being briefly offline must not lose messages
  (use Redis Streams or Kafka for that instead).

Session store:
  HSET session:{id} ... with a TTL -- the textbook ElastiCache use case for
  stateless web tiers behind a load balancer.
```

---

## 11. Operational Pitfalls to Name Proactively

```text
KEYS * in production        -> O(N) full scan, blocks the single thread; use
                               SCAN (cursor-based, non-blocking) instead
Unbounded collections       -> a List/Set/Hash with millions of members makes
                               any O(N) command on it (LRANGE, SMEMBERS,
                               HGETALL) a stall risk
Big keys during eviction/expiry -> freeing a huge key can itself take
                               noticeable time; Redis 4+ offloads this to a
                               background thread (lazy freeing, UNLINK) instead
                               of blocking on DEL
No maxmemory policy set      -> defaults to noeviction; a cache that fills up
                               starts rejecting writes outright, not silently
                               evicting -- easy production surprise
```

---

## 12. Redis vs. Memcached vs. DynamoDB DAX / ElastiCache (the Amazon angle)

| | Redis | Memcached | DynamoDB DAX |
|---|---|---|---|
| Data model | Rich types (hash, sorted set, stream, etc.) | Plain key-value blobs only | Key-value, DynamoDB-API-compatible |
| Persistence | RDB/AOF optional | None — pure cache | Managed, tied to DynamoDB |
| Replication/HA | Sentinel or Cluster | Client-side sharding only, no built-in replication | Fully managed by AWS |
| Multi-threading | Single-threaded core (see section 2) | Multi-threaded | Managed, opaque |
| When to reach for it | Need atomic structures (rate limiter, leaderboard, lock), pub/sub, or richer ops than get/set | Pure caching, want the simplest possible model and multi-threaded throughput per node | Already on DynamoDB and want a drop-in read-through/write-through cache with zero app-level cache logic |

On AWS specifically, this maps directly onto **ElastiCache for Redis** vs.
**ElastiCache for Memcached** vs. **DAX** — worth naming the managed-service names
since Amazon interviewers appreciate you connecting the open-source concept to
the AWS service they'd actually deploy.

---

## 13. Common Interview Follow-Ups (Q&A)

**Q: Cache is down — what happens to the system?**
A: In cache-aside, every request falls through to the database — a "thundering
herd" against the DB the moment the cache is empty/unavailable, not just a slow
path. Mitigate with request coalescing, a circuit breaker that sheds load if the
DB is visibly struggling, and a warm-up/backfill strategy for cold starts after
planned maintenance.

**Q: How do you keep the cache consistent with the database on updates?**
A: Cache-aside naturally handles this on read-miss, but for writes you either (a)
invalidate the cache key on write (next read repopulates it) or (b) write-through.
Invalidate, don't update-in-place, is usually safer — it avoids races where a
slow write overwrites a cache entry with stale data after a faster concurrent
write already updated it.

**Q: Redis Cluster node dies — what's the blast radius?**
A: Only the slots owned by that node's master are affected until its replica is
promoted (automatic, similar to Sentinel's failover, built into Cluster itself) —
not the whole keyspace. Any in-flight async-replicated writes to that master that
hadn't reached its replica are lost, same durability trade-off as section 5.

**Q: Why not just make everything strongly consistent by waiting for replicas?**
A: You can, with `WAIT`, but it's per-command and adds latency to every such
write — Redis's default async replication exists precisely because most caching
use cases tolerate losing the last few milliseconds of writes far better than they
tolerate added latency on every write.

---

## 14. Trade-offs Considered

| Decision | Benefit | Cost |
|---|---|---|
| Cache-aside over write-through | Simple, cache only holds what's asked for | Read-miss thundering herd risk |
| AOF `everysec` over `always` | Much lower write latency | Up to ~1s of writes lost on crash |
| Approximate LRU (sampled) | Cheap, fast eviction decisions | Not perfectly accurate eviction order |
| Async replication (default) | Low write latency | Possible small data loss on failover |
| Redis Cluster sharding | Horizontal write scale | Multi-key ops restricted to same-slot keys (hash tags needed) |
| Redlock for distributed locking | Works for the common case, simple | Disputed correctness under partial failure — not a consensus system |

---

## Interview-Ready Summary

> I'd reach for Redis wherever we need atomic operations on rich data structures —
> counters, sorted sets for leaderboards/rate limits, or a cache layer in front of
> the primary database. Internally it's single-threaded per shard, which gives
> free atomicity on any single command but means unbounded O(N) commands can stall
> everything, so we'd standardize on SCAN over KEYS and bound collection sizes.
> For caching specifically I'd default to cache-aside with TTL jitter and request
> coalescing to avoid thundering-herd on expiry. For durability I'd size AOF's
> fsync policy to how much data loss is acceptable — tight for anything
> source-of-truth, loose for a pure cache. For scale beyond one node, Redis Cluster
> shards by hash slot, with Sentinel-style automatic failover per shard; the
> underlying trade-off throughout is that replication is asynchronous by default,
> so we're choosing availability and latency over the last few milliseconds of
> durability, unless a specific write needs `WAIT`.

---

## Key Concepts to Master

```text
Rich in-memory data structures and why that beats plain key-value caching
Single-threaded event loop: free atomicity vs. blocking-command risk
RDB vs. AOF, and matching fsync policy to how critical the data is
Approximate LRU/LFU eviction via sampling
Async replication and the durability trade-off vs. WAIT
Sentinel (HA/failover) vs. Cluster (sharding/scale) -- different problems
Cache-aside, write-through, write-behind, and stampede mitigation
Hot key detection and mitigation
Redis as locks/rate-limiter/leaderboard/pub-sub, and Redlock's real limits
```

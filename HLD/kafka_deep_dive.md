# Kafka Deep Dive (Amazon HLD Interviews)

Kafka shows up in Amazon HLD interviews as a **building block**, not usually as the
whole prompt — e.g. "how would the order service notify the inventory and shipping
services?" or "how do you decouple the write path from the notification fan-out?"
This note is written so you can drop Kafka into any design and defend it, and also
answer the deep-dive follow-ups interviewers chase once you mention it.

---

## 1. What Kafka Actually Is

Kafka is a **distributed, durable, append-only log**, exposed as a publish/subscribe
system. The "durable log" framing matters more than "pub/sub" — it's what
distinguishes Kafka from SQS/SNS/RabbitMQ:

```text
Traditional queue (SQS-style):
  message consumed -> deleted -> gone

Kafka:
  message appended -> stays on disk for a retention window
  multiple independent consumers can each replay the same log
  from any offset they choose
```

This "replayable log" property is the reason Kafka is chosen over a plain queue:
multiple teams (analytics, fraud, search-indexing, notifications) can each consume
the same event stream independently, at their own pace, without stepping on
each other.

---

## 2. Core Building Blocks

```text
Topic       -> a named stream of records (e.g. "order-events")
Partition   -> an ordered, immutable log; a topic is split into N partitions
Broker      -> a Kafka server; hosts some partitions
Producer    -> writes records to a topic
Consumer    -> reads records from a topic
Consumer Group -> a set of consumers that split a topic's partitions between them
Offset      -> a record's position within a partition (monotonically increasing)
Replica     -> a copy of a partition; one is the Leader, others are Followers
```

**Ordering guarantee**: Kafka only guarantees order **within a single partition**,
never across partitions in a topic. This single fact drives almost every design
decision below — partitioning key choice, hot-partition problems, and why consumer
parallelism is capped at partition count.

---

## 3. Cluster Architecture (Diagram)

```text
 Producers                         Kafka Cluster
+----------+        key="user:42" hashed to partition 1
| Order    |------------------+
| Service  |                  |
+----------+                  v
                    +-------------------+-------------------+-------------------+
                    |     Broker 1      |     Broker 2      |     Broker 3      |
                    |-------------------|-------------------|-------------------|
                    | Topic "orders"    | Topic "orders"    | Topic "orders"    |
                    |   P0  (Leader)    |   P1  (Leader)    |   P2  (Leader)    |
                    |   P1  (Follower)  |   P2  (Follower)  |   P0  (Follower)  |
                    |   P2  (Follower)  |   P0  (Follower)  |   P1  (Follower)  |
                    +-------------------+-------------------+-------------------+
                              ^                   ^                   ^
                              |                   |                   |
                    +---------+---------+---------+---------+---------+
                    |         Consumer Group: "inventory-service"     |
                    |   C1 -> reads P0   C2 -> reads P1   C3 -> reads P2 |
                    +---------------------------------------------------+

                    A second, independent group can replay the SAME log:
                    Consumer Group: "fraud-detection" (separate offsets, own pace)
```

Each partition has exactly one **Leader** replica; all reads/writes for that
partition go through the leader, and followers pull from it to stay in sync. This
is what makes Kafka horizontally scalable for writes: different partitions have
different leaders on different brokers, so write load spreads across the cluster.

---

## 4. Partitioning Deep Dive

**Why partitions exist**: a partition is the unit of parallelism. One partition can
only be actively written/read by one broker (leader) and, within a consumer group,
by one consumer at a time. More partitions = more parallel throughput, at the cost
of weaker ordering guarantees (only per-partition, not per-topic) and more
per-partition bookkeeping overhead.

**Choosing a partition for a record**:

```text
If a key is provided:
  partition = hash(key) % num_partitions
  -> guarantees all records with the same key land in the same partition,
     so they are strictly ordered relative to each other (e.g. all events
     for order_id=42 are ordered)

If no key is provided:
  sticky round-robin across partitions (batches efficiently, no ordering guarantee)
```

**The hot partition problem**: if your key is skewed (e.g. partitioning by
`seller_id` and one seller does 40% of volume, or partitioning by `tenant_id` on a
huge enterprise customer), that one partition's leader broker takes a
disproportionate share of load while others sit idle — you don't get the
parallelism you paid for.

Mitigations, in order of preference:

```text
1. Pick a better key (compound key: seller_id + order_id) so load spreads out
   while still keeping per-order ordering
2. Salt the key: append a random suffix (0-N) for high-volume keys only,
   then merge/re-aggregate downstream (loses strict per-key ordering)
3. Custom partitioner with explicit overrides for known hot keys
4. Increase partition count (helps only if the skew is spread over many
   moderately-hot keys, not one dominant key)
```

**Picking partition count up front matters** because you can add partitions later
but doing so **reshuffles which key maps to which partition** (since it's
`hash(key) % N`), breaking ordering guarantees for existing keys mid-stream. A
common rule of thumb: size partitions for peak expected throughput ÷ target
per-partition throughput (Kafka partitions comfortably handle a few MB/s to tens of
MB/s each), and err on the side of a few more than you need, since consumer count
can grow into unused partitions later without a repartition.

---

## 5. Replication & Durability

Each partition is replicated (typically `replication.factor=3`). Only followers
that are sufficiently caught-up are in the **ISR (In-Sync Replica set)**.

```text
acks=0    -> producer doesn't wait for any ack (fastest, can silently lose data)
acks=1    -> waits for the partition LEADER to write (can lose data if leader
             dies before followers replicate)
acks=all  -> waits for ALL replicas in the ISR to acknowledge (safest, higher
             latency)

min.insync.replicas=2  -> combined with acks=all, a write is rejected outright
                           if fewer than 2 replicas (leader + 1 follower) are
                           in sync -- trades availability for durability
```

**Unclean leader election trade-off**: if the leader dies and no in-sync follower
is available, Kafka can either (a) wait for an in-sync replica to come back
(favors durability, but the partition is unavailable meanwhile), or (b)
`unclean.leader.election.enable=true`, promote an out-of-sync follower anyway
(favors availability, but silently loses whatever the old leader had that never
replicated). This is a direct CAP-theorem trade-off you should be ready to name.

```text
      Leader (Broker 1)                 Followers pull, don't get pushed to
      +----------------+
      | Partition 0    |----replicate---->  Broker 2 (Follower, in ISR)
      | offset: 1042   |----replicate---->  Broker 3 (Follower, in ISR)
      +----------------+
      acks=all -> client gets ack only after both followers confirm offset 1042
```

---

## 6. Producer Internals

```text
Batching:    records for the same partition are buffered and sent together
             (linger.ms = how long to wait to fill a batch; batch.size = max bytes)
Compression: batches compressed (snappy/lz4/zstd) before sending -> less network,
             less disk
Idempotent producer: each producer gets a PID + per-partition sequence number;
             broker deduplicates retries, so producer retries after a network
             blip don't create duplicate records (enable.idempotence=true)
Transactions: group writes to multiple partitions/topics atomically -- either
             all become visible to consumers or none do (used for exactly-once
             read-process-write pipelines)
```

Batching + compression is why Kafka producers get *higher* throughput with a
little added latency (`linger.ms`) — this is the classic **throughput vs. latency**
trade-off to mention explicitly.

---

## 7. Consumer Groups & Rebalancing

```text
Rule: within one consumer group, each partition is consumed by exactly ONE
      consumer at a time. So max parallelism for a group = number of partitions.

Topic "orders" has 6 partitions, Group "inventory-service" has 3 consumers:
  C1 -> P0, P1      C2 -> P2, P3      C3 -> P4, P5

Add a 4th consumer -> rebalance:
  C1 -> P0, P1      C2 -> P2, P3      C3 -> P4      C4 -> P5

Add a 7th consumer (more consumers than partitions) -> it sits IDLE.
```

Committed offsets (where each group last read up to) are stored durably in an
internal Kafka topic, `__consumer_offsets` — this is how a consumer that
crashes and restarts (or a rebalance that hands its partition to a different
consumer) knows exactly where to resume, without needing an external
coordination store.

**Rebalancing cost**: the classic ("eager") protocol stops *all* consumers in the
group, reassigns every partition, then resumes — a "stop-the-world" pause. Modern
Kafka uses the **cooperative sticky assignor**, which only reassigns the specific
partitions that actually need to move, letting unaffected consumers keep
processing. Worth naming this directly if asked "what happens when a consumer
joins/leaves" — it signals you know this used to be a real production pain point
(rebalance storms during rolling deploys) and that it's been substantially
mitigated.

---

## 8. Delivery Semantics

```text
At-most-once:  commit offset BEFORE processing -> if the consumer crashes mid-
               processing, that record is skipped on restart. Fast, can lose work.

At-least-once: commit offset AFTER processing -> if the consumer crashes after
               processing but before committing, the record is reprocessed on
               restart. Default in most setups; requires idempotent downstream
               handling (e.g. upsert by order_id, not "increment balance").

Exactly-once:  requires idempotent producer + transactional writes across the
               read-offset-commit and the output write (Kafka's transactional
               API), OR an idempotent consumer design (dedupe by a unique key on
               the write side) layered on top of at-least-once. True end-to-end
               exactly-once across a non-Kafka sink (e.g. a database) still
               ultimately relies on that sink supporting idempotent writes.
```

Say this explicitly in interviews: **"exactly-once" is a Kafka-internal guarantee
between Kafka topics; the moment you write to an external system, you're back to
needing idempotent writes on that system to get end-to-end exactly-once
semantics.** Interviewers listen for this nuance.

---

## 9. How Kafka Achieves High Throughput (the "how does it manage load" answer)

This is the section to lead with when asked "how does Kafka handle load so
efficiently":

```text
1. Sequential disk I/O only
   Every partition is an append-only log -- writes are always sequential
   appends, never random-access writes. Sequential I/O on spinning disks (and
   even SSDs) is dramatically faster than random I/O, which is what lets Kafka
   get near-disk-bandwidth write throughput.

2. OS page cache, not JVM heap
   Kafka deliberately avoids caching data in the JVM heap. It writes through
   the OS page cache and lets the OS manage it. Consumers reading recent data
   are usually reading straight from page cache (RAM speed), not disk.

3. Zero-copy transfer (sendfile)
   When sending data to a consumer, Kafka uses the OS's sendfile() syscall to
   move bytes from the page cache directly to the network socket, without
   copying through the broker's application (JVM) memory at all. Fewer copies,
   fewer context switches, higher throughput per core.

4. Batching everywhere
   Producers batch records; brokers write/replicate/fsync in batches;
   consumers fetch in batches. Every layer amortizes fixed overhead (network
   round-trip, syscall, disk seek) over many records.

5. Partitions as the scaling unit
   Throughput scales horizontally by adding partitions (more parallel leaders
   spread across more brokers) and adding brokers (more disks, more network,
   more CPU), not by making one machine bigger.
```

The one-line version to say out loud: *"Kafka is fast mainly because it turns
everything into sequential disk I/O and lets the OS do the memory/network work it's
already good at, instead of the broker doing extra copying."*

---

## 10. Storage: Log Segments, Retention & Compaction

```text
A partition's log on disk is split into SEGMENT files (e.g. 1 GB each):
  00000000000000000000.log
  00000000000000524288.log
  00000000000001048576.log   <- active segment, currently being appended to

Retention (default): delete whole segments once they're older than
  retention.ms or the partition exceeds retention.bytes -- cheap, because it's
  just deleting files, not scanning/rewriting the log.

Compaction (log.cleanup.policy=compact): instead of deleting by age, Kafka
  keeps only the LATEST record per key, garbage-collecting older values for
  the same key in the background. Used for topics that represent "current
  state" (e.g. a changelog topic backing a KTable: user_id -> latest profile),
  not raw event history.
```

---

## 11. Failure Handling

```text
Broker dies:
  -> Every partition it LED needs a new leader. The cluster controller
     promotes an in-sync follower to leader for each affected partition.
  -> Kafka 3.x+ uses KRaft (Kafka's own Raft-based metadata quorum) for
     controller election instead of depending on an external ZooKeeper
     cluster -- one less moving distributed system to operate.

Producer retries:
  -> Transient errors (leader not available, network blip) trigger automatic
     retries. Idempotent producer config prevents these retries from creating
     duplicate records on the broker.

Consumer dies:
  -> Its partitions get reassigned to other consumers in the group at the
     next rebalance; processing resumes from the last COMMITTED offset for
     that partition (which is why at-least-once, not exactly-once, is the
     default assumption unless you did the extra work in section 8).
```

---

## 12. Kafka vs. SQS vs. SNS vs. Kinesis (the Amazon-specific angle)

Amazon interviewers will absolutely ask "why not just use SQS/SNS here?" — have
this ready:

| | Kafka | SQS | SNS | Kinesis Data Streams |
|---|---|---|---|---|
| Model | Durable replayable log | Point-to-point queue | Fan-out pub/sub, no storage | Durable replayable log (AWS-managed) |
| Replay history | Yes, within retention window | No — deleted on consume | No | Yes, within retention window |
| Multiple independent consumer groups | Yes, natively | Needs one queue per consumer | Yes, via subscriptions | Yes, via separate iterators |
| Ordering | Per-partition | FIFO queues only, and capped throughput | None guaranteed | Per-shard |
| Ops burden | You run/tune it (or MSK) | Fully managed | Fully managed | Fully managed |
| When to reach for it | High-throughput event streaming, multiple downstream consumers replaying the same events, strict per-key ordering | Simple decoupled task queue, one consumer side, want zero ops | Fan-out notifications, no replay needed | Same use case as Kafka but want AWS-managed with tighter native AWS service integration |

The interview-safe answer: *"I'd reach for Kafka (or Kinesis, if we want it fully
AWS-managed) specifically because multiple independent teams need to replay the
same event stream at their own pace with strict ordering per key — that's the one
thing a plain queue like SQS structurally can't give you, since a queue deletes a
message once it's consumed."*

---

## 13. Common Interview Follow-Ups (Q&A)

**Q: How many partitions should a topic have?**
A: Start from target throughput ÷ realistic per-partition throughput (a few MB/s
to tens of MB/s), then round up a bit so consumer count has room to grow without a
repartition. More partitions also means more open file handles and more
replication traffic per broker, so it's not "just add more forever."

**Q: What happens if a consumer is much slower than the others in its group?**
A: It doesn't block other consumers — each partition's consumer processes
independently — but it does cause growing **consumer lag** on its own partitions
(the gap between latest offset and its committed offset), which is exactly what
you'd alert on. If that's unacceptable, split into more partitions/consumers, or
move slow processing to an async downstream step.

**Q: How do you prevent one hot partition from becoming a bottleneck?**
A: See section 4 — better key design first, salting/splitting second, custom
partitioner as a targeted override.

**Q: How would you do exactly-once delivery to a database?**
A: Combine at-least-once consumption with an idempotent write on the database side
— e.g. `INSERT ... ON CONFLICT (order_id) DO UPDATE`, or a unique constraint plus
upsert — so re-processing the same record after a crash-and-retry is a no-op
rather than a duplicate.

**Q: How do you scale Kafka for multi-region?**
A: Kafka clusters are region-local; cross-region replication is handled by a
separate tool (MirrorMaker 2 or MSK Replicator) that mirrors topics between
clusters, typically active-passive for disaster recovery or active-active with
careful handling of offset translation between clusters, since offsets aren't
globally consistent across independent clusters.

---

## 14. Trade-offs Considered

| Decision | Benefit | Cost |
|---|---|---|
| More partitions | More parallelism, higher throughput | More rebalancing overhead, more open file handles per broker, weaker cross-partition ordering |
| `acks=all` + `min.insync.replicas=2` | Strong durability, survives single broker loss | Higher write latency, reduced availability if replicas fall behind |
| Unclean leader election enabled | Partition stays available during an outage | Silent data loss of unreplicated writes |
| Log compaction over time-based retention | Bounded storage for "current state" topics | Loses full history — can't replay intermediate states |
| Larger `linger.ms` / batch size | Higher producer throughput, better compression ratio | Higher per-record latency |
| Kafka over SQS | Replay, multiple independent consumer groups, strict per-key ordering | You own more operational complexity (or pay for MSK) |

---

## Interview-Ready Summary

> I'd use Kafka when multiple independent consumers need to process the same event
> stream at their own pace with replay capability, which a plain queue like SQS
> can't give you. Topics are split into partitions, which are the unit of both
> ordering (only guaranteed within a partition) and parallelism (one consumer per
> partition per group). Each partition is replicated across brokers with a
> leader/follower model — `acks` and `min.insync.replicas` tune the durability vs.
> latency/availability trade-off. Kafka gets its throughput mainly from turning
> everything into sequential disk I/O, serving reads out of the OS page cache with
> zero-copy transfer, and batching at every layer. Consumer groups track progress
> via committed offsets stored in Kafka itself, so a crashed consumer resumes
> exactly where it left off after a rebalance. The main things I'd watch for in
> production are hot partitions from skewed keys, consumer lag, and rebalance
> impact during deploys.

---

## Key Concepts to Master

```text
Partitions as the unit of ordering AND parallelism
Leader/follower replication, ISR, acks, min.insync.replicas
Idempotent producers vs. transactional exactly-once
Consumer groups, offset commits, cooperative rebalancing
Sequential I/O + page cache + zero-copy as the throughput story
Hot partition detection and mitigation
Kafka vs. SQS/SNS/Kinesis trade-offs
```

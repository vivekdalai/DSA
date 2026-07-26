# Key-Value Store HLD Notes

A **key-value store** is one of the most common High-Level Design topics because it teaches core distributed systems concepts: hashing, replication, consistency, partitioning, failures, and scaling.

---

## 1. What is a Key-Value Store?

A key-value store stores data as:

```text
key -> value
```

Example:

```text
"user:123" -> "{ name: 'Vivek', age: 28 }"
"session:abc" -> "{ userId: 123, expiresAt: ... }"
"cart:456" -> "{ items: [...] }"
```

Basic operations:

```text
put(key, value)
get(key)
delete(key)
```

Popular examples:

```text
Redis, DynamoDB, Cassandra, Riak, RocksDB, etcd
```

At HLD level, think of it as a **distributed hash map**.

---

## 2. Basic Single-Machine Design

On one server, the simplest design is:

```text
Client -> Key-Value Server -> Storage
```

The server can keep data in memory using a hash map:

```text
Map<String, String>
```

Operations:

```text
PUT key, value
GET key
DELETE key
```

This gives very fast reads and writes.

But one server has limits:

```text
Limited memory/storage
Single point of failure
Cannot handle huge traffic
Data lost if machine crashes
```

So we need a distributed design.

---

## 3. Distributed Key-Value Store

At scale, data is spread across many servers.

```text
Client
  |
  v
Load Balancer / Router
  |
  v
+---------+   +---------+   +---------+
| Node A  |   | Node B  |   | Node C  |
+---------+   +---------+   +---------+
```

The key question is:

> Which node stores which key?

That is solved using hashing.

---

## 4. Partitioning Using Hashing

Suppose we have 3 nodes.

```text
node = hash(key) % number_of_nodes
```

Example:

```text
hash("user:123") % 3 = 1
```

So the key goes to Node B.

### Problem

When a node is added or removed, many keys move.

```text
hash(key) % 3
hash(key) % 4
```

The result changes for many keys, causing massive data movement.

---

## 5. Consistent Hashing

Consistent hashing solves the large data movement problem.

Imagine a ring from `0` to `2^32 - 1`.

```text
        Node A
          |
   Node C   Node B
```

Both nodes and keys are hashed onto the ring.

A key is stored on the first node clockwise from its position.

Example:

```text
hash("user:123") lands between Node A and Node B
=> store on Node B
```

When a node joins or leaves, only nearby keys move, not all keys.

This is why consistent hashing is commonly used in distributed key-value stores.

---

## 6. Replication

If each key is stored on only one node, node failure means data loss.

So we replicate data.

Example with replication factor 3:

```text
key -> primary node + next 2 nodes on ring
```

```text
"user:123" stored on:
Node B
Node C
Node D
```

Benefits:

```text
High availability
Fault tolerance
Read scalability
```

If Node B dies, Node C or D can serve the request.

---

## 7. Read and Write Flow

### Write Flow

```text
Client sends PUT("user:123", value)
Router finds responsible nodes
Write is sent to replicas
System returns success after enough replicas acknowledge
```

Example:

```text
Replication factor N = 3
Write quorum W = 2
```

This means the write is considered successful after 2 out of 3 replicas confirm.

### Read Flow

```text
Client sends GET("user:123")
Router finds replicas
Reads from one or more replicas
Returns latest value
```

Example:

```text
Read quorum R = 2
```

The system reads from 2 replicas and picks the latest version.

---

## 8. Quorum Consistency

A common rule:

```text
R + W > N
```

Where:

```text
N = number of replicas
R = read quorum
W = write quorum
```

Example:

```text
N = 3
W = 2
R = 2
```

Since:

```text
R + W = 4 > 3
```

At least one read replica will have the latest write.

This improves consistency, but it increases latency because the system waits for more nodes.

---

## 9. Consistency Models

Key-value stores often trade strong consistency for availability and speed.

### Strong Consistency

After a write succeeds, every read gets the latest value.

Good for:

```text
Bank balance
Inventory count
Distributed locks
```

Harder to scale.

### Eventual Consistency

After a write, some replicas may briefly return old data, but eventually all replicas converge.

Good for:

```text
User profiles
Shopping carts
Likes
Session metadata
```

Easier to scale and highly available.

---

## 10. Handling Conflicts

Suppose two clients write the same key at the same time:

```text
Client A writes value X
Client B writes value Y
```

Different replicas may receive writes in different orders.

Conflict resolution options:

### Last Write Wins

Use timestamp. Latest timestamp wins.

Simple, but can lose updates.

### Version Vectors

Track causal history of updates.

More accurate, but more complex.

### Application-Level Merge

Return multiple versions and let the application resolve.

Example:

```text
Shopping cart merge
```

If two carts conflict, merge the item lists.

---

## 11. Failure Handling

A production key-value store must handle:

```text
Node crashes
Network partitions
Slow nodes
Disk failure
Data corruption
```

Common techniques:

### Replication

Multiple copies of data.

### Heartbeats

Nodes periodically signal that they are alive.

```text
Node A -> Coordinator: I am alive
```

### Gossip Protocol

Nodes share cluster state with each other.

```text
Node A tells Node B what it knows
Node B tells Node C
```

Eventually all nodes learn which nodes are alive or dead.

### Hinted Handoff

If a replica is down, another node temporarily stores the write.

When the failed node comes back, the temporary node forwards the missed writes.

---

## 12. Anti-Entropy and Merkle Trees

Replicas can drift apart.

Example:

```text
Node A has value v1
Node B has value v2
```

Anti-entropy is the process of comparing replicas and repairing differences.

A **Merkle tree** helps compare large datasets efficiently.

Instead of comparing every key, nodes compare hashes of ranges.

```text
If root hash matches -> data is same
If root hash differs -> compare child hashes
```

This quickly finds which key ranges differ.

---

## 13. Storage Engine

A key-value store can store data in memory or on disk.

### In Memory

Very fast.

Example:

```text
Redis
```

But memory is expensive and volatile unless persisted.

### On Disk

More durable and cheaper.

Common storage structures:

```text
LSM Tree
B+ Tree
Hash index
```

For write-heavy systems, LSM trees are common.

Write path with LSM:

```text
Write to WAL
Write to MemTable
Flush MemTable to SSTable on disk
Compact SSTables in background
```

Where:

```text
WAL = Write-Ahead Log
MemTable = in-memory sorted structure
SSTable = immutable sorted file on disk
Compaction = merging files to remove duplicates/deletes
```

---

## 14. Caching

A key-value store may itself be used as a cache, but internally it can also cache hot keys.

Example:

```text
"user:999" is read very frequently
```

The system can keep it in memory.

But beware of hot keys.

---

## 15. Hot Key Problem

Some keys may receive huge traffic.

Example:

```text
"celebrity:profile:123"
"worldcup:score"
"flashsale:item:999"
```

If one key maps to one shard, that shard becomes overloaded.

Solutions:

```text
Replicate hot keys more widely
Cache hot keys at edge/application level
Split one hot key into multiple keys
Use request coalescing
```

Example splitting:

```text
counter:item:999:shard:1
counter:item:999:shard:2
counter:item:999:shard:3
```

Then aggregate later.

---

## 16. Typical HLD Architecture

```text
                 +---------+
                 | Client  |
                 +---------+
                      |
                      v
              +----------------+
              | API Gateway /  |
              | Request Router |
              +----------------+
                      |
          +-----------+-----------+
          |                       |
          v                       v
   +-------------+         +-------------+
   | Metadata /  |         | Cluster     |
   | Ring Config |         | Membership  |
   +-------------+         +-------------+
                      |
                      v
        +-------------+-------------+
        |                           |
        v                           v
   +---------+                 +---------+
   | Node A  |                 | Node B  |
   | WAL     |                 | WAL     |
   | MemTable|                 | MemTable|
   | SSTable |                 | SSTable |
   +---------+                 +---------+
        |                           |
        v                           v
   +---------+                 +---------+
   | Replica |                 | Replica |
   +---------+                 +---------+
```

---

## 17. APIs to Mention in Interviews

```text
PUT /kv/{key}
GET /kv/{key}
DELETE /kv/{key}
```

Example write:

```http
PUT /kv/user:123
{
  "name": "Vivek",
  "age": 28
}
```

Response:

```json
{
  "status": "success",
  "version": 42
}
```

Example read:

```http
GET /kv/user:123
```

Response:

```json
{
  "key": "user:123",
  "value": {
    "name": "Vivek",
    "age": 28
  },
  "version": 42
}
```

---

## 18. Design Requirements

For an interview, start with requirements.

### Functional Requirements

```text
put(key, value)
get(key)
delete(key)
Support large values? Maybe limited to 1 MB
TTL support? Optional
```

### Non-Functional Requirements

```text
High availability
Low latency
Horizontal scalability
Durability
Fault tolerance
Configurable consistency
```

Example target:

```text
99.9% availability
p99 read latency under 10 ms
Support billions of keys
Support millions of QPS
```

---

## 19. CAP Theorem

CAP says a distributed system can only fully guarantee two of:

```text
Consistency
Availability
Partition tolerance
```

Since network partitions are unavoidable, you usually choose between:

```text
CP: consistency + partition tolerance
AP: availability + partition tolerance
```

Dynamo-style key-value stores usually lean AP with eventual consistency.

Systems like etcd or ZooKeeper lean CP with strong consistency.

---

## 20. Interview Answer Structure

Use this flow:

```text
1. Clarify requirements
2. Define APIs
3. Estimate scale
4. Start with single-node design
5. Move to distributed design
6. Explain partitioning with consistent hashing
7. Add replication
8. Discuss read/write quorum
9. Discuss consistency trade-offs
10. Handle failures
11. Explain storage engine
12. Discuss hot keys and scaling
13. Mention monitoring and operations
```

---

## Interview-Ready Summary

> I would design the key-value store as a distributed hash table. Keys are partitioned across nodes using consistent hashing. Each key is replicated to multiple nodes for fault tolerance. Writes go to a configurable quorum of replicas, and reads can also use quorum reads depending on the consistency requirement. Each storage node uses a write-ahead log for durability and an LSM-tree-based storage engine for efficient writes. Failures are handled using heartbeats, gossip, hinted handoff, and anti-entropy repair using Merkle trees.

---

## Key Concepts to Master

```text
Consistent hashing
Replication
Quorum reads/writes
Eventual consistency
Conflict resolution
WAL + LSM tree
Failure handling
Hot key mitigation
```

These are the backbone of a strong key-value store HLD answer.

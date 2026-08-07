# HashMap Interview Questions

*Reviewed 2026-08-07: fixed corrupted characters (mojibake from a bad encoding save) and
expanded answers that were thin, incomplete, or slightly imprecise. Kept the rapid-fire
Q&A format for quick pre-interview review; added short code snippets only where the
question is genuinely a "predict the output" / gotcha type.*

## Core Concept Questions
1. ***What happens internally when you call `put()` in a HashMap?***
    - `put()` computes `hash(key)` -> spreads the bits -> finds the bucket via
      `hash & (capacity - 1)` -> if the bucket is empty, insert a new node -> else walk the
      bucket (list or tree) comparing `hash` first, then `equals()`, to see if the key
      already exists -> update the value in place, or append/insert a new node.

2. ***How does `get()` work internally?***
    - `get()` computes the same spread hash -> locates the bucket -> traverses it
      (linked list, or a red-black tree if treeified) -> uses `hash` as a cheap
      pre-filter and `equals()` as the final check to match the key -> returns the
      value, or `null` if no match is found.

3. ***Why is HashMap's average time complexity O(1)?***
    - Because a good hash function distributes keys uniformly across buckets, so each
      bucket holds a small, roughly constant number of entries regardless of total map
      size. This is an *average-case, well-distributed-hash* guarantee, not a hard
      guarantee — a poor `hashCode()` can degrade it (see Q37/Q54).

4. ***What is the role of `hashCode()` and `equals()` in HashMap?***
    - `hashCode()` decides *which bucket* a key lands in; `equals()` resolves *which
      entry within that bucket* is the actual match (needed because different keys can
      share a bucket via collision).

5. ***Why must `equals()` and `hashCode()` be consistent?***
    - The contract: if `a.equals(b)` is `true`, then `a.hashCode() == b.hashCode()` must
      also be true. If violated, two "equal" keys could land in different buckets, and
      `get()` would never find the entry inserted under the other key — see Q16/Q19.

## Hashing & Indexing
6. ***Why is the formula `index = hash & (n - 1)` used instead of `%`?***
    - Bitwise AND is a single fast CPU instruction vs. the (slower) division-based
      modulo. It also naturally handles negative hash codes correctly — `%` in Java can
      return a negative result for a negative dividend, which would be an invalid array
      index, while `hash & (n - 1)` is always non-negative because it only ever looks at
      the low bits.

7. ***Why must the capacity of HashMap be a power of 2?***
    - `hash & (n - 1)` is only mathematically equivalent to `hash % n` when `n` is a
      power of 2 — that's exactly when `(n - 1)` is a contiguous run of 1-bits (e.g.
      `16 - 1 = 15 = 0b1111`), so the AND cleanly masks out the low bits. If capacity
      weren't a power of 2, `(n - 1)` would have 0-bits mixed in, and the mask would
      silently ignore certain hash bits, causing uneven, non-uniform bucket distribution
      even with a well-spread hash.

8. ***What is hash spreading and why is it needed?***
    - Java 8's `HashMap.hash()` does: `h = key.hashCode(); return h ^ (h >>> 16);` —
      XOR-ing the high 16 bits into the low 16 bits before masking with `(n - 1)`.
      Since the bucket index only uses the low bits of the hash (Q6), a `hashCode()`
      that varies mostly in its *high* bits (common for some hash implementations)
      would otherwise collapse to the same few buckets. Spreading mixes that
      high-bit entropy down into the bits that actually matter for indexing.

9. ***What happens if `hashCode()` returns the same value for all objects?***
    - Every key hashes to the same bucket — the "map" degenerates into a single linked
      list (or a single tree once past the treeify threshold), so `put`/`get` become
      O(n) / O(log n) instead of O(1). Correctness is unaffected, only performance.

10. ***Can hashCode be negative? How does HashMap handle it?***
    - Yes — `hashCode()` returns an `int`, which is signed. `hash & (n - 1)` still
      produces a valid non-negative index because AND only depends on the low bits of
      the two's-complement representation, unlike `%` which can return a negative value.

## Collision Handling
11. ***What happens when two keys have the same hashCode?***
    - They land in the same bucket (a genuine hash collision). HashMap first compares
      the stored `hash` (a cheap int comparison) as a pre-filter, then falls back to
      `equals()` to determine if it's truly the same key or just a collision between two
      different keys.

12. ***How are collisions handled before Java 8 vs after Java 8?***
    - Pre-Java 8: every bucket is a singly linked list, so a heavily collided bucket
      degrades to O(n) lookup.
    - Java 8+: a bucket starts as a linked list, but converts to a red-black tree once it
      crosses the treeify threshold (subject to the capacity condition in Q15), bounding
      worst-case lookup within that bucket to O(log n).

13. ***What is treeification in HashMap?***
    - The Java 8+ process of converting an overcrowded bucket's linked list into a
      red-black tree (nodes become `TreeNode`, ordered by hash and, as a tiebreaker, by
      class name / `compareTo`), so lookups within that bucket become O(log n) instead
      of O(n).

14. ***Why is the treeify threshold 8 and untreeify threshold 6?***
    - **8 (treeify):** with a well-distributed hash function, bucket sizes roughly
      follow a Poisson distribution; the JDK's own analysis shows the probability of a
      bucket reaching 8 entries at the default 0.75 load factor is astronomically low
      (well under 1 in a million). So treeification is a rare safety net against
      pathological hash functions or adversarial input, not something that should
      trigger in normal, well-behaved usage — 8 was chosen as "rare enough that tree
      overhead (extra memory per node, tree-balancing cost) isn't worth paying until
      truly needed."
    - **6 (untreeify, on removal):** deliberately lower than 8, not the same value. This
      gap is *hysteresis* — if both thresholds were 8, a bucket hovering at exactly 8
      entries would thrash back and forth between list <-> tree conversion on every
      insert/remove near that boundary, which is wasteful. The gap means a bucket has to
      shrink meaningfully before converting back.

15. ***Under what conditions does HashMap convert a bucket to a tree?***
    - Both must hold: bucket size exceeds `TREEIFY_THRESHOLD` (8), **and** the table's
      total capacity is at least `MIN_TREEIFY_CAPACITY` (64). If the bucket is
      overcrowded but capacity is still below 64, HashMap **resizes the whole table
      instead of treeifying** — the reasoning is that a small table with one crowded
      bucket is more likely just under-sized than genuinely collision-heavy, so growing
      the table (spreading entries into new buckets) is tried first.

## equals() & hashCode() Edge Cases
16. ***What happens if you override `equals()` but not `hashCode()`?***
    - Two "equal" objects (per your `equals()`) can still get different default
      (identity-based) hash codes, so they land in different buckets. `map.get(key2)`
      can return `null` even though `key2.equals(key1)` is `true` and `key1` is in the
      map — retrieval silently fails.

17. ***What happens if you override `hashCode()` but not `equals()`?***
    - Objects land in the correct (same) bucket, since `hashCode()` is consistent, but
      the bucket scan falls back to the default `equals()` (reference/identity
      equality), which fails for two distinct-but-logically-equal instances — lookup by
      a "new" logically-equal key object still fails.

18. ***Can two unequal objects have the same hashCode?***
    - Yes — that's simply a hash collision, and it's explicitly allowed by the contract.
      HashMap is built to handle this correctly via `equals()`; it's a performance
      concern (more entries per bucket), not a correctness bug.

19. ***Can two equal objects have different hashCodes?***
    - No — this directly violates the `equals()`/`hashCode()` contract (Q5) and will
      break `HashMap`, `HashSet`, and any hash-based collection's ability to find the
      key again.

20. ***What happens if `equals()` is inconsistent?***
    - (e.g. `equals()` depends on a mutable field, or isn't symmetric/transitive) —
      leads to unpredictable behavior: an entry might be findable one moment and not the
      next, `containsKey()` and `get()` can disagree, and duplicate "equal" entries can
      end up coexisting in the map.

## Tricky Code-Based Questions
21. ***Predict output when inserting duplicate keys.***
    - The old value is silently replaced by the new one; `size()` stays the same;
      `put()` returns the *previous* value (not the new one) — a common gotcha when
      people expect `put()` to return `this`/`void`-like or the new value.
    ```java
    Map<String, Integer> m = new HashMap<>();
    m.put("a", 1);
    Integer old = m.put("a", 2);  // old == 1, m.size() == 1, m.get("a") == 2
    ```

22. ***What happens if you mutate a key after inserting it into HashMap?***
    - Retrieval fails, because the bucket index was computed from the key's hash code
      *at insertion time*. If the mutation changes the fields used in `hashCode()`, the
      key now "belongs" in a different bucket than the one it's actually stored in —
      `get()` recomputes the hash from the *current* (mutated) state and looks in the
      wrong bucket, silently missing the entry. The entry isn't deleted; it's just
      unreachable — a memory leak in practice. This is exactly why mutable objects make
      poor map keys (see also why `String` is deliberately immutable).
    ```java
    class Key { int id; }               // mutable, hashCode() based on id
    Key k = new Key(); k.id = 1;
    map.put(k, "value");
    k.id = 2;                            // mutate after insertion
    map.get(k);                          // likely null — looks in bucket for id=2
    ```

23. ***Why does `map.get(new Key(...))` sometimes return null even if logically equal?***
    - Almost always one of: (a) `hashCode()`/`equals()` weren't overridden at all
      (default identity semantics), (b) they were overridden inconsistently with each
      other (Q16/Q17), or (c) the original key was mutated after insertion (Q22) so its
      current hash no longer matches its stored bucket.

24. ***What happens if key fields used in hashCode change?***
    - Same root cause as Q22 — the map becomes internally inconsistent: the entry sits
      in the bucket for the *old* hash, but any future lookup computes the *new* hash
      and searches a different bucket. The entry becomes unreachable via `get()`, but
      still counts toward `size()` and will still appear if you iterate `entrySet()`.

25. ***Can HashMap store itself as a key (or value)?***
    - Syntactically yes, but it's dangerous: `hashCode()` and `toString()` on the outer
      map would recurse into the self-referencing entry, and if the map's own
      `hashCode()`/`toString()` implementation isn't specially guarded, this causes
      infinite recursion and a `StackOverflowError`. (`HashMap` itself does special-case
      a map containing itself in a few places, but relying on this is bad practice —
      avoid self-referential structures as keys entirely.)
    ```java
    Map<Object, Object> m = new HashMap<>();
    m.put(m, "self");
    m.hashCode();   // StackOverflowError risk in general self-referential structures
    ```

## Internal Behavior
26. ***What is load factor and default value?***
    - Load factor = `size / capacity` threshold that triggers a resize. Default = 0.75
      — a tuned balance between wasted space (low load factor -> more empty buckets)
      and collision rate (high load factor -> more entries per bucket).

27. ***When does resizing happen?***
    - When `size > capacity * loadFactor` after an insertion (i.e. crossing the
      `threshold` field, which is precomputed as `capacity * loadFactor`).

28. ***What happens during rehashing?***
    - A new backing array of double the old capacity is allocated, and every existing
      node is redistributed into the new array based on its bucket index in the new,
      larger table. See Q29 for the Java 8 optimization of *how* that redistribution
      actually happens.

29. ***Does resizing recompute every key's `hashCode()`?***
    - **No — this is a common misconception.** Java 8+ avoids recomputing
      `hashCode()`/re-spreading for every entry. Since capacity always doubles, an
      entry's new index is either the *same* index, or `old index + old capacity` —
      determined entirely by checking a single extra bit of the already-stored hash
      (`hash & oldCapacity`). HashMap splits each old bucket's list into a "low" list
      (bit is 0, stays at the same index) and a "high" list (bit is 1, moves to
      `index + oldCapacity`), preserving relative order within each list and doing this
      in O(1) per node — no `hashCode()` calls and no `equals()` calls needed during
      resize at all.

30. ***What is the default initial capacity?***
    - Default = 16 (`DEFAULT_INITIAL_CAPACITY`). If you construct
      `new HashMap<>(expectedSize)`, note the constructor doesn't use `expectedSize`
      directly as capacity — it computes the next power of 2 via `tableSizeFor()`, and
      because resizing is still keyed off `capacity * loadFactor`, sizing the map for
      `n` known entries without any resize usually means constructing with roughly
      `n / 0.75` (or just using `Math.ceil(n / 0.75) + 1`) so the threshold isn't
      crossed immediately.

## Null Handling
31. ***Why does HashMap allow only one null key?***
    - Keys are unique by definition (it's a map), and `null` is treated as a valid,
      singular key value — so at most one entry can have a `null` key, same as any
      other key. (Values have no such restriction — many entries can each independently
      have a `null` value.)

32. ***How are null keys stored internally?***
    - `HashMap.hash(null)` is special-cased to return `0` (since you can't call
      `null.hashCode()`), so a null key always maps to bucket index `0`.

33. ***Why does Hashtable not allow null keys/values?***
    - `Hashtable` predates `HashMap` (JDK 1.0, retrofitted onto the Collections
      Framework) and its design choice was to treat `null` as reserved/ambiguous —
      `get(key) == null` was meant to unambiguously signal "key not present." Allowing a
      null *value* would break that signal (you couldn't tell "absent" from "present
      with null value" using `get()` alone). `HashMap` relaxed this and instead offers
      `containsKey()` to disambiguate. See also Q43 — `ConcurrentHashMap` reintroduces
      the same restriction, for a related but distinct concurrency reason.

## Performance & Optimization
34. ***What causes performance degradation in HashMap?***
    - A poor `hashCode()` (clusters keys into few buckets), a load factor set too high,
      or a genuinely adversarial workload designed to force collisions (hash-flooding
      DoS — a real concern for public-facing servers, which is one reason the treeify
      fallback in Q13-15 exists).

35. ***How does a bad hashCode affect performance?***
    - Causes clustering — many keys funnel into few buckets, so those buckets' internal
      lists (or trees) grow long, and lookups within them degrade toward O(n) (or
      O(log n) if treeified) instead of O(1).

36. ***Why is 0.75 used as default load factor?***
    - Empirically tuned by the JDK authors as a good time/space trade-off: high enough
      to avoid excessive wasted array slots, low enough to keep the expected number of
      collisions per bucket small for a reasonably-distributed hash function.

37. ***What is the worst-case time complexity of HashMap?***
    - O(n) if the implementation never treeifies (pre-Java 8, or a bucket below the
      `MIN_TREEIFY_CAPACITY` size threshold from Q15) — e.g. all keys colliding into one
      bucket.

38. ***How does Java 8 improve worst-case performance?***
    - Once a bucket is treeified (Q13-15), lookups within that bucket become O(log n)
      instead of O(n), bounding the damage a bad hash function or adversarial input can
      do.

## Concurrency & Threading
39. ***Is HashMap thread-safe?***
    - No — concurrent structural modification (put/remove causing resize) from multiple
      threads without external synchronization can corrupt internal state.

40. ***What issues occur if multiple threads use HashMap?***
    - Lost updates (two threads' `put()`s race and one is silently overwritten),
      `size()` inconsistency, and in the worst case (pre-Java 8) a corrupted internal
      structure — see Q41.

41. ***What was the infinite loop issue in pre-Java 8 HashMap?***
    - Pre-Java 8's resize used **head insertion** when moving a bucket's list to the new
      table (each transferred node was pushed onto the front of its new bucket's list).
      If two threads resized concurrently, this could interleave in a way that reversed
      part of a list into a **cycle** (node A points to B and B points back to A). Once
      that cycle existed, `get()` traversing that bucket would loop forever, pinning a
      CPU core at 100% — a well-known production outage cause. Java 8's resize
      (Q29, tail-insertion-preserving lo/hi split) does not exhibit this specific bug,
      but `HashMap` is still not thread-safe for other reasons (Q40) — use
      `ConcurrentHashMap` for concurrent access regardless of JDK version.

42. ***Difference between HashMap and ConcurrentHashMap?***
    - `HashMap` has no internal synchronization at all. `ConcurrentHashMap` is
      thread-safe without locking the entire map for every operation: Java 8+ uses
      per-bin `synchronized` blocks (on the first node of a bin) combined with CAS
      operations for lock-free inserts into empty bins, giving high concurrent
      throughput. It also guarantees `get()` never blocks and iterators are weakly
      consistent (fail-safe, not fail-fast) rather than throwing
      `ConcurrentModificationException`.

43. ***Why doesn't ConcurrentHashMap allow null keys or values?***
    - Doug Lea's (the author's) stated reasoning: in a single-threaded `HashMap`, if
      `get(key)` returns `null` you can disambiguate "key absent" from "key present with
      null value" by calling `containsKey(key)` afterward. In a **concurrent** map, that
      two-step check is inherently racy — another thread could insert or remove the key
      between your `get()` and your `containsKey()` calls, so the disambiguation isn't
      reliable. Disallowing `null` outright removes the ambiguity entirely: `get() ==
      null` always and unambiguously means "not present," even under concurrent
      modification.

## Comparison Questions
44. ***HashMap vs Hashtable?***
    - `HashMap`: not synchronized (fast, but not thread-safe), allows one null key +
      multiple null values, no legacy baggage. `Hashtable`: synchronized on every method
      (thread-safe but slow, coarse-grained locking), disallows null keys/values, is a
      legacy class from JDK 1.0 — prefer `ConcurrentHashMap` over `Hashtable` for new
      concurrent code.

45. ***HashMap vs LinkedHashMap?***
    - `LinkedHashMap` extends `HashMap` and additionally maintains a doubly linked list
      through all entries, preserving **insertion order** by default (or **access
      order** if constructed with `accessOrder = true` — the basis of the LRU cache
      pattern in `Q57` and in `JPMC_Java_Interview_Questions.md` Q10). Slightly more
      memory/bookkeeping overhead per entry than plain `HashMap`.

46. ***HashMap vs TreeMap?***
    - `TreeMap` keeps keys in **sorted order** (natural ordering via `Comparable`, or a
      supplied `Comparator`), backed by a red-black tree — O(log n) for
      put/get/remove, vs `HashMap`'s O(1) average. `TreeMap` also supports range
      operations (`headMap`, `tailMap`, `firstKey`, `ceilingKey`, etc.) that `HashMap`
      cannot.

47. ***When would you prefer TreeMap over HashMap?***
    - When you need entries in sorted key order for iteration, or need range queries
      (e.g. "all entries between key X and Y," "closest key <= X") — none of which
      `HashMap` supports at all.

48. ***Difference between identity-based and equals-based maps?***
    - `IdentityHashMap` uses reference identity (`==`) and `System.identityHashCode()`
      instead of `equals()`/`hashCode()` — two distinct-but-equal objects are treated as
      *different* keys. Niche use cases: topology-preserving object graph traversal,
      serialization frameworks, or anywhere you deliberately want reference semantics
      even for objects that override `equals()`.

    | | HashMap | Hashtable | LinkedHashMap | TreeMap | ConcurrentHashMap |
    |---|---|---|---|---|---|
    | Thread-safe | No | Yes (coarse) | No | No | Yes (fine-grained) |
    | Null key | 1 allowed | None | 1 allowed | None (throws NPE) | None |
    | Order | None | None | Insertion/access | Sorted | None |
    | put/get | O(1) avg | O(1) avg | O(1) avg | O(log n) | O(1) avg |

## Advanced / Deep Dive
49. ***How does HashMap resize internally (step-by-step)?***
    1. An insert pushes `size` past `threshold` (`capacity * loadFactor`).
    2. A new array of double the capacity is allocated.
    3. Each old bucket's entries are split into a "low" and "high" sub-list using one
       extra hash bit — **no hashCode recomputation** (see Q29's correction).
    4. The low sub-list stays at the same index in the new table; the high sub-list
       moves to `index + oldCapacity`.
    5. `threshold` is recomputed as `newCapacity * loadFactor`.

50. ***What is the role of bitwise operations in HashMap?***
    - `hash & (n - 1)` for O(1) index calculation (Q6-7), `h ^ (h >>> 16)` for hash
      spreading (Q8), and `hash & oldCapacity` to decide low/high placement during
      resize without recomputing hashes (Q29).

51. ***How does HashMap maintain performance during high collisions?***
    - Treeification (Q13-15) bounds a single overcrowded bucket to O(log n) instead of
      O(n); resizing (Q27-29) proactively keeps the average bucket size small before it
      gets that bad in the first place.

52. ***What is the structure of Node in HashMap?***
    - Java 8+'s `Node<K,V>` (implements `Map.Entry<K,V>`) holds: `final int hash`, `final
      K key`, `V value`, and `Node<K,V> next` (the singly-linked-list pointer within a
      bucket). Once treeified, entries become `TreeNode<K,V>` instead, which extends
      `LinkedHashMap.Entry` and adds parent/left/right/prev pointers plus a `red`
      boolean for red-black tree balancing.

53. ***How does HashMap handle memory vs performance trade-offs?***
    - Load factor (Q26) controls the space/collision trade-off directly; capacity
      doubling (rather than growing by a fixed increment) amortizes resize cost to O(1)
      per insertion on average, at the cost of potentially over-allocating up to ~2x the
      strictly-needed space right after a resize.

## Real-world / Design Thinking
54. ***When would HashMap perform poorly in production?***
    - Poor/weak `hashCode()` implementations (e.g. always returning a constant, or one
      derived from only a low-entropy field), keys that are mutated after insertion
      (Q22), or adversarial input specifically crafted to collide (hash-flooding) if
      user-controlled strings are used as keys without treeification protection.

55. ***How would you design a better hash function?***
    - Combine all significant fields (not just one), use prime multipliers to reduce
      systematic collisions (the classic `31 * result + field.hashCode()` pattern used
      by `Objects.hash()` / IDE-generated `hashCode()`), and ensure the result has good
      bit-level entropy across its full range (not just the low bits, since Q8's
      spreading step helps but can't fix a fundamentally weak hash).

56. ***What happens if all keys land in one bucket?***
    - The map degenerates to a single linked list (or tree, once past the Q15
      threshold) — every `put`/`get` degrades toward O(n) (or O(log n) if treeified),
      even though `size()` and overall correctness are unaffected.

57. ***Can HashMap be used as a cache? What are limitations?***
    - Yes for a simple unbounded cache, but plain `HashMap` has no eviction policy, no
      TTL/expiry, and isn't thread-safe. For an actual LRU cache, use `LinkedHashMap`
      with `accessOrder = true` and override `removeEldestEntry()` (see
      `JPMC_Java_Interview_Questions.md` Q10 for the full implementation) — or a
      dedicated caching library (Caffeine, Guava `LoadingCache`) for TTL, size-based
      eviction, and concurrent access.

58. ***How would you debug a slow HashMap?***
    - Check bucket distribution (is one or a few buckets abnormally large — indicates a
      weak `hashCode()`), verify `hashCode()`/`equals()` are actually overridden for
      custom key types, check whether resizing is happening excessively (undersized
      initial capacity relative to actual entry count — see Q30), and confirm keys
      aren't being mutated after insertion (Q22).

## Related Notes in This Repo
- [`../Revision-Notes/06_HashMaps.md`](../Revision-Notes/06_HashMaps.md) — broader
  HashMap usage/syntax notes.
- [`JPMC_Java_Interview_Questions.md`](JPMC_Java_Interview_Questions.md) — Q1/Q2 cover
  the same HashMap/ConcurrentHashMap internals in the "explain it live" interview format,
  plus the `LinkedHashMap`-based LRU cache implementation referenced in Q57 above.

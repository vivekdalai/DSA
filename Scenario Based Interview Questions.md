# Scenario Based Interview Questions

## 1. Your API is slow under load. Where will you start debugging?

I would start by confirming whether the problem is really application latency, downstream latency, database latency, or infrastructure saturation.

### How I would debug

1. Check key metrics:
   - Request rate or throughput
   - Average latency
   - P95 and P99 latency
   - Error rate
   - CPU usage
   - Memory usage
   - GC activity
   - Thread pool usage
   - Database connection pool usage

2. Compare normal traffic vs peak traffic:
   - Is latency increasing only when traffic increases?
   - Is there a sudden spike after a deployment?
   - Are only some APIs slow or all APIs slow?

3. Check application logs:
   - Slow request logs
   - Exception logs
   - Timeout logs
   - Retry logs
   - Downstream service failures

4. Use distributed tracing:
   - Identify where the request spends most of its time.
   - Check whether delay is in controller, service logic, DB query, cache, external API, or message broker.

5. Check database performance:
   - Slow queries
   - Missing indexes
   - Lock waits
   - Long-running transactions
   - Connection pool exhaustion

6. Check JVM behavior:
   - High GC pause time
   - Memory leak
   - Too many blocked threads
   - Thread pool exhaustion

7. Reproduce with load testing:
   - Use tools like JMeter, Gatling, k6, or Locust.
   - Test gradually increasing traffic.
   - Identify the breaking point.

### Possible fixes

- Add caching for repeated reads.
- Optimize slow database queries.
- Add missing indexes.
- Increase connection pool size only after checking DB capacity.
- Add pagination instead of returning huge responses.
- Use async processing for heavy work.
- Tune thread pools.
- Remove unnecessary synchronous calls.
- Add rate limiting and backpressure.
- Scale horizontally if the application is stateless.

### Good interview answer

I would not randomly increase server capacity first. I would start with metrics and tracing to find the bottleneck. Once I know whether the issue is CPU, memory, DB, thread pool, downstream service, or bad code path, I would apply a targeted fix.

---

## 2. A downstream service is failing. How will you prevent cascading failure?

A cascading failure happens when one failing service causes other services to slow down or fail because they keep waiting, retrying, or consuming resources.

### How I would prevent it

1. Set proper timeouts:
   - Never wait indefinitely for a downstream service.
   - Configure connection timeout and read timeout.

2. Use circuit breaker:
   - If downstream failures cross a threshold, stop calling it temporarily.
   - Return fallback response or fail fast.
   - After some time, allow limited test requests.

3. Use fallback logic:
   - Return cached data.
   - Return default response.
   - Disable only the affected feature.
   - Show partial response if possible.

4. Use bulkheads:
   - Isolate thread pools or connection pools per downstream service.
   - One bad dependency should not consume all application threads.

5. Control retries:
   - Use limited retries.
   - Use exponential backoff.
   - Add jitter.
   - Do not retry non-retryable errors like validation failures.

6. Add rate limiting:
   - Protect the downstream service from overload.
   - Protect your own service from too many stuck requests.

7. Monitor dependency health:
   - Failure rate
   - Timeout rate
   - Latency
   - Circuit breaker state

### Example

If a payment service is down, the order service should not keep waiting forever. It should timeout quickly, open the circuit breaker, mark payment as pending, and process it later through a queue.

### Good interview answer

I would use timeouts, circuit breakers, fallbacks, bulkheads, and controlled retries. The goal is to fail fast, degrade gracefully, and prevent one failing service from bringing down the whole system.

---

## 3. Your system processes duplicate requests. How will you ensure idempotency?

Idempotency means the same request can be sent multiple times but the final result remains the same.

### Common duplicate request causes

- Client retries due to timeout
- Network issues
- User double-clicking a button
- Message broker redelivery
- Retry jobs
- Service crash after processing but before sending response

### How I would handle it

1. Use an idempotency key:
   - Client sends a unique key for each operation.
   - Server stores the key with request status and response.
   - If the same key comes again, return the previous result instead of processing again.

2. Use database unique constraints:
   - For example, `order_id`, `transaction_id`, or `request_id` should be unique.
   - Even if two duplicate requests reach the DB, only one insert succeeds.

3. Store request processing state:
   - `IN_PROGRESS`
   - `SUCCESS`
   - `FAILED`

4. Make operations naturally idempotent:
   - `PUT /user/1/status` with `ACTIVE` is idempotent.
   - `POST /charge` is not idempotent unless protected by an idempotency key.

5. Use transactions:
   - Save idempotency key and business data in the same transaction.
   - Avoid check-then-insert race conditions.

6. Handle message consumers carefully:
   - Store processed message IDs.
   - Use deduplication table.
   - Commit offset only after successful processing.

### Example

For payment processing:

- Client sends `Idempotency-Key: abc-123`.
- First request creates payment and stores response.
- Duplicate request with same key returns the same payment result.
- It does not charge the customer twice.

### Good interview answer

I would use an idempotency key, persist it with a unique constraint, and make the operation transactional. For async systems, I would also deduplicate messages using message IDs or business keys.

---

## 4. Your service works locally but fails in production. What will you check?

I would compare local and production environments because this usually happens due to configuration, infrastructure, data, security, or dependency differences.

### Things I would check

1. Configuration:
   - Environment variables
   - Spring profiles
   - Feature flags
   - Config server values
   - Secrets and credentials

2. Database:
   - Correct DB URL
   - Schema migration applied or not
   - Data differences
   - DB permissions
   - Connection pool settings

3. Network:
   - DNS resolution
   - Firewall rules
   - Security groups
   - Service discovery
   - Load balancer routing

4. Dependency versions:
   - Java version
   - Library versions
   - Docker image version
   - OS-level dependencies

5. Logs and errors:
   - Startup logs
   - Stack traces
   - Health check failures
   - Timeout errors

6. Resource limits:
   - CPU limit
   - Memory limit
   - Container restart count
   - Disk space

7. Security:
   - TLS certificates
   - Expired credentials
   - Missing IAM permissions
   - CORS or authentication configuration

8. Deployment changes:
   - Recent code changes
   - Recent config changes
   - Recent DB migrations
   - Recent infrastructure changes

### Good interview answer

I would first check logs, configuration, environment variables, database connectivity, network rules, dependency versions, and resource limits. Most local-vs-production issues come from environment mismatch rather than code logic alone.

---

## 5. Thread pool gets exhausted. How will you fix it?

Thread pool exhaustion means all threads are busy and new tasks cannot be processed. This usually causes high latency, rejected requests, or application hangs.

### How I would debug

1. Identify which thread pool is exhausted:
   - Web server thread pool
   - Async executor
   - Scheduler thread pool
   - Database connection pool related waiting
   - Message listener thread pool

2. Check metrics:
   - Active threads
   - Queue size
   - Rejected task count
   - Task execution time
   - Waiting time

3. Take thread dumps:
   - Check if threads are blocked on DB calls.
   - Check if threads are waiting on locks.
   - Check if threads are stuck in external API calls.
   - Check for deadlocks.

4. Check for missing timeouts:
   - HTTP client timeout
   - DB query timeout
   - Redis timeout
   - Kafka or queue timeout

### Common causes

- Slow downstream service
- Long-running DB queries
- Deadlock
- Blocking calls inside async code
- Too many scheduled jobs
- Unbounded queue
- Pool size too small
- Traffic spike

### Fixes

- Add timeouts to all external calls.
- Optimize slow DB queries.
- Use circuit breakers for failing dependencies.
- Increase pool size only after understanding why threads are busy.
- Use separate pools for different workloads.
- Use bounded queues.
- Apply backpressure.
- Move long-running tasks to a queue.
- Use non-blocking IO where suitable.

### Good interview answer

I would not blindly increase thread count. I would take thread dumps and check whether threads are CPU-bound, blocked, waiting for DB, waiting for downstream services, or deadlocked. Then I would fix the actual bottleneck and tune the pool safely.

---

## 6. Your logs are not enough to debug an issue. What will you improve?

Logs should help answer what happened, where it happened, for which request, and why it failed.

### Improvements I would make

1. Add structured logging:
   - Use JSON logs.
   - Include fields like request ID, user ID, order ID, API name, status, and latency.

2. Add correlation ID:
   - Generate or accept a correlation ID at the entry point.
   - Pass it across all services.
   - Log it everywhere.

3. Improve exception logging:
   - Log complete stack trace.
   - Log meaningful error message.
   - Avoid swallowing exceptions silently.

4. Add important business context:
   - Payment ID
   - Order ID
   - Customer ID
   - Request type
   - Current state

5. Add logs at important boundaries:
   - Request received
   - Validation failed
   - DB operation started or failed
   - Downstream call started or failed
   - Message published or consumed

6. Avoid bad logging:
   - Do not log passwords.
   - Do not log tokens.
   - Do not log full card numbers.
   - Do not log huge request bodies unnecessarily.

7. Add metrics and tracing:
   - Logs alone are not enough.
   - Add metrics for rate, latency, error count, queue size, and pool usage.
   - Add distributed tracing to follow request flow.

8. Use proper log levels:
   - `INFO` for important lifecycle events
   - `WARN` for recoverable problems
   - `ERROR` for failures requiring attention
   - `DEBUG` for detailed troubleshooting

### Good interview answer

I would add structured logs, correlation IDs, meaningful context, full exception details, and distributed tracing. I would also ensure sensitive information is masked.

---

## 7. A deployment breaks communication between services. How will you handle versioning?

This usually happens when one service changes its API contract but another service still expects the old contract.

### How I would prevent this

1. Make backward-compatible changes:
   - Add new fields instead of removing old fields.
   - Do not rename fields directly.
   - Do not change field meaning.
   - Do not change response structure suddenly.

2. Use API versioning:
   - URI versioning: `/api/v1/orders`
   - Header versioning: `Accept-Version: v1`
   - Media type versioning if needed

3. Use consumer-driven contract testing:
   - Verify that provider changes do not break consumers.
   - Tools like Pact can help.

4. Use schema evolution:
   - For events, maintain compatibility in Kafka or messaging schemas.
   - Avoid removing fields immediately.
   - Use optional fields.

5. Use safe deployment strategies:
   - Rolling deployment
   - Canary deployment
   - Blue-green deployment
   - Feature flags

6. Follow deprecation process:
   - Introduce new version.
   - Support old and new versions together.
   - Notify consumers.
   - Remove old version only after migration.

### Example

Bad change:

```json
{
  "customerName": "Rahul"
}
```

changed to:

```json
{
  "name": "Rahul"
}
```

This can break consumers.

Better change:

```json
{
  "customerName": "Rahul",
  "name": "Rahul"
}
```

Support both for some time, then deprecate the old field.

### Good interview answer

I would avoid breaking API contracts. I would use backward-compatible changes, API versioning, contract tests, canary deployments, and a proper deprecation strategy.

---

## 8. Your database connections are exhausted. What could be wrong?

Database connection exhaustion means the application needs more DB connections than the pool can provide, or connections are not being released properly.

### Possible causes

1. Connection leak:
   - Connections are opened but not closed.
   - Usually happens with manual JDBC code.

2. Long-running queries:
   - Queries hold connections for too long.
   - Missing indexes can make this worse.

3. Long transactions:
   - Business logic runs inside transaction for too long.
   - External API calls happen inside transaction.

4. Pool size too small:
   - Traffic increased but pool size stayed the same.

5. Pool size too large:
   - Too many app instances multiplied by pool size overload the DB.

6. High concurrency:
   - Too many requests hitting DB at once.

7. Database locks:
   - Queries are waiting because rows or tables are locked.

8. Not using pagination:
   - Large queries consume resources for too long.

### How I would debug

- Check connection pool metrics.
- Check active vs idle connections.
- Check connection wait time.
- Check timeout errors.
- Check slow query logs.
- Check DB lock waits.
- Check thread dumps.
- Check transaction boundaries.

### Fixes

- Fix connection leaks.
- Use try-with-resources for JDBC.
- Optimize slow queries.
- Add indexes.
- Keep transactions short.
- Avoid external calls inside DB transactions.
- Tune connection pool size based on DB capacity.
- Add read replicas for heavy read traffic.
- Add caching for repeated reads.

### Good interview answer

I would check for connection leaks, long queries, long transactions, locks, and incorrect pool sizing. Pool size tuning should be done carefully because increasing every service pool can overload the database.

---

## 9. You see inconsistent data due to concurrent updates. How will you handle it?

Concurrent updates happen when multiple requests update the same data at the same time.

### Example problem

Two users update the same wallet balance:

- Current balance is 100.
- Request A adds 50.
- Request B subtracts 20.
- Both read balance as 100.
- Final balance may become 150 or 80 instead of 130.

This is called lost update.

### Ways to handle it

1. Use database transactions:
   - Keep related operations atomic.
   - Commit all changes together or rollback all changes.

2. Use proper isolation level:
   - Read committed
   - Repeatable read
   - Serializable

3. Use optimistic locking:
   - Add a `version` column.
   - Update only if version matches.
   - If version changed, retry or return conflict.

Example:

```sql
UPDATE account
SET balance = 130, version = version + 1
WHERE id = 1 AND version = 5;
```

If zero rows are updated, someone else already changed the row.

4. Use pessimistic locking:
   - Lock the row while updating.
   - Example: `SELECT ... FOR UPDATE`
   - Useful when conflicts are frequent.

5. Use atomic database updates:

```sql
UPDATE account
SET balance = balance + 50
WHERE id = 1;
```

This avoids read-modify-write problems.

6. Use distributed locks carefully:
   - Redis lock, Zookeeper, or database lock.
   - Use only when DB-level locking is not enough.
   - Always set lock expiry.

7. Use event sourcing or single-writer pattern:
   - Useful for highly concurrent financial or inventory systems.

### Good interview answer

I would first identify the consistency requirement. For normal cases, I would use transactions and optimistic locking. For high-conflict updates, I would use pessimistic locking or atomic DB updates.

---

## 10. Your application crashes after running for some time. How will you debug memory issues?

If an application crashes after running for some time, it may be due to memory leak, high object creation, improper cache usage, or resource leaks.

### Symptoms

- `OutOfMemoryError`
- Frequent full GC
- Increasing heap usage
- Application becomes slow before crash
- Container restarts
- High memory usage in monitoring

### How I would debug

1. Check memory metrics:
   - Heap usage
   - Non-heap usage
   - Old generation usage
   - GC frequency
   - GC pause time

2. Check logs:
   - `OutOfMemoryError`
   - GC overhead limit exceeded
   - Container killed due to memory limit

3. Enable heap dump on OOM:

```bash
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/path/to/dumps
```

4. Analyze heap dump:
   - Use Eclipse MAT, VisualVM, YourKit, or JProfiler.
   - Find largest objects.
   - Find objects that should have been garbage collected.
   - Check reference chains.

5. Take thread dumps:
   - Check if too many threads are created.
   - Each thread consumes memory.

6. Check caches:
   - Unbounded maps
   - Missing TTL
   - Too many cached objects
   - Cache key explosion

7. Check resource leaks:
   - File streams not closed
   - DB connections not closed
   - HTTP clients not closed
   - Large buffers retained

8. Check code patterns:
   - Static collections growing forever
   - List accumulation in batch jobs
   - Loading huge files into memory
   - Returning huge API responses

### Fixes

- Add cache size limit and TTL.
- Close resources properly.
- Use streaming for large files.
- Use pagination.
- Reduce object retention.
- Tune JVM heap only after fixing leaks.
- Set correct container memory limits.

### Good interview answer

I would monitor heap and GC, collect heap dumps, analyze retained objects, check unbounded caches and static collections, and verify whether the container memory limit is causing the crash.

---

## 11. A scheduled job runs multiple times across instances. How will you control it?

This happens when the same application is deployed on multiple instances and each instance runs the same scheduler.

### Example

If 5 instances are running and each has `@Scheduled`, the same job may run 5 times.

### Ways to control it

1. Use distributed locking:
   - Only one instance gets the lock.
   - Others skip the execution.

2. Use database lock:
   - Store lock entry in DB.
   - Use expiry time to avoid permanent lock.

3. Use ShedLock in Spring:
   - Common solution for Spring Boot scheduled jobs.
   - Supports JDBC, MongoDB, Redis, etc.

4. Use leader election:
   - Only leader instance runs scheduled jobs.
   - Common in Kubernetes or distributed systems.

5. Move job to external scheduler:
   - Kubernetes CronJob
   - Quartz cluster mode
   - Airflow
   - Jenkins
   - Cloud scheduler

6. Make the job idempotent:
   - Even if it runs twice, it should not corrupt data.
   - Use unique constraints and processed flags.

### Important lock properties

- Lock should have expiry time.
- Lock should be released after job completes.
- Job should be idempotent.
- System should handle instance crash while holding lock.

### Good interview answer

I would use a distributed lock or external scheduler so only one instance runs the job. I would also make the job idempotent because duplicate execution can still happen during failures.

---

## 12. Your API gets high latency during peak hours. How will you optimize it?

Peak-hour latency usually means the system cannot handle high traffic efficiently.

### How I would analyze

1. Check traffic pattern:
   - Which APIs are slow?
   - Is the issue read-heavy or write-heavy?
   - Is latency high for all users or specific flows?

2. Check bottlenecks:
   - CPU saturation
   - Memory pressure
   - GC pauses
   - Thread pool exhaustion
   - DB connection pool exhaustion
   - Slow queries
   - Downstream latency
   - Cache misses

3. Check latency percentiles:
   - P50 shows typical user experience.
   - P95 and P99 show tail latency.
   - Tail latency is very important during peak traffic.

### Optimization options

1. Caching:
   - Cache frequently read data.
   - Use Redis, CDN, or local cache.
   - Set TTL and invalidation strategy.

2. Database optimization:
   - Add indexes.
   - Optimize queries.
   - Use pagination.
   - Avoid N+1 queries.
   - Use read replicas.

3. Reduce payload size:
   - Return only required fields.
   - Compress responses.
   - Avoid huge JSON responses.

4. Async processing:
   - Move heavy non-critical work to queue.
   - Example: email sending, report generation, audit logs.

5. Scale horizontally:
   - Add more instances if service is stateless.
   - Use autoscaling.

6. Rate limiting:
   - Protect system from overload.
   - Give priority to important traffic.

7. Connection and thread tuning:
   - Tune web server thread pool.
   - Tune DB connection pool.
   - Tune HTTP client pool.

8. Improve downstream calls:
   - Parallelize independent calls.
   - Add timeouts.
   - Use circuit breakers.
   - Cache downstream responses when safe.

### Good interview answer

I would identify the bottleneck using metrics and tracing, then optimize the slowest part. Typical fixes include caching, DB query optimization, async processing, payload reduction, autoscaling, and rate limiting.

---

## 13. Inter-service communication fails intermittently. How will you ensure reliability?

Intermittent failures are common in distributed systems. The system must be designed assuming network calls can fail.

### Possible causes

- Network timeout
- DNS issue
- Load balancer issue
- Service restart
- Deployment in progress
- Connection pool exhaustion
- TLS certificate issue
- Downstream overload
- Bad retry configuration

### Reliability techniques

1. Timeouts:
   - Set connection timeout.
   - Set read timeout.
   - Avoid infinite waiting.

2. Retries:
   - Retry only safe operations.
   - Use retry limit.
   - Use exponential backoff.
   - Add jitter.

3. Circuit breaker:
   - Stop calling unhealthy service temporarily.
   - Fail fast.
   - Recover gradually.

4. Bulkhead:
   - Use separate thread pools or connection pools.
   - Isolate failure impact.

5. Idempotency:
   - Required when retrying write operations.
   - Prevent duplicate processing.

6. Service discovery and health checks:
   - Route only to healthy instances.
   - Remove unhealthy instances from load balancer.

7. Observability:
   - Logs with correlation ID
   - Metrics
   - Distributed tracing
   - Dependency dashboards

8. Async communication where suitable:
   - Use queues for non-immediate work.
   - This improves resilience when downstream is temporarily unavailable.

### Good interview answer

I would make communication reliable using timeouts, limited retries with backoff and jitter, circuit breakers, health checks, idempotency, and observability. For non-critical synchronous calls, I would consider asynchronous messaging.

---

## 14. A retry mechanism creates more load. How will you control it?

Retries can make failures worse. If a downstream service is already overloaded, aggressive retries increase traffic and can delay recovery.

### What can go wrong

- Retry storm
- Duplicate writes
- Thread pool exhaustion
- More DB pressure
- Increased latency
- Cascading failure

### How I would control retries

1. Limit retry count:
   - Do not retry forever.
   - Example: maximum 2 or 3 retries.

2. Use exponential backoff:
   - Wait longer between retries.
   - Example: 100 ms, 200 ms, 400 ms, 800 ms.

3. Add jitter:
   - Randomize retry delay.
   - Prevent all clients from retrying at the same time.

4. Retry only retryable errors:
   - Retry timeouts, 429, 503, temporary network errors.
   - Do not retry 400, validation errors, or authentication errors.

5. Use circuit breaker:
   - Stop retries when downstream is clearly failing.

6. Use retry budget:
   - Limit total retry traffic as a percentage of original traffic.

7. Add rate limiting:
   - Protect downstream system.
   - Protect your own service.

8. Use idempotency:
   - Required for retrying write operations.
   - Prevent duplicate side effects.

9. Move to queue when possible:
   - For background work, retry through message queue with delay.
   - Use dead-letter queue after max attempts.

### Good interview answer

I would control retries using retry limits, exponential backoff, jitter, circuit breakers, retry budgets, and idempotency. Retrying should be selective, not automatic for every failure.

---

## 15. Your system becomes unstable under traffic spike. How will you stabilize it?

During a traffic spike, the first goal is to protect the system from total failure. Optimization can come later.

### Immediate actions

1. Enable rate limiting:
   - Limit requests per user, IP, API key, or tenant.
   - Protect critical resources.

2. Add load shedding:
   - Reject low-priority requests.
   - Keep critical APIs running.

3. Use autoscaling:
   - Add more application instances.
   - Make sure DB and downstream services can handle the extra load.

4. Enable caching:
   - Cache popular read responses.
   - Use CDN for static content.
   - Use Redis for frequently accessed data.

5. Degrade gracefully:
   - Disable non-critical features.
   - Return partial response.
   - Delay background processing.

6. Apply backpressure:
   - Do not accept unlimited work.
   - Use bounded queues.
   - Slow producers when consumers are overloaded.

7. Protect dependencies:
   - Add circuit breakers.
   - Limit concurrent calls.
   - Use bulkheads.

8. Move work async:
   - Queue non-urgent tasks.
   - Process later when traffic reduces.

### Long-term fixes

- Capacity planning
- Load testing
- Autoscaling policies
- Better caching strategy
- DB optimization
- Queue-based architecture
- CDN usage
- Better monitoring and alerts
- Chaos testing
- Runbooks for incidents

### Good interview answer

I would stabilize the system using rate limiting, load shedding, caching, autoscaling, backpressure, circuit breakers, and graceful degradation. After the incident, I would do root cause analysis and improve capacity planning and load testing.

---

# Quick Revision Summary

| Scenario | Main Concepts |
|---|---|
| API slow under load | Metrics, tracing, DB, GC, thread pool, load testing |
| Downstream service failing | Timeout, circuit breaker, fallback, bulkhead |
| Duplicate requests | Idempotency key, unique constraint, deduplication |
| Works locally but fails in prod | Config, network, secrets, DB, resource limits |
| Thread pool exhausted | Thread dump, blocking calls, timeouts, pool tuning |
| Logs not enough | Structured logs, correlation ID, tracing, metrics |
| Deployment breaks communication | API versioning, backward compatibility, contract tests |
| DB connections exhausted | Leaks, slow queries, long transactions, pool sizing |
| Concurrent updates | Transactions, optimistic locking, pessimistic locking |
| Memory issues | Heap dump, GC logs, cache limits, leak analysis |
| Scheduled job runs multiple times | Distributed lock, ShedLock, external scheduler |
| Peak latency | Caching, DB optimization, async processing, scaling |
| Inter-service failures | Retry, timeout, circuit breaker, health checks |
| Retry creates load | Backoff, jitter, retry budget, circuit breaker |
| Traffic spike instability | Rate limiting, load shedding, backpressure, autoscaling |


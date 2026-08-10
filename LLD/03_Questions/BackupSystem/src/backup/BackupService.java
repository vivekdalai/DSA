package backup;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BackupService {
    private final BackupContext context;
    private final BackupRepository repository;
    private final Map<BackupType, BackupStrategy> strategies;

    public BackupService(BackupContext context, BackupRepository repository, java.util.List<BackupStrategy> strategies) {
        this.context = context;
        this.repository = repository;
        this.strategies = strategies.stream().collect(Collectors.toMap(BackupStrategy::type, Function.identity()));
    }

    public BackupJob run(BackupType type) {
        BackupStrategy strategy = strategies.get(type);
        if (strategy == null) throw new IllegalArgumentException("Unsupported type: " + type);
        Instant startedAt = Instant.now();
        BackupArtifact artifact = strategy.create(context);
        long checkpoint = type == BackupType.LOG
                ? context.logEntries().stream().mapToLong(BackupContext.LogEntry::lsn).max().orElse(0L) : 0L;
        BackupJob job = new BackupJob(UUID.randomUUID().toString(), type, startedAt, Instant.now(), artifact, checkpoint);
        repository.save(job);
        return job;
    }
}


package backup;

import java.time.Instant;

public record BackupJob(String jobId, BackupType type, Instant startedAt, Instant completedAt,
                        BackupArtifact artifact, long checkpoint) {}


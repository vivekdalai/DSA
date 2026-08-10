package backup;

import java.time.Instant;

public class DifferentialBackupStrategy implements BackupStrategy {
    @Override public BackupType type() { return BackupType.DIFFERENTIAL; }
    @Override public BackupArtifact create(BackupContext context) {
        Instant baseline = context.repository().latestSuccessful(BackupType.FULL)
                .map(BackupJob::completedAt)
                .orElseThrow(() -> new IllegalStateException("Differential backup requires a successful full backup"));
        return context.storage().store(type(), context.files().stream()
                .filter(file -> file.modifiedAt().isAfter(baseline))
                .map(BackupContext.VersionedFile::path).toList());
    }
}


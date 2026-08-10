package backup;

public class LogBackupStrategy implements BackupStrategy {
    @Override public BackupType type() { return BackupType.LOG; }
    @Override public BackupArtifact create(BackupContext context) {
        long checkpoint = context.repository().latestSuccessful(BackupType.LOG)
                .map(BackupJob::checkpoint).orElse(0L);
        return context.storage().store(type(), context.logEntries().stream()
                .filter(log -> log.lsn() > checkpoint)
                .map(log -> log.lsn() + ":" + log.value()).toList());
    }
}


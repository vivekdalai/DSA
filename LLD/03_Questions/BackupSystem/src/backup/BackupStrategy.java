package backup;

public interface BackupStrategy {
    BackupType type();
    BackupArtifact create(BackupContext context);
}


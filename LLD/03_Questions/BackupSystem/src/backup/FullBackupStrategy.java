package backup;

public class FullBackupStrategy implements BackupStrategy {
    @Override public BackupType type() { return BackupType.FULL; }
    @Override public BackupArtifact create(BackupContext context) {
        return context.storage().store(type(), context.files().stream().map(BackupContext.VersionedFile::path).toList());
    }
}


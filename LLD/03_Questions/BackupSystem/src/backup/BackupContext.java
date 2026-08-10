package backup;

import java.time.Instant;
import java.util.List;

public record BackupContext(List<VersionedFile> files, List<LogEntry> logEntries,
                            BackupRepository repository, BackupStorage storage) {
    public record VersionedFile(String path, Instant modifiedAt) {}
    public record LogEntry(long lsn, String value) {}
}


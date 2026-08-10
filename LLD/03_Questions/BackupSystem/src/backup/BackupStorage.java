package backup;

import java.util.List;

public interface BackupStorage {
    BackupArtifact store(BackupType type, List<String> entries);
}


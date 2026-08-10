package backup;

import java.util.List;
import java.util.Optional;

public interface BackupRepository {
    void save(BackupJob job);
    Optional<BackupJob> latestSuccessful(BackupType type);
    List<BackupJob> all();
}


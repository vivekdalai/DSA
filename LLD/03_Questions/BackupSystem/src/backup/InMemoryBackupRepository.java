package backup;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class InMemoryBackupRepository implements BackupRepository {
    private final List<BackupJob> jobs = new ArrayList<>();

    @Override public void save(BackupJob job) { jobs.add(job); }

    @Override public Optional<BackupJob> latestSuccessful(BackupType type) {
        return jobs.stream().filter(job -> job.type() == type)
                .max(Comparator.comparing(BackupJob::completedAt));
    }

    @Override public List<BackupJob> all() { return List.copyOf(jobs); }
}


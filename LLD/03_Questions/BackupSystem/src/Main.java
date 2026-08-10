import backup.*;
import java.time.Instant;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        BackupRepository repository = new InMemoryBackupRepository();
        BackupStorage storage = new InMemoryBackupStorage();
        Instant now = Instant.now();
        BackupContext context = new BackupContext(
                List.of(new BackupContext.VersionedFile("users.db", now.minusSeconds(3600)),
                        new BackupContext.VersionedFile("orders.db", now.plusSeconds(3600))),
                List.of(new BackupContext.LogEntry(1, "INSERT user"), new BackupContext.LogEntry(2, "UPDATE order")),
                repository, storage);
        BackupService service = new BackupService(context, repository,
                List.of(new FullBackupStrategy(), new DifferentialBackupStrategy(), new LogBackupStrategy()));
        service.run(BackupType.FULL);
        service.run(BackupType.DIFFERENTIAL);
        service.run(BackupType.LOG);
    }
}


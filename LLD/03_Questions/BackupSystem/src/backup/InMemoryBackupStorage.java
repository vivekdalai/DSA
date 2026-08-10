package backup;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class InMemoryBackupStorage implements BackupStorage {
    @Override public BackupArtifact store(BackupType type, List<String> entries) {
        BackupArtifact artifact = new BackupArtifact(UUID.randomUUID().toString(), type, Instant.now(), List.copyOf(entries));
        System.out.println("Stored " + type + " artifact " + artifact.artifactId() + ": " + artifact.entries());
        return artifact;
    }
}


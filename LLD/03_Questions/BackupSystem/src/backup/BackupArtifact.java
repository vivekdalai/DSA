package backup;

import java.time.Instant;
import java.util.List;

public record BackupArtifact(String artifactId, BackupType type, Instant createdAt, List<String> entries) {}


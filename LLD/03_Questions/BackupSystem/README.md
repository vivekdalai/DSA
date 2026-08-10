# Design Backup System

Uses the **Strategy pattern** so a backup job delegates the changed-data selection to
\`FullBackupStrategy\`, \`DifferentialBackupStrategy\`, or \`LogBackupStrategy\`.

- Full: copies every source file.
- Differential: copies files changed since the latest successful full backup.
- Log: copies database log records newer than the last backed-up log sequence number.

\`\`\`mermaid
classDiagram
    BackupService --> BackupStrategy
    BackupStrategy <|.. FullBackupStrategy
    BackupStrategy <|.. DifferentialBackupStrategy
    BackupStrategy <|.. LogBackupStrategy
    BackupService --> BackupRepository
    BackupService --> BackupStorage
    BackupJob --> BackupArtifact
\`\`\`

The repository is in-memory for the LLD demo. In production, make job creation and
checkpoint update transactional: only advance a log checkpoint after the archive is
durably written.

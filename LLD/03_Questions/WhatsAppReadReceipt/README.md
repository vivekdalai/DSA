# WhatsApp Read Receipt Flow

This LLD demo uses **Observer** for sender UI subscribers and stores a receipt state
per message-recipient pair. States are monotonic: \`SENT → DELIVERED → READ\`; duplicate
or older events are idempotently ignored.

\`\`\`mermaid
sequenceDiagram
    participant R as Recipient device
    participant S as MessageService
    participant C as Conversation
    participant O as SenderChatScreen
    R->>S: markRead(messageId, recipientId)
    S->>C: advance receipt
    C-->>O: onReceiptUpdated(messageId, recipientId, READ)
\`\`\`

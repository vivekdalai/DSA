import java.time.Instant;
import java.util.*;

public class Main {
    enum ReceiptStatus { SENT, DELIVERED, READ }
    record ReadReceipt(String messageId, String recipientId, ReceiptStatus status, Instant updatedAt) {}
    interface ReceiptObserver { void onReceiptUpdated(ReadReceipt receipt); }

    static final class Message {
        private final String id;
        private final Map<String, ReceiptStatus> statusByRecipient = new HashMap<>();

        Message(String id, Set<String> recipients) {
            this.id = id;
            recipients.forEach(recipient -> statusByRecipient.put(recipient, ReceiptStatus.SENT));
        }

        String id() { return id; }

        boolean advanceStatus(String recipientId, ReceiptStatus next) {
            ReceiptStatus current = statusByRecipient.get(recipientId);
            if (current == null) throw new IllegalArgumentException("Recipient is not part of this message");
            if (next.ordinal() <= current.ordinal()) return false; // idempotent and monotonic
            statusByRecipient.put(recipientId, next);
            return true;
        }
    }

    static final class Conversation {
        private final Set<String> participants;
        private final Map<String, Message> messages = new HashMap<>();
        private final List<ReceiptObserver> observers = new ArrayList<>();

        Conversation(Set<String> participants) { this.participants = Set.copyOf(participants); }
        void addObserver(ReceiptObserver observer) { observers.add(observer); }
        void addMessage(Message message) { messages.put(message.id(), message); }

        void updateReceipt(String messageId, String recipientId, ReceiptStatus status) {
            if (!participants.contains(recipientId)) throw new IllegalArgumentException("Not a participant");
            Message message = Optional.ofNullable(messages.get(messageId))
                    .orElseThrow(() -> new IllegalArgumentException("Unknown message"));
            if (message.advanceStatus(recipientId, status)) {
                ReadReceipt receipt = new ReadReceipt(messageId, recipientId, status, Instant.now());
                observers.forEach(observer -> observer.onReceiptUpdated(receipt));
            }
        }
    }

    static final class MessageService {
        private final Conversation conversation;
        MessageService(Conversation conversation) { this.conversation = conversation; }
        void markDelivered(String messageId, String recipientId) {
            conversation.updateReceipt(messageId, recipientId, ReceiptStatus.DELIVERED);
        }
        void markRead(String messageId, String recipientId) {
            conversation.updateReceipt(messageId, recipientId, ReceiptStatus.READ);
        }
    }

    public static void main(String[] args) {
        Conversation conversation = new Conversation(Set.of("alice", "bob", "cara"));
        conversation.addObserver(receipt -> System.out.printf(
                "Alice UI: message %s is %s for %s at %s%n",
                receipt.messageId(), receipt.status(), receipt.recipientId(), receipt.updatedAt()));
        conversation.addMessage(new Message("m-1", Set.of("bob", "cara")));

        MessageService service = new MessageService(conversation);
        service.markDelivered("m-1", "bob");
        service.markRead("m-1", "bob");
        service.markRead("m-1", "bob"); // duplicate: ignored
        service.markRead("m-1", "cara"); // read advances SENT directly to READ
    }
}


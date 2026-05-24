package NotificationCreation;

public class SignatureDecorator extends NotificationDecorator{
    String sender;
    public SignatureDecorator(Notification notification, String sender) {
        super(notification);
        this.sender = sender;
    }

    @Override
    public String getNotification() {
        return super.getNotification() + ", -by " + sender;
    }
}

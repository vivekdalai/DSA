package NotificationCreation;

/**
 * Goal: Simple Content (Message : must)
 * - Optional: TimeStamp
 * - Optional: Signature of Sender
 * - Optional: Footer/header
 *
 * First we create a base decorator class - abstract class
 */
public abstract class NotificationDecorator implements Notification {
    protected final Notification basicNotification;

    public NotificationDecorator(Notification notification){
        this.basicNotification = notification;
    }

    @Override
    public String getNotification() {
        return basicNotification.getNotification();
    }

}

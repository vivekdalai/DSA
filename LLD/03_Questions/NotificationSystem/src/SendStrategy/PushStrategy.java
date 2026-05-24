package SendStrategy;

public class PushStrategy implements SendNotificationStrategy{
    @Override
    public String send(String message) {
        return "Popup Notification sent, message: " + message;
    }
}

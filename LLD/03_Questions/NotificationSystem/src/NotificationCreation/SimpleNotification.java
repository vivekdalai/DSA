package NotificationCreation;

public class SimpleNotification implements Notification {
    private final String content;

    public SimpleNotification(String message){
        this.content = message;
    }
    @Override
    public String getNotification(){
        return "Status Update: " + this.content;
    }
}

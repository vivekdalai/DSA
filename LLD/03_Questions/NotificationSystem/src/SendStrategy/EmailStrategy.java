package SendStrategy;

public class EmailStrategy implements SendNotificationStrategy {
    String emailId;

    public EmailStrategy(String emailId){
        this.emailId = emailId;
    }
    @Override
    public String send(String message) {
        return "Notification sent via Email to " + this.emailId + ", message: "+ message;
    }
}

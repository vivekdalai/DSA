package SendStrategy;

public class SmsStrategy implements SendNotificationStrategy{
    String number;

    public SmsStrategy(String number){
        this.number = number;
    }
    @Override
    public String send(String message) {
        return "Notification sent via SMS to " + this.number + ",message :" + message;
    }
}

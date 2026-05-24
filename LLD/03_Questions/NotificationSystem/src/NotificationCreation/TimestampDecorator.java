package NotificationCreation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimestampDecorator extends NotificationDecorator{

    LocalDateTime now;
    public TimestampDecorator(Notification notification) throws InterruptedException {
        super(notification);
        Thread.sleep(3000);
        now = LocalDateTime.now();
    }

//    DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("EEE dd-MM-yyyy HH:mm:ss a");

    @Override
    public String getNotification(){
        return super.getNotification() + ", TimeStamp: " + now.format(dtf2);
    }
}

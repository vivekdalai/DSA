import NotificationCreation.*;
import ObserverComponents.LoggerObserver;
import ObserverComponents.NotificationEngine;
import ObserverComponents.NotificationObservable;
import SendStrategy.EmailStrategy;
import SendStrategy.PushStrategy;
import SendStrategy.SmsStrategy;
import Service.NotificationService;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("\n");
//        testNotificationCreation();
        testNotificationService();

    }

    private static void testNotificationService() throws InterruptedException {
        System.out.println("\nTest - Service.NotificationService\n");

        NotificationService notificationService = NotificationService.getInstance();
//        NotificationObservable notificationObservable = notificationService.getObservable(); // create an implicit observable
        LoggerObserver loggerObserver = new LoggerObserver();

        NotificationEngine notificationEngine = new NotificationEngine();

        //attach observers --> added in constructor
//        notificationObservable.addObserver(loggerObserver);
//        notificationObservable.addObserver(notificationEngine);

        notificationEngine.addNotificationStrategy(new EmailStrategy("abc@xyz.com"));
        notificationEngine.addNotificationStrategy(new SmsStrategy("123456789"));
        notificationEngine.addNotificationStrategy(new PushStrategy());

        Notification simpleNotification = new SimpleNotification("Welcome User!");
        TimestampDecorator timestampNotification = new TimestampDecorator(simpleNotification);
        SignatureDecorator signatureNotification = new SignatureDecorator(timestampNotification, "ADMIN");

        notificationService.sendNotification(signatureNotification);

        System.out.println("\n\n second notification");
        notificationService.getObservable().removeObserver(notificationEngine);
        Notification taskComplete = new TimestampDecorator(new SimpleNotification("Task Completed Successfully!"));
        notificationService.sendNotification(taskComplete);




    }

    private static void testNotificationCreation() throws InterruptedException {
        System.out.println("\nTest - NotificationCreation\n");
        Notification notification = new SimpleNotification("Order Shipped");
        System.out.println(notification.getNotification());

        NotificationDecorator notificationDecorator = new TimestampDecorator(new SimpleNotification("Out for Delivery"));
        System.out.println(notificationDecorator.getNotification());

        NotificationDecorator notificationDecorator2 = new SignatureDecorator(
                new TimestampDecorator(
                        new SimpleNotification("Order Delivered!")), "Vivek");
        System.out.println(notificationDecorator2.getNotification());



        Notification n1 = new SimpleNotification("New Order Created");
        System.out.println(n1.getNotification());
        Notification n2 = new TimestampDecorator(n1);
        System.out.println(n2.getNotification());
        Notification n3 = new SignatureDecorator(n2, "Vivek");
        System.out.println(n3.getNotification());
        /**
         * SignatureDecorator.getNotification()
         *   -> super.getNotification()
         *   -> NotificationDecorator.getNotification()
         *   -> basicNotification.getNotification()  // calls TimestampDecorator
         *       -> TimestampDecorator.getNotification()
         *           -> super.getNotification()
         *           -> NotificationDecorator.getNotification()
         *           -> basicNotification.getNotification()  // calls SimpleNotification
         */
    }
}
package Service;

import NotificationCreation.Notification;
import ObserverComponents.NotificationObservable;

import java.util.ArrayList;
import java.util.List;

public class NotificationService {
    private static NotificationService instance;
    private static NotificationObservable observable;
    List<Notification> notificationsList;

    public NotificationService(){
        observable = new NotificationObservable();
        notificationsList = new ArrayList<>();
    }

    public static synchronized NotificationService getInstance(){
        if(instance == null)
            instance = new NotificationService();

        return instance;
    }

    public NotificationObservable getObservable(){
        return observable;
    }

    public void sendNotification(Notification notification){
        notificationsList.add(notification);
        observable.setNotification(notification);
    }

}

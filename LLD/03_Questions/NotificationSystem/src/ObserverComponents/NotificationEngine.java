package ObserverComponents;

import SendStrategy.SendNotificationStrategy;
import Service.NotificationService;

import java.util.ArrayList;
import java.util.List;

public class NotificationEngine implements Observer{
    NotificationObservable notificationObservable;
    List<SendNotificationStrategy> notificationStrategies;


    public NotificationEngine(){
        notificationStrategies = new ArrayList<>();
        notificationObservable = NotificationService.getInstance().getObservable();
        notificationObservable.addObserver(this);
    }

    public NotificationEngine(NotificationObservable observable){
        this.notificationObservable = observable;
        notificationStrategies = new ArrayList<>();
    }

    public void addNotificationStrategy(SendNotificationStrategy strategy){
        this.notificationStrategies.add(strategy);
    }

    @Override
    public void update(){
        String notificationContent = notificationObservable.getNotification();
        notificationStrategies.forEach(e -> System.out.println(e.send(notificationContent)));
    }
}

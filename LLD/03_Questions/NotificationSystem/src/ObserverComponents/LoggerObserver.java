package ObserverComponents;

import Service.NotificationService;

public class LoggerObserver implements Observer{

    NotificationObservable notificationObservable;

    public LoggerObserver(){
        notificationObservable = NotificationService.getInstance().getObservable();
        notificationObservable.addObserver(this);
    }

    public LoggerObserver(NotificationObservable observable){
        this.notificationObservable = observable;
    }

    @Override
    public void update() {
        System.out.println("Logger Observer: " + notificationObservable.getNotification());
    }
}

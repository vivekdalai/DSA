package ObserverComponents;

import NotificationCreation.Notification;

import java.util.ArrayList;
import java.util.List;

/**
 * Think of it like - observable is some product whose status is to be provided to observers.
 */
public class NotificationObservable implements Observable{

    private List<Observer> observerList;
    private Notification notification;
    public NotificationObservable(){
        observerList = new ArrayList<>();
    }

    @Override
    public void addObserver(Observer observer) {
        observerList.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observerList = observerList.stream().
                        filter((e) -> !e.equals(observer))
                        .toList();
    }

    @Override
    public void notifyAllObservers() {
        observerList.forEach(Observer::update);
    }


    public void setNotification(Notification notification){
        this.notification = notification;
        notifyAllObservers();
    }

    public String getNotification(){
        if(this.notification == null){
            return "No notification set";
        } else {
            return notification.getNotification();
        }
    }
}

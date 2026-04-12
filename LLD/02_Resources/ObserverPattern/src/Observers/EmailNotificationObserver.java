package Observers;

import Observable.StockObservable;

public class EmailNotificationObserver implements NotificationObserver{

    StockObservable stockObservable;
    String email;
    public EmailNotificationObserver(String email, StockObservable observable){
        this.email = email;
        stockObservable = observable;
        stockObservable.add(this);
    }
    @Override
    public void update(String message) {
        System.out.println("EmailNotification sent to: "+ this.email + ", message: " + message);
    }
}

package Observers;

import Observable.StockObservable;

public class MobileNotificationObserver implements NotificationObserver {
    StockObservable stockObservable;
    int number;
    public MobileNotificationObserver(int number, StockObservable observable){
        this.number = number;
        this.stockObservable = observable;
        observable.add(this);
    }
    @Override
    public void update(String message) {
        System.out.println("MobileNotification sent on: " +
                this.number + ", message: "+ message);
    }
}

package Observers;

import Observable.StockObservable;

public interface NotificationObserver {
    public void update(String message);
}

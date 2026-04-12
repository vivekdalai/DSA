package Observable;

import Observers.NotificationObserver;

import java.util.List;

/**
 * Interfaces only should contain method signature
 * do not initialize variables here ->
 * variables are implicit -> public static final
 */
public interface StockObservable {
    void add(NotificationObserver observer);
    void remove(NotificationObserver observer);

    void notifyAllObservers(String message);

    void setStockCount(int count);

    int getStockCount();

}

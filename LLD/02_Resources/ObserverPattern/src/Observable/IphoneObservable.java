package Observable;

import Observers.NotificationObserver;
import java.util.*;

public class IphoneObservable implements StockObservable{

    List<NotificationObserver> notificationObservers = new ArrayList<>();
    int stockCount = 0;

    @Override
    public void add(NotificationObserver observer){
        //Method to add Observers
        notificationObservers.add(observer);
    }

    @Override
    public void remove(NotificationObserver observer){
        // remove observer
        notificationObservers.remove(observer);
    }

    @Override
    public void notifyAllObservers(String message){
        //notify ALL observers
        for(NotificationObserver observer : notificationObservers)
            observer.update(message);
    }

    @Override
    public void setStockCount(int count){
        //set value of observable object
        if(this.stockCount == 0){
            notifyAllObservers("New iPhone stock updated. Get one NOW!!! ");
        }
        this.stockCount = count;
    }

    @Override
    public int getStockCount() {
        return this.stockCount;
    }
}

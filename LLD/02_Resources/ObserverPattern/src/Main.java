import Observable.IphoneObservable;
import Observable.StockObservable;
import Observers.EmailNotificationObserver;
import Observers.MobileNotificationObserver;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        System.out.println("Learning Observer Pattern");
        StockObservable iPhoneObservable = new IphoneObservable();
        MobileNotificationObserver mobileNotificationObserver = new MobileNotificationObserver(123456789, iPhoneObservable);
        MobileNotificationObserver mobileNotificationObserver2 = new MobileNotificationObserver(987654321, iPhoneObservable);
        EmailNotificationObserver emailNotificationObserver = new EmailNotificationObserver("vivek@gmail.com", iPhoneObservable);
        EmailNotificationObserver emailNotificationObserver2 = new EmailNotificationObserver("dalai@gmail.com",iPhoneObservable);

        System.out.println("current stock: " + iPhoneObservable.getStockCount());
        System.out.println("Updating stock to 100");
        iPhoneObservable.setStockCount(100);

        iPhoneObservable.remove(emailNotificationObserver2);

        iPhoneObservable.setStockCount(0);
        System.out.println("New stock added");
        iPhoneObservable.setStockCount(10);
    }
}
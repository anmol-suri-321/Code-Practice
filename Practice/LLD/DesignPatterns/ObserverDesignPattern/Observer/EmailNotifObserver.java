package Practice.LLD.DesignPatterns.ObserverDesignPattern.Observer;

import Practice.LLD.DesignPatterns.ObserverDesignPattern.Observable.StocksObservable;

public class EmailNotifObserver implements NotifObserver{
    String email;
    StocksObservable observable;

    public EmailNotifObserver(String email, StocksObservable ob) {
        this.email = email;
        this.observable = ob;
    }

    @Override
    public void update() {
        int countOfUnits = observable.getStockCount();
        System.out.println("Hurry up Stock is back, " + observable.getType() + " units left: "
                 + countOfUnits + " Check your email: " + email);
    }
    
}

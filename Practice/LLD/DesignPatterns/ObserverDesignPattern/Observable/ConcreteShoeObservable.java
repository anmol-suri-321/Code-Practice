package Practice.LLD.DesignPatterns.ObserverDesignPattern.Observable;

import java.util.ArrayList;
import java.util.List;

import Practice.LLD.DesignPatterns.ObserverDesignPattern.Observer.NotifObserver;

public class ConcreteShoeObservable implements StocksObservable {
    List<NotifObserver> observerList = new ArrayList<>();
    private int shoeCount = 0;

    @Override
    public void addObserver(NotifObserver ob) {
        observerList.add(ob);
    }

    @Override
    public void removeObserver(NotifObserver ob) {
        observerList.add(ob);
    }

    @Override
    public void NotifyObserver() {
        for(NotifObserver ob : observerList) {
            ob.update();
        }
    }

    @Override
    public void setStockCount(int units) {
        if(shoeCount == 0) {
            shoeCount += units;
            NotifyObserver();
        } else {
            shoeCount += units;
        }
        
    }

    @Override
    public int getStockCount() {
        return shoeCount;
    }

    @Override
    public String getType() {
        return "Shoe";
    }
}

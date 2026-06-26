package Practice.LLD.DesignPatterns.ObserverDesignPattern.Observable;

import java.util.ArrayList;
import java.util.List;

import Practice.LLD.DesignPatterns.ObserverDesignPattern.Observer.NotifObserver;

public class ConcreteIphoneObservable implements StocksObservable {
    List<NotifObserver> observerList = new ArrayList<>();
    private int iphoneCount = 0;

    @Override
    public void addObserver(NotifObserver ob) {
        observerList.add(ob);
    }

    @Override
    public void removeObserver(NotifObserver ob) {
        observerList.remove(ob);
    }

    @Override
    public void NotifyObserver() {
        for(NotifObserver ob : observerList) {
            ob.update();
        }
    }

    @Override
    public void setStockCount(int units) {
        if(iphoneCount == 0) {
            iphoneCount += units;
            NotifyObserver();
        } else {
            iphoneCount += units;
        }
    }

    @Override
    public int getStockCount() {
        return iphoneCount;
    }

    @Override
    public String getType() {
        return "Iphone";
    }
    
}

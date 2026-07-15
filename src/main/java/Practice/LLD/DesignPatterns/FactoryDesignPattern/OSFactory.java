package Practice.LLD.DesignPatterns.FactoryDesignPattern;

public class OSFactory {

    public OS getInstance(String msg) {
        if(msg.equals("open")) {
            return new Android();
        } else if(msg.equals("closed")) {
            return new IOS();
        } else {
            return new Windows();
        }
    }
}

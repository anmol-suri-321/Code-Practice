package Practice.LLD.DesignPatterns.DecoratorDesignPattern;

public class BasePizza implements Pizza {
    @Override
    public double cost() {
        return 199.0;
    }

    @Override
    public String pizzaType() {
        return "Base Pizza";
    }
}

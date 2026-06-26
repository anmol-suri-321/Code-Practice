package Practice.LLD.DesignPatterns.DecoratorDesignPattern;

public class CheesePizza extends ToppingDecorator {

    public CheesePizza(Pizza pizza) {
        super(pizza);
    }

    @Override
    public double cost() {
        return pizza.cost() + 45.0;
    }

    @Override
    public String pizzaType() {
        return "Cheese Pizza";
    }
}

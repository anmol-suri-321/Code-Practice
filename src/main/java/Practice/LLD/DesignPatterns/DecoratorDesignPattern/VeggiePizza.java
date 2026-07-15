package Practice.LLD.DesignPatterns.DecoratorDesignPattern;

public class VeggiePizza extends ToppingDecorator {

    public VeggiePizza(Pizza pizza) {
        super(pizza);
    }

    @Override
    public double cost() {
        return pizza.cost() + 50.0;
    }

    @Override
    public String pizzaType() {
        return "Veggie Pizza";
    }
}

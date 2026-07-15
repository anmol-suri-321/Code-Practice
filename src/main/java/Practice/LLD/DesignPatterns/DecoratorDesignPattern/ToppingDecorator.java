package Practice.LLD.DesignPatterns.DecoratorDesignPattern;

public abstract class ToppingDecorator implements Pizza {
    Pizza pizza;

    public ToppingDecorator(Pizza pizza) {
        this.pizza = pizza;
    }
}

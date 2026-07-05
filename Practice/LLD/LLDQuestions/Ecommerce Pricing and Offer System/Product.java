package Flipkart;

public class Product {
    private final String id;
    private final int basePrice;


    public Product(String id, int basePrice) {
        this.id = id;
        this.basePrice = basePrice;
    }

    public String getId() {
        return id;
    }

    public int getBasePrice() {
        return basePrice;
    }
}

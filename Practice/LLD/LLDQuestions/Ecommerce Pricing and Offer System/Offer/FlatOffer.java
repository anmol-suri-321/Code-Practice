package Flipkart.Offer;

import Flipkart.Product;

import java.util.Set;

public class FlatOffer extends Offer {
    private final int flatDiscount;

    public FlatOffer(String id, Set<String> productsIds, int minimumQuantity, int remainingUsage, int flatDiscount) {
        super(id, productsIds, minimumQuantity, remainingUsage);
        this.flatDiscount = flatDiscount;
    }

    @Override
    public int calculateDiscount(Product product, int quantity) {
        // we need to multiply the number of quantity with flat discount offering
        return flatDiscount * quantity;
    }

    public int getFlatDiscount() {
        return flatDiscount;
    }
}

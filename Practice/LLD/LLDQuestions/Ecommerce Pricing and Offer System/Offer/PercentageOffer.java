package Flipkart.Offer;

import Flipkart.Product;

import java.util.Set;

public class PercentageOffer extends Offer {
    private final int percentDiscount;

    public PercentageOffer(String id, Set<String> productsIds, int minimumQuantity, int remainingUsage, int percentDiscount) {
        super(id, productsIds, minimumQuantity, remainingUsage);
        this.percentDiscount = percentDiscount;
    }

    @Override
    public int calculateDiscount(Product product, int quantity) {
        int totalPrice = product.getBasePrice() * quantity;

        return (totalPrice * percentDiscount) / 100;
    }

    public int getPercentDiscount() {
        return percentDiscount;
    }
}

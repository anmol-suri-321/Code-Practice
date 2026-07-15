package Practice.LLD.LLDQuestions.Ecommerce.Offer;

import Practice.LLD.LLDQuestions.Ecommerce.Product;

import java.util.Set;

public abstract class Offer {
    private final String id;
    private final Set<String> productsIds;
    private final int minimumQuantity;
    private int remainingUsage;

    protected Offer(String id, Set<String> productsIds, int minimumQuantity, int remainingUsage) {
        this.id = id;
        this.productsIds = productsIds;
        this.minimumQuantity = minimumQuantity;
        this.remainingUsage = remainingUsage;
    }

    public String getId() {
        return id;
    }

    public Set<String> getProductsIds() {
        return productsIds;
    }

    public int getMinimumQuantity() {
        return minimumQuantity;
    }

    public int getRemainingUsage() {
        return remainingUsage;
    }

    public abstract int calculateDiscount(Product product, int quantity);

    public boolean isOfferApplicable(Product product, int quantity) {
        if(productsIds.contains(product.getId()) && quantity >= minimumQuantity && remainingUsage > 0) {
            return true;
        }

        return false;
    }

    public synchronized boolean applyOfferUsage() {
        if(remainingUsage <= 0) {
            return false;
        }

        remainingUsage--;
        return true;
    }
}

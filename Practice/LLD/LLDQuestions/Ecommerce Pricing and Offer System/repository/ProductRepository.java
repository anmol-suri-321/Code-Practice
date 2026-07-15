package Practice.LLD.LLDQuestions.Ecommerce.repository;

import Practice.LLD.LLDQuestions.Ecommerce.Product;

import java.util.HashMap;
import java.util.Map;

public class ProductRepository {
    private final Map<String, Product> productStore;

    public ProductRepository() {
        this.productStore = new HashMap<>();
    }

    public void save(Product product) {
        if(productStore.containsKey(product.getId())) {
            throw new AlreadyPresentException("Product already present in the DB: " + product.getId());
        }

        productStore.put(product.getId(), product);
    }

    public Product findById(String productId) {
        Product product = productStore.get(productId);

        if(product == null) {
            throw new NotFoundException("Product not found in the DB: " + productId);
        }

        return product;
    }

    public boolean exists(String productId) {
        return productStore.containsKey(productId);
    }
}

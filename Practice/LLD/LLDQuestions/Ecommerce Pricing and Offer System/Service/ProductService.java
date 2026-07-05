package Flipkart.Service;

import Flipkart.Product;
import Flipkart.repository.ProductRepository;

public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void createProduct(String productId, int basePrice) {
        Product product = new Product(productId, basePrice);
        productRepository.save(product);
    }

    public Product getProduct(String productId) {
        return productRepository.findById(productId);
    }
}

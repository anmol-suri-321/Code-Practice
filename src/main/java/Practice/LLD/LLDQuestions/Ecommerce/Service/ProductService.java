package Practice.LLD.LLDQuestions.Ecommerce.Service;

import Practice.LLD.LLDQuestions.Ecommerce.Product;
import Practice.LLD.LLDQuestions.Ecommerce.repository.ProductRepository;

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

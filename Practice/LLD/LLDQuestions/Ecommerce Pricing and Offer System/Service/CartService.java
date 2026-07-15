package Practice.LLD.LLDQuestions.Ecommerce.Service;

import Practice.LLD.LLDQuestions.Ecommerce.Cart;
import Practice.LLD.LLDQuestions.Ecommerce.Product;
import Practice.LLD.LLDQuestions.Ecommerce.repository.CartRepository;
import Practice.LLD.LLDQuestions.Ecommerce.repository.ProductRepository;

public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository,
                       ProductRepository productRepository) {

        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public void createCart(String cartId) {
        cartRepository.save(new Cart(cartId));
    }

    public void addProductToCart(String cartId,
                                 String productId,
                                 int quantity) {

        Cart cart = cartRepository.findById(cartId);

        Product product = productRepository.findById(productId);

        cart.addItemToCart(product, quantity);
    }

    public Cart getCart(String cartId) {
        return cartRepository.findById(cartId);
    }
}

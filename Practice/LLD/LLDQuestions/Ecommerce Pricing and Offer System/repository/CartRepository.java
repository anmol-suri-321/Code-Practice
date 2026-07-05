package Flipkart.repository;

import Flipkart.Cart;

import java.util.HashMap;
import java.util.Map;

public class CartRepository {
    private final Map<String, Cart> cartStore;

    public CartRepository() {
        this.cartStore = new HashMap<>();
    }

    public void save(Cart cart) {
        if (cartStore.containsKey(cart.getId())) {
            throw new AlreadyPresentException("Cart already exists : " + cart.getId());
        }

        cartStore.put(cart.getId(), cart);
    }

    public Cart findById(String cartId) {
        Cart cart = cartStore.get(cartId);
        if (cart == null) {
            throw new NotFoundException("Cart not found : " + cartId);
        }

        return cart;
    }
}

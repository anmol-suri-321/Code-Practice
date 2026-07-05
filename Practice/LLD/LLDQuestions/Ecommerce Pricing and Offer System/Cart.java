package Flipkart;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Cart {
    private final String id;
    private final Map<String, CartItem> items;

    public Cart(String id) {
        this.id = id;
        this.items = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public Collection<CartItem> getItems() {
        return items.values();
    }

    public void addItemToCart(Product product, int quantity) {
        CartItem cartItem = items.get(product.getId());

        if (cartItem != null) {
            cartItem.increaseQuantity(quantity);
        } else {
            items.put(product.getId(), new CartItem(product, quantity));
        }
    }
}

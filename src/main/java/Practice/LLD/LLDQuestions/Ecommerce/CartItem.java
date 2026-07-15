package Practice.LLD.LLDQuestions.Ecommerce;

public class CartItem {
    private final Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.quantity = quantity;
        this.product = product;
    }


    public int getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void increaseQuantity(int addition) {
        this.quantity += addition;
    }
}

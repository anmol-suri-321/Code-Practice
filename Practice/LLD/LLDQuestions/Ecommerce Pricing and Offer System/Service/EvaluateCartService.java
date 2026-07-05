package Flipkart.Service;

import Flipkart.Cart;
import Flipkart.CartItem;
import Flipkart.Offer.Offer;
import Flipkart.SingleOfferStrategy;
import Flipkart.repository.OfferRepository;

import java.util.List;

public class EvaluateCartService {
    private final CartService cartService;
    private final OfferRepository offerRepository;
    private final SingleOfferStrategy bestSingleOfferStrategy;

    public EvaluateCartService(CartService cartService, OfferRepository offerRepository, SingleOfferStrategy bestSingleOfferStrategy) {
        this.cartService = cartService;
        this.offerRepository = offerRepository;
        this.bestSingleOfferStrategy = bestSingleOfferStrategy;
    }

    public void evaluateCart(String cartId) {

        Cart cart = cartService.getCart(cartId);

        int totalBasePrice = 0;
        int totalDiscount = 0;

        for (CartItem cartItem : cart.getItems()) {

            int basePrice = cartItem.getProduct().getBasePrice() * cartItem.getQuantity();

            List<Offer> offers = offerRepository.findOffersByProduct(
                    cartItem.getProduct().getId());

            Offer bestOffer = bestSingleOfferStrategy.selectBestOffer(
                    cartItem,
                    offers);

            int discount = 0;
            String appliedOffer = "NONE";

            if (bestOffer != null && bestOffer.applyOfferUsage()) {
                    discount = bestOffer.calculateDiscount(cartItem.getProduct(), cartItem.getQuantity());
                    appliedOffer = bestOffer.getId();
                }


            totalBasePrice += basePrice;
            totalDiscount += discount;

            System.out.println("Product : " + cartItem.getProduct().getId());
            System.out.println("Quantity : " + cartItem.getQuantity());
            System.out.println("Applied Offer : " + appliedOffer);
            System.out.println("Discount : " + discount);
            System.out.println("Final Price : " + (basePrice - discount));
        }

        System.out.println("Total Price : " + totalBasePrice);
        System.out.println("Total Discount : " + totalDiscount);
        System.out.println("Final Amount : " + (totalBasePrice - totalDiscount));
    }
}

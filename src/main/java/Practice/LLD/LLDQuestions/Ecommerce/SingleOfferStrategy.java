package Practice.LLD.LLDQuestions.Ecommerce;

import Practice.LLD.LLDQuestions.Ecommerce.Offer.Offer;

import java.util.List;

public class SingleOfferStrategy {
    public Offer selectBestOffer(CartItem cartItem, List<Offer> offers) {
        Offer bestOffer = null;
        int maximumDiscount = 0;

        Product product = cartItem.getProduct();
        int quantity = cartItem.getQuantity();

        for (Offer offer : offers) {

            if (offer.isOfferApplicable(product, quantity)) {
                int discount = offer.calculateDiscount(product, quantity);

                if (discount > maximumDiscount) {
                    maximumDiscount = discount;
                    bestOffer = offer;
                }
            }
        }

        return bestOffer;
    }
}

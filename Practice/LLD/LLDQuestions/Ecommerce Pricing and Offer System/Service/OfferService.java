package Flipkart.Service;

import Flipkart.Enum.OfferType;
import Flipkart.Offer.FlatOffer;
import Flipkart.Offer.Offer;
import Flipkart.Offer.PercentageOffer;
import Flipkart.repository.OfferRepository;
import Flipkart.repository.ProductRepository;

import java.util.Set;

public class OfferService {
    private final ProductRepository productRepository;
    private final OfferRepository offerRepository;

    public OfferService(ProductRepository productRepository, OfferRepository offerRepository) {
        this.productRepository = productRepository;
        this.offerRepository = offerRepository;
    }

    public void createOffer(String offerId, OfferType offerType, Set<String> applicableProductIds, int minQuantity,
                            int maxUsages, int discount) {

        validateProducts(applicableProductIds);

        Offer offer = switch (offerType) {

            case FLAT -> new FlatOffer(
                    offerId,
                    applicableProductIds,
                    minQuantity,
                    maxUsages,
                    discount
            );

            case PERCENTAGE -> new PercentageOffer(
                    offerId,
                    applicableProductIds,
                    minQuantity,
                    maxUsages,
                    discount
            );
        };

        offerRepository.save(offer);
    }

    public Offer getOffer(String offerId) {
        return offerRepository.findById(offerId);
    }

    private void validateProducts(Set<String> productIds) {
        for (String productId : productIds) {
            productRepository.findById(productId);
        }
    }
}

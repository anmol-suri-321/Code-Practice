package Practice.LLD.LLDQuestions.Ecommerce.repository;

import Practice.LLD.LLDQuestions.Ecommerce.Offer.Offer;
import Practice.LLD.LLDQuestions.Ecommerce.Product;

import java.util.*;

public class OfferRepository {
    // This stores {offerId, Offer}
    private final Map<String, Offer> offerStore;
    // This stores {productId, Offer List}
    private final Map<String, List<Offer>> productOfferStore;

    public OfferRepository() {
        this.offerStore = new HashMap<>();
        this.productOfferStore = new HashMap<>();
    }

    public void save(Offer offer) {
        if(offerStore.containsKey(offer.getId())) {
            throw new AlreadyPresentException("Offer Already Present in the Offer DB: " + offer.getId());
        }

        offerStore.put(offer.getId(), offer);

        for(String productId : offer.getProductsIds()) {
            productOfferStore.computeIfAbsent(productId, key -> new ArrayList<>()).add(offer);
        }
    }

    public Offer findById(String offerId) {
        return offerStore.get(offerId);
    }

    public List<Offer> findOffersByProduct(String productId) {
        return Collections.unmodifiableList(productOfferStore.getOrDefault(productId, new ArrayList<>()));
    }
}

package Practice.LLD.LLDQuestions.Ecommerce;

import Practice.LLD.LLDQuestions.Ecommerce.Enum.OfferType;
import Practice.LLD.LLDQuestions.Ecommerce.Service.CartService;
import Practice.LLD.LLDQuestions.Ecommerce.Service.EvaluateCartService;
import Practice.LLD.LLDQuestions.Ecommerce.Service.OfferService;
import Practice.LLD.LLDQuestions.Ecommerce.Service.ProductService;
import Practice.LLD.LLDQuestions.Ecommerce.repository.CartRepository;
import Practice.LLD.LLDQuestions.Ecommerce.repository.OfferRepository;
import Practice.LLD.LLDQuestions.Ecommerce.repository.ProductRepository;

import java.util.Set;

public class Main {
    public static void main(String[] args) {
        ProductRepository productRepository = new ProductRepository();
        OfferRepository offerRepository = new OfferRepository();
        CartRepository cartRepository = new CartRepository();

        ProductService productService = new ProductService(productRepository);

        OfferService offerService = new OfferService(productRepository, offerRepository);

        CartService cartService = new CartService(cartRepository, productRepository);

        SingleOfferStrategy singleOfferStrategy = new SingleOfferStrategy();

        EvaluateCartService evaluateCartService = new EvaluateCartService(cartService, offerRepository, singleOfferStrategy);

        productService.createProduct("Shirt", 1000);
        productService.createProduct("Shoes", 2000);
        productService.createProduct("Socks", 500);

        offerService.createOffer("OFFER1", OfferType.PERCENTAGE, Set.of("Shirt", "Socks"), 2, 2, 10);
        offerService.createOffer("OFFER2", OfferType.FLAT, Set.of("Shoes"), 1, 5, 300);
        offerService.createOffer("OFFER3", OfferType.PERCENTAGE, Set.of("Shoes"), 2, 1, 20);

        cartService.createCart("Cart1");

        cartService.addProductToCart("Cart1", "Shirt", 2);
        cartService.addProductToCart("Cart1", "Shoes", 2);

        System.out.println("\nCART 1");
        evaluateCartService.evaluateCart("Cart1");

        cartService.createCart("Cart2");

        cartService.addProductToCart("Cart2", "Shirt", 2);
        cartService.addProductToCart("Cart2", "Shoes", 2);

        System.out.println("\nCART 2");
        evaluateCartService.evaluateCart("Cart2");

        cartService.createCart("Cart3");

        cartService.addProductToCart("Cart3", "Socks", 3);

        System.out.println("\nCART 3");
        evaluateCartService.evaluateCart("Cart3");
    }
}

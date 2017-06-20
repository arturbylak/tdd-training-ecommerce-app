package pl.pragmatists.trainings.ecommerce.addtocart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.pragmatists.trainings.ecommerce.addtocart.json.CartItemJson;
import pl.pragmatists.trainings.ecommerce.addtocart.json.CartJson;
import pl.pragmatists.trainings.ecommerce.cart.Cart;
import pl.pragmatists.trainings.ecommerce.cart.CartItem;
import pl.pragmatists.trainings.ecommerce.product.persistence.Product;
import pl.pragmatists.trainings.ecommerce.product.persistence.ProductRepository;

import java.util.Optional;

@Controller
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @RequestMapping(value = "/user/{userId}/cart/items", method = RequestMethod.POST)
    public ResponseEntity add(@PathVariable Long userId, @RequestBody CartItemJson cartItemJson) {
        Optional<Product> one = productRepository.findOne(cartItemJson.productId);
        return one.map(product -> {
            addItem(userId, cartItemJson, product);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }).orElse(ResponseEntity.badRequest().build());
    }

    private void addItem(Long userId,CartItemJson cartItemJson, Product product) {
        CartItem cartItem = new CartItem(product, cartItemJson.quantity);
        Cart cart = cartRepository.findByUserId(userId).orElse(new Cart(userId));
        cart.add(cartItem);
        cartRepository.save(cart);
    }

    @RequestMapping(value = "/user/{userId}/cart", method = RequestMethod.GET)
    public @ResponseBody CartJson get(@PathVariable Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElse(new Cart(userId));
        CartJson cartJson = new CartJson();
        cartJson.total = cart.total().toString();
        cartJson.shipping = 15;
        return cartJson;
    }

}

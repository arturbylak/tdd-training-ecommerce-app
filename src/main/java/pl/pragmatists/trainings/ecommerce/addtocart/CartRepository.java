package pl.pragmatists.trainings.ecommerce.addtocart;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.repository.Repository;

import pl.pragmatists.trainings.ecommerce.cart.Cart;

public interface CartRepository extends Repository<Cart, Long> {
    Optional<Cart> findByUserId(Long userId);

    Cart save(Cart cart);

    Stream<Cart> findAll();
}

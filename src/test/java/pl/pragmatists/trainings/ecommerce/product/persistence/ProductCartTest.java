package pl.pragmatists.trainings.ecommerce.product.persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.pragmatists.trainings.ecommerce.cart.Cart;
import pl.pragmatists.trainings.ecommerce.cart.CartItem;
import pl.pragmatists.trainings.ecommerce.common.Money;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional
public class ProductCartTest {

    @Autowired
    private TestEntityManager em;

    @Test
    public void saves_two_carts_with_same_product() {
        //given
        Product product = new Product(1L, "firstProductName", new Money(10, 23));
        em.persistAndFlush(product);

        Cart firstCart = new Cart(1L);
        CartItem cartItem = new CartItem(product, 1);
        firstCart.withItems(Collections.singletonList(cartItem));

        Cart secondCart = new Cart(2L);
        CartItem secondCartItem = new CartItem(product, 10);
        secondCart.withItems(Collections.singletonList(secondCartItem));

        //when
        em.persistAndFlush(firstCart);
        em.persistAndFlush(secondCart);
        em.clear();

        //then
        Cart firstFetchedCart = em.find(Cart.class, firstCart.getId());
        Cart secondFetchedCart = em.find(Cart.class, secondCart.getId());
        assertThat(firstFetchedCart.items().get(0)).isEqualTo(cartItem);
        assertThat(secondFetchedCart.items().get(0)).isEqualTo(secondCartItem);
    }

    @Test
    public void saves_two_different_products_in_cart() {
        //given
        Product productA = new Product(1L, "A", new Money(10, 23));
        Product productB = new Product(2L, "B", new Money(11, 0));
        em.persistAndFlush(productA);
        em.persistAndFlush(productB);

        Cart cart = new Cart(1L);
        CartItem cartItemA = new CartItem(productA, 1);
        CartItem cartItemB = new CartItem(productB, 1);
        cart.withItems(Arrays.asList(cartItemA, cartItemB));

        //when
        em.persistAndFlush(cart);
        em.clear();

        //then
        Cart firstFetchedCart = em.find(Cart.class, cart.getId());
        assertThat(firstFetchedCart.items()).hasSize(2);
    }


}

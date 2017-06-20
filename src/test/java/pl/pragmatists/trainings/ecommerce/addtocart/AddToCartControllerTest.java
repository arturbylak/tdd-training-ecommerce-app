package pl.pragmatists.trainings.ecommerce.addtocart;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import pl.pragmatists.trainings.ecommerce.cart.Cart;
import pl.pragmatists.trainings.ecommerce.cart.CartItem;
import pl.pragmatists.trainings.ecommerce.common.Money;
import pl.pragmatists.trainings.ecommerce.product.persistence.Product;
import pl.pragmatists.trainings.ecommerce.product.persistence.ProductRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AutoConfigureTestEntityManager
public class AddToCartControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    public void add_one_product() throws Exception {
        Product product = new Product(1L, "cup", new Money(3, 50));
        em.persistAndFlush(product);

        addItemToCart(1L, 3, 5);

        assertThat(firstCart().userId()).isEqualTo(5L);
        assertThat(firstCart().items()).containsExactly(new CartItem(product, 3));
    }

    @Test
    public void add_two_product() throws Exception {
        //given
        Product productA = new Product(1L, "cupA", new Money(3, 50));
        em.persistAndFlush(productA);

        Product productB = new Product(2L, "cupB", new Money(3, 50));
        em.persistAndFlush(productB);

        //when
        addItemToCart(productA.getId(), 3, 5);
        addItemToCart(productB.getId(), 1, 5);

        //then
        assertThat(firstCart().items()).hasSize(2);
    }

    @Test
    public void updates_existing_cart_item_quantity() throws Exception {
        //given
        Product productA = new Product(1L, "cupA", new Money(3, 50));
        em.persistAndFlush(productA);

        //when
        addItemToCart(productA.getId(), 3, 5);
        addItemToCart(productA.getId(), 2, 5);

        //then
        assertThat(firstCart().items()).hasSize(1);
        assertThat(firstCart().items().get(0).getQuantity()).isEqualTo(5);
    }

    @Test
    public void adding_not_existing_product_returns_bad_request() throws Exception {
        //given

        //when
        ResultActions resultActions = addItemToCart(11L, 3, 5);

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    private ResultActions addItemToCart(long productId, int quantity, int userId) throws Exception {
        return mvc.perform(post("/user/" + userId + "/cart/items")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                        new JSONObject()
                                .put("productId", productId)
                                .put("quantity", quantity)
                                .toString()
                )
        );
    }

    private Cart firstCart() {
        return cartRepository.findAll().iterator().next();
    }
}

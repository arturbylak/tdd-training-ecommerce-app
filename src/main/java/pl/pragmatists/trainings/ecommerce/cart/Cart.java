package pl.pragmatists.trainings.ecommerce.cart;


import pl.pragmatists.trainings.ecommerce.common.Money;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
public class Cart {
    @Id
    @GeneratedValue
    private long id;
    private long userId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cart", fetch = FetchType.EAGER)
    private List<CartItem> items;

    private Cart() {
        items = new ArrayList<>();
    }

    public Cart(Long userId) {
        this();
        this.userId = userId;
    }

    public long userId() {
        return userId;
    }

    public List<CartItem> items() {
        return items;
    }

    public Cart withItems(List<CartItem> items) {
        items.forEach(this::add);
        return this;
    }

    public void add(CartItem cartItem) {
        cartItem.cart = this;
        Optional<CartItem> itemOptional = items.stream().filter(item -> item.getProduct().getId() == cartItem.getProduct().getId()).findFirst();
        if (itemOptional.isPresent()) {
            CartItem item = itemOptional.get();
            item.setQuantity(cartItem.getQuantity() + item.getQuantity());
        } else {
            items.add(cartItem);
        }
    }

    public Money total() {
        return items.stream().map(cartItem -> cartItem.getPrice()).reduce(new Money(0,0), Money::add);
    }

    public long getId() {
        return id;
    }
}

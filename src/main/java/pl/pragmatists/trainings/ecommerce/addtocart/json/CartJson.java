package pl.pragmatists.trainings.ecommerce.addtocart.json;

import java.util.List;

public class CartJson {
    public String total;
    public long shipping;

    public CartJson() {

    }

    public List<CartItemJson> items;
}

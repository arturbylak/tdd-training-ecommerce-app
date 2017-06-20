package pl.pragmatists.trainings.ecommerce.common;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MoneyTest {

    @Test
    public void add_money() {
        assertThat(new Money(3,50).add(new Money(4,20))).isEqualTo(new Money(7,70));
    }

    @Test
    public void multiply_money() {
        assertThat(new Money(3,50).multiply(2)).isEqualTo(new Money(7,0));
    }

}
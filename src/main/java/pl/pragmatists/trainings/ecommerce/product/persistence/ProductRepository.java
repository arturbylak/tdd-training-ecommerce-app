package pl.pragmatists.trainings.ecommerce.product.persistence;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface ProductRepository extends Repository<Product, Long> {
    Optional<Product> findOne(long productId);
}

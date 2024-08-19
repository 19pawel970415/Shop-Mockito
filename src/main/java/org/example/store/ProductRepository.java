package org.example.store;

import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(Long id);
    Product addProduct(Product product);
    void deleteProduct(Long id);
}

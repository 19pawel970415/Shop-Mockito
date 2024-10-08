package org.example.store;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(Long id);
    void addProduct(Product product);
    void deleteProduct(Long id);
    List<Product> findAll();
}

package org.example.store;

import java.util.NoSuchElementException;

public class ProductService {

    private final ProductRepository productRepository;
    private final ProductValidator productValidator;

    public ProductService(ProductRepository productRepository, ProductValidator productValidator) {
        this.productRepository = productRepository;
        this.productValidator = productValidator;
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found"));
    }

    public Product addProduct(Product product) {
        if (productValidator.isProductValid(product)) {
            return productRepository.addProduct(product);
        }
        throw new IllegalArgumentException("Product is invalid");
    }

    public void deleteProduct(Long id) {
        if (productRepository.findById(id).isPresent()) {
            productRepository.deleteProduct(id);
        } else {
            throw new NoSuchElementException("Cannot delete non-existent product");
        }
    }
}

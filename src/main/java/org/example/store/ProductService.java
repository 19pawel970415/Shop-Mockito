package org.example.store;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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

    public void addProduct(Product product) {
        if (productRepository.findById(product.getId()).isEmpty()) {
            if (productValidator.isProductValid(product)) {
                productRepository.addProduct(product);
            } else {
                throw new IllegalArgumentException("Product is invalid");
            }
        } else {
            throw new IllegalArgumentException("Product already in repository");
        }
    }

    public void deleteProduct(Long id) {
        if (productRepository.findById(id).isPresent()) {
            productRepository.deleteProduct(id);
        } else {
            throw new NoSuchElementException("Cannot delete non-existent product");
        }
    }

    public void updateProductPrice(Long id, double newPrice) {
        Optional<Product> existingProductOptional = productRepository.findById(id);

        if (existingProductOptional.isPresent()) {
            Product existingProduct = existingProductOptional.get();

            if (newPrice > 0) {
                existingProduct.setPrice(newPrice);
                productRepository.addProduct(existingProduct);
            } else {
                throw new IllegalArgumentException("Price must be greater than 0");
            }
        } else {
            throw new NoSuchElementException("Cannot update price of non-existent product");
        }
    }


    public List<Product> getAllProducts() {
        // Assuming productRepository has a method to get all products
        return productRepository.findAll();
    }
}

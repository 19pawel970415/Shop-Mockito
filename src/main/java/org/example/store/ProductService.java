package org.example.store;

import java.util.List;
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

    public void addProduct(Product product) {
        if (productValidator.isProductValid(product)) {
            productRepository.addProduct(product);
        } else {
            throw new IllegalArgumentException("Product is invalid");
        }
    }

    public void deleteProduct(Long id) {
        if (productRepository.findById(id).isPresent()) {
            productRepository.deleteProduct(id);
        } else {
            throw new NoSuchElementException("Cannot delete non-existent product");
        }
    }

    public void updateProduct(Long id, Product newProduct) {
        if (productRepository.findById(id).isPresent()) {
            if (productValidator.isProductValid(newProduct)) {
                productRepository.addProduct(newProduct); // Assuming update is done by adding a new product
            } else {
                throw new IllegalArgumentException("Product is invalid");
            }
        } else {
            throw new NoSuchElementException("Cannot update non-existent product");
        }
    }

    public List<Product> getAllProducts() {
        // Assuming productRepository has a method to get all products
        return productRepository.findAll();
    }

    public void addProductIfNotExists(Product product) {
        if (productRepository.findById(product.getId()).isEmpty()) {
            if (productValidator.isProductValid(product)) {
                productRepository.addProduct(product);
            } else {
                throw new IllegalArgumentException("Product is invalid");
            }
        } else {
            throw new IllegalArgumentException("Product already exists");
        }
    }
}

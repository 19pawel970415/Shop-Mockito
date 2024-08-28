Task: Shop with Mockito

Objective:

The goal of this task is to test your skills in using the Mockito library in practice. We will focus on mocking dependencies, modeling behaviors, and verifying interactions between objects. Below is the project structure, classes, and your task to write unit tests for the provided business logic.

Project Structure:

plaintext
Skopiuj kod
src/
│
├── main/
│   └── java/
│       └── com/example/store/
│           ├── Product.java
│           ├── ProductRepository.java
│           ├── ProductValidator.java
│           ├── ProductService.java
│           └── StoreException.java
│
└── test/
    └── java/
        └── com/example/store/
            └── ProductServiceTest.java
Class Descriptions:

Product

Represents a product in the store.

java
Skopiuj kod
package com.example.store;

public class Product {
    private Long id;
    private String name;
    private double price;

    // Getters, Setters, Constructor
    public Product(Long id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}
ProductRepository

Interface for managing products.

java
Skopiuj kod
package com.example.store;

import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(Long id);
    Product addProduct(Product product);
    void deleteProduct(Long id);
}
ProductValidator

Product validator that checks if a product is valid.

java
Skopiuj kod
package com.example.store;

public class ProductValidator {
    public boolean isProductValid(Product product) {
        return product != null && product.getName() != null && !product.getName().isBlank();
    }
}
ProductService

Business class for handling product-related logic.

java
Skopiuj kod
package com.example.store;

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
StoreException

Class for handling store-specific exceptions.

java
Skopiuj kod
package com.example.store;

public class StoreException extends RuntimeException {
    public StoreException(String message) {
        super(message);
    }
}
Your Task:

Write unit tests for the ProductService class in the ProductServiceTest.java file using Mockito. The tests should cover:

Retrieving Product by ID:

Test that a product is correctly returned when it exists.
Test that a NoSuchElementException is thrown when the product does not exist.
Adding Product:

Test that a product is correctly added when it is valid.
Test that an IllegalArgumentException is thrown when the product is invalid.
Deleting Product:

Test that a product is correctly deleted when it exists.
Test that a NoSuchElementException is thrown when the product does not exist.
Extend Tests:

Verify the order of method calls where critical to the correct implementation (use InOrder).

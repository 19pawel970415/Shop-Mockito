package org.example.store;

public class ProductValidator {
    public boolean isProductValid(Product product) {
        return product != null && product.getName() != null && !product.getName().isBlank();
    }
}

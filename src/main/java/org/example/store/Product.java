package org.example.store;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Product {
    private Long id;
    private String name;
    private double price;

    public void setPrice(double price) {
        this.price = price;
    }
}

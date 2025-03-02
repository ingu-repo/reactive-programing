package ingu.flux.model;

import lombok.Getter;

@Getter
public class Product {
    private int id;
    private String productName;
    public Product(int id, String productName) {
        this.id = id;
        this.productName = productName;
    }
}

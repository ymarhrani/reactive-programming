package com.learning.reactivedataaccess.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Data
@ToString
@NoArgsConstructor
public class Product {
    @Id
    private int id;
    private String name;
    private double amount;

    public Product(String name, double amount) {
        this.name = name;
        this.amount = amount;
    }
}

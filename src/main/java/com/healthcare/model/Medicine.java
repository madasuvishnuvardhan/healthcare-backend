package com.healthcare.model;

import jakarta.persistence.*;

@Entity
@Table(name = "medicines")
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String brand;
    private String category;
    private String symptoms;
    private double price;
    private int stock;

    public Medicine() {}

    public Medicine(Long id, String name, String brand, String category, String symptoms, double price, int stock) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.symptoms = symptoms;
        this.price = price;
        this.stock = stock;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getSymptoms() { return symptoms; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
}

package com.example.shopit.models;

public class Product {
    private String id;
    private String name;
    private int quantity;
    private String username;

    public Product() {
    }

    public Product(String id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public Product(String id, String name, int quantity, String username) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.username = username;
    }

    public Product(String name, int quantity, String username) {
        this.name = name;
        this.quantity = quantity;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
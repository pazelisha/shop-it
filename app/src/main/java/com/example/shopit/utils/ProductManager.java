package com.example.shopit.utils;

import com.example.shopit.models.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ProductManager {
    private static ProductManager instance;
    private final Map<String, List<Product>> productsMap = new HashMap<>();

    private ProductManager() {}

    public static synchronized ProductManager getInstance() {
        if (instance == null) {
            instance = new ProductManager();
        }
        return instance;
    }

    /**
     * Get a mutable list of products for the given user.
     */
    public List<Product> getProducts(String username) {
        List<Product> list = productsMap.get(username);
        if (list == null) {
            list = new ArrayList<>();
            productsMap.put(username, list);
        }
        return list;
    }


    public boolean addProduct(String username, String name, int quantity) {
        Product p = new Product(UUID.randomUUID().toString(), name, quantity, username);
        getProducts(username).add(p);
        return true;
    }


    public boolean removeProduct(String username, String productId) {
        List<Product> list = getProducts(username);
        Iterator<Product> it = list.iterator();
        while (it.hasNext()) {
            if (it.next().getId().equals(productId)) {
                it.remove();
                return true;
            }
        }
        return false;
    }
}
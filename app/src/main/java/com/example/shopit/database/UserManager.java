package com.example.shopit.database;

import android.util.Log;

import com.example.shopit.models.Product;
import com.example.shopit.models.User;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Class for managing user-related database operations
 */
public class UserManager {
    private static final String TAG = "UserManager";

    private MongoCollection<Document> usersCollection;
    private MongoCollection<Document> productsCollection;

    public UserManager() {
        MongoDBHelper dbHelper = MongoDBHelper.getInstance();
        usersCollection = dbHelper.getUsersCollection();
        productsCollection = dbHelper.getProductsCollection();
    }

    /**
     * Authenticate user with username and password
     * @param username User's username
     * @param password User's password
     * @return true if authentication successful, false otherwise
     */
    public boolean authenticateUser(String username, String password) {
        try {
            Document user = usersCollection.find(
                    Filters.and(
                            Filters.eq("username", username),
                            Filters.eq("password", password)
                    )
            ).first();

            return user != null;
        } catch (Exception e) {
            Log.e(TAG, "Authentication error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if username already exists
     * @param username Username to check
     * @return true if username exists, false otherwise
     */
    public boolean isUsernameExists(String username) {
        try {
            Document user = usersCollection.find(Filters.eq("username", username)).first();
            return user != null;
        } catch (Exception e) {
            Log.e(TAG, "Username check error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Register new user
     * @param user User object with registration data
     * @return true if registration successful, false otherwise
     */
    public boolean registerUser(User user) {
        try {
            // Check if username exists
            if (isUsernameExists(user.getUsername())) {
                return false;
            }

            // Create new user document
            Document newUser = new Document()
                    .append("username", user.getUsername())
                    .append("password", user.getPassword())
                    .append("phone", user.getPhone());

            // Insert new user
            usersCollection.insertOne(newUser);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Registration error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get user by username
     * @param username Username to find
     * @return User object if found, null otherwise
     */
    public User getUserByUsername(String username) {
        try {
            Document doc = usersCollection.find(Filters.eq("username", username)).first();

            if (doc != null) {
                String id = doc.getObjectId("_id").toString();
                String password = doc.getString("password");
                String phone = doc.getString("phone");

                return new User(id, username, password, phone);
            }

            return null;
        } catch (Exception e) {
            Log.e(TAG, "Get user error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Add new product for user
     * @param product Product to add
     * @return true if product added successfully, false otherwise
     */
    public boolean addProduct(Product product) {
        try {
            // Create new product document
            Document newProduct = new Document()
                    .append("username", product.getUsername())
                    .append("productName", product.getName())
                    .append("quantity", product.getQuantity());

            // Insert new product
            productsCollection.insertOne(newProduct);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Add product error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get products for a specific user
     * @param username Username to get products for
     * @return List of user's products
     */
    public List<Product> getUserProducts(String username) {
        List<Product> productList = new ArrayList<>();

        try {
            // Use MongoCursor instead of forEach to avoid ambiguity
            MongoCursor<Document> cursor = productsCollection.find(Filters.eq("username", username)).iterator();

            while (cursor.hasNext()) {
                Document doc = cursor.next();
                String id = doc.getObjectId("_id").toString();
                String name = doc.getString("productName");
                int quantity = doc.getInteger("quantity");

                Product product = new Product(id, name, quantity, username);
                productList.add(product);
            }

            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "Get products error: " + e.getMessage());
        }

        return productList;
    }

    /**
     * Remove a product by ID
     * @param productId ID of product to remove
     * @return true if product removed successfully, false otherwise
     */
    public boolean removeProduct(String productId) {
        try {
            productsCollection.deleteOne(Filters.eq("_id", new ObjectId(productId)));
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Remove product error: " + e.getMessage());
            return false;
        }
    }
}

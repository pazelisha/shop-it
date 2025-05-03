package com.example.shopit.database;

import android.util.Log;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

/**
 * Helper class for MongoDB connection management
 */
public class MongoDBHelper {
    private static final String TAG = "MongoDBHelper";
    private static final String CONNECTION_STRING = "";
    private static final String DATABASE_NAME = "shopping_app";

    private static MongoDBHelper instance;
    private MongoClient mongoClient;
    private MongoDatabase database;

    // Collections
    private MongoCollection<Document> usersCollection;
    private MongoCollection<Document> productsCollection;

    // Private constructor for singleton pattern
    private MongoDBHelper() {
        try {
            mongoClient = MongoClients.create(CONNECTION_STRING);
            database = mongoClient.getDatabase(DATABASE_NAME);

            // Initialize collections
            usersCollection = database.getCollection("users");
            productsCollection = database.getCollection("products");

            Log.d(TAG, "MongoDB connection established successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error connecting to MongoDB: " + e.getMessage());
        }
    }

    // Singleton getter
    public static synchronized MongoDBHelper getInstance() {
        if (instance == null) {
            instance = new MongoDBHelper();
        }
        return instance;
    }

    // Get users collection
    public MongoCollection<Document> getUsersCollection() {
        return usersCollection;
    }

    // Get products collection
    public MongoCollection<Document> getProductsCollection() {
        return productsCollection;
    }

    // Close connection
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            instance = null;
            Log.d(TAG, "MongoDB connection closed");
        }
    }
}
package com.example.shopit;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shopit.adapters.ProductAdapter;
import com.example.shopit.database.UserManager;
import com.example.shopit.models.Product;
import com.example.shopit.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for shopping management
 */
public class ShoppingActivity extends AppCompatActivity implements ProductAdapter.OnProductActionListener {

    private TextView welcomeTextView;
    private Button addProductButton, logoutButton;
    private ListView productsListView;

    private String username;
    private UserManager userManager;
    private SessionManager sessionManager;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        // Initialize managers
        userManager = new UserManager();
        sessionManager = SessionManager.getInstance(this);

        // Get username from session
        username = sessionManager.getUsername();

        // Check if user is logged in
        if (username == null) {
            finish(); // Return to login activity
            return;
        }

        // Initialize UI components
        welcomeTextView = findViewById(R.id.welcomeTextView);
        addProductButton = findViewById(R.id.addProductButton);
        logoutButton = findViewById(R.id.logoutButton);
        productsListView = findViewById(R.id.productsListView);

        // Set welcome message
        welcomeTextView.setText(getString(R.string.welcome_format, username));

        // Initialize product list and adapter
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productList, this);
        productsListView.setAdapter(productAdapter);

        // Fetch products
        fetchProducts();

        // Add product button click listener
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddProductDialog();
            }
        });

        // Logout button click listener
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear session data
                sessionManager.logout();

                // Return to login activity
                finish();
            }
        });
    }

    /**
     * Show dialog to add new product
     */
    private void showAddProductDialog() {
        // Create dialog
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_product);
        dialog.setTitle(R.string.add_product);

        // Initialize dialog components
        EditText productNameEditText = dialog.findViewById(R.id.productNameEditText);
        EditText productQuantityEditText = dialog.findViewById(R.id.productQuantityEditText);
        Button addButton = dialog.findViewById(R.id.addButton);
        Button cancelButton = dialog.findViewById(R.id.cancelButton);

        // Add button click listener
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = productNameEditText.getText().toString().trim();
                String quantityStr = productQuantityEditText.getText().toString().trim();

                if (productName.isEmpty() || quantityStr.isEmpty()) {
                    Toast.makeText(ShoppingActivity.this, R.string.error_empty_fields, Toast.LENGTH_SHORT).show();
                    return;
                }

                int quantity;
                try {
                    quantity = Integer.parseInt(quantityStr);
                    if (quantity <= 0) {
                        Toast.makeText(ShoppingActivity.this, R.string.error_invalid_quantity, Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(ShoppingActivity.this, R.string.error_invalid_quantity, Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create product object
                Product newProduct = new Product(productName, quantity, username);

                // Add product in background thread
                new Thread(() -> {
                    boolean isAdded = userManager.addProduct(newProduct);

                    runOnUiThread(() -> {
                        if (isAdded) {
                            // Dismiss dialog
                            dialog.dismiss();

                            // Refresh product list
                            fetchProducts();

                            Toast.makeText(ShoppingActivity.this, R.string.product_added, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ShoppingActivity.this, R.string.error_adding_product, Toast.LENGTH_SHORT).show();
                        }
                    });
                }).start();
            }
        });

        // Cancel button click listener
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Show dialog
        dialog.show();
    }

    /**
     * Method to fetch user's products
     */
    private void fetchProducts() {
        new Thread(() -> {
            List<Product> products = userManager.getUserProducts(username);

            runOnUiThread(() -> {
                productList.clear();
                productList.addAll(products);
                productAdapter.notifyDataSetChanged();
            });
        }).start();
    }

    /**
     * Handle remove product action
     * @param productId ID of product to remove
     */
    @Override
    public void onRemove(String productId) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.remove_product)
                .setMessage(R.string.confirm_remove_product)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    // Remove product in background thread
                    new Thread(() -> {
                        boolean isRemoved = userManager.removeProduct(productId);

                        runOnUiThread(() -> {
                            if (isRemoved) {
                                fetchProducts();
                                Toast.makeText(ShoppingActivity.this, R.string.product_removed, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ShoppingActivity.this, R.string.error_removing_product, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }).start();
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh product list
        fetchProducts();
    }}
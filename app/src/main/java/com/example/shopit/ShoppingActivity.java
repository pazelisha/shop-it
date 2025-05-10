package com.example.shopit;

import android.app.Dialog;
import android.content.Intent;
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
import com.example.shopit.models.Product;
import com.example.shopit.utils.ProductManager;
import com.example.shopit.utils.SessionManager;

import java.util.List;

public class ShoppingActivity extends AppCompatActivity implements ProductAdapter.OnProductActionListener {

    private TextView welcomeTextView;
    private Button addProductButton, logoutButton;
    private ListView productsListView;

    private String username;
    private SessionManager sessionManager;
    private ProductManager productManager;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        sessionManager = SessionManager.getInstance(this);
        productManager = ProductManager.getInstance();

        username = sessionManager.getUsername();
        if (username == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        welcomeTextView = findViewById(R.id.welcomeTextView);
        addProductButton = findViewById(R.id.addProductButton);
        logoutButton = findViewById(R.id.logoutButton);
        productsListView = findViewById(R.id.productsListView);

        welcomeTextView.setText(getString(R.string.welcome_format, username));

        productList = productManager.getProducts(username);
        productAdapter = new ProductAdapter(this, productList, this);
        productsListView.setAdapter(productAdapter);

        addProductButton.setOnClickListener(v -> showAddProductDialog());
        logoutButton.setOnClickListener(v -> {
            sessionManager.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void showAddProductDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_product);
        dialog.setTitle(R.string.add_product);

        EditText nameInput = dialog.findViewById(R.id.productNameEditText);
        EditText qtyInput  = dialog.findViewById(R.id.productQuantityEditText);
        Button addBtn      = dialog.findViewById(R.id.addButton);
        Button cancelBtn   = dialog.findViewById(R.id.cancelButton);

        addBtn.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String qtyStr = qtyInput.getText().toString().trim();
            if (name.isEmpty() || qtyStr.isEmpty()) {
                Toast.makeText(this, R.string.error_empty_fields, Toast.LENGTH_SHORT).show();
                return;
            }
            int qty;
            try {
                qty = Integer.parseInt(qtyStr);
                if (qty <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                Toast.makeText(this, R.string.error_invalid_quantity, Toast.LENGTH_SHORT).show();
                return;
            }
            productManager.addProduct(username, name, qty);
            productAdapter.notifyDataSetChanged();
            dialog.dismiss();
            Toast.makeText(this, R.string.product_added, Toast.LENGTH_SHORT).show();
        });

        cancelBtn.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    @Override
    public void onRemove(String productId) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.remove_product)
                .setMessage(R.string.confirm_remove_product)
                .setPositiveButton(R.string.yes, (d, w) -> {
                    productManager.removeProduct(username, productId);
                    productAdapter.notifyDataSetChanged();
                    Toast.makeText(this, R.string.product_removed, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        productAdapter.notifyDataSetChanged();
    }
}

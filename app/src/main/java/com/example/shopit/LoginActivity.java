package com.example.shopit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shopit.database.UserManager;
import com.example.shopit.utils.SessionManager;

/**
 * Activity for user login
 */
public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton, registerButton;
    private TextView errorTextView;

    private UserManager userManager;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize managers
        userManager = new UserManager();
        sessionManager = SessionManager.getInstance(this);

        // Check if user is already logged in
        if (sessionManager.isLoggedIn()) {
            // Go to shopping activity directly
            navigateToShoppingActivity();
        }

        // Initialize UI components
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        errorTextView = findViewById(R.id.errorTextView);

        // Login button click listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    errorTextView.setText(R.string.error_empty_fields);
                    return;
                }

                // Validate login credentials in background thread
                new Thread(() -> {
                    boolean isAuthenticated = userManager.authenticateUser(username, password);

                    runOnUiThread(() -> {
                        if (isAuthenticated) {
                            // Login successful
                            errorTextView.setText("");

                            // Create login session
                            sessionManager.createLoginSession(username);

                            // Navigate to shopping activity
                            navigateToShoppingActivity();
                        } else {
                            // Login failed
                            errorTextView.setText(R.string.error_invalid_credentials);
                        }
                    });
                }).start();
            }
        });

        // Register button click listener
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Navigate to shopping activity
     */
    private void navigateToShoppingActivity() {
        Intent intent = new Intent(LoginActivity.this, ShoppingActivity.class);
        startActivity(intent);
        finish(); // Close login activity
    }
}
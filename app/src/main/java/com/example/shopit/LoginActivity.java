package com.example.shopit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shopit.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton, registerButton;
    private TextView errorTextView;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = SessionManager.getInstance(this);

        // If already logged in, skip to shopping
        if (sessionManager.isLoggedIn()) {
            navigateToShoppingActivity();
            return;
        }

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton      = findViewById(R.id.loginButton);
        registerButton   = findViewById(R.id.registerButton);
        errorTextView    = findViewById(R.id.errorTextView);

        loginButton.setOnClickListener(v -> {
            String user = usernameEditText.getText().toString().trim();
            String pass = passwordEditText.getText().toString().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                errorTextView.setText(R.string.error_empty_fields);
                return;
            }

            boolean success = sessionManager.login(user, pass);
            if (success) {
                navigateToShoppingActivity();
            } else {
                errorTextView.setText(R.string.error_invalid_credentials);
            }
        });

        registerButton.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void navigateToShoppingActivity() {
        startActivity(new Intent(this, ShoppingActivity.class));
        finish();
    }
}
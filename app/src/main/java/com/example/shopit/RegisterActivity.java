package com.example.shopit;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shopit.database.UserManager;
import com.example.shopit.models.User;

/**
 * Activity for user registration
 */
public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText, phoneEditText;
    private Button registerButton, backButton;
    private TextView errorTextView;

    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize user manager
        userManager = new UserManager();

        // Initialize UI components
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        registerButton = findViewById(R.id.registerButton);
        backButton = findViewById(R.id.backButton);
        errorTextView = findViewById(R.id.errorTextView);

        // Register button click listener
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty() || phone.isEmpty()) {
                    errorTextView.setText(R.string.error_empty_fields);
                    return;
                }

                // Create user object
                User newUser = new User(username, password, phone);

                // Register user in background thread
                new Thread(() -> {
                    boolean isRegistered = userManager.registerUser(newUser);

                    runOnUiThread(() -> {
                        if (isRegistered) {
                            Toast.makeText(RegisterActivity.this, R.string.registration_success, Toast.LENGTH_SHORT).show();
                            finish(); // Return to login screen
                        } else {
                            errorTextView.setText(R.string.error_username_exists);
                        }
                    });
                }).start();
            }
        });

        // Back button click listener
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Return to login screen
            }
        });
    }
}
package com.example.shopit;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shopit.utils.SessionManager;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText, phoneEditText;
    private Button registerButton, backButton;
    private TextView errorTextView;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sessionManager = SessionManager.getInstance(this);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        phoneEditText    = findViewById(R.id.phoneEditText);
        registerButton   = findViewById(R.id.registerButton);
        backButton       = findViewById(R.id.backButton);
        errorTextView    = findViewById(R.id.errorTextView);

        registerButton.setOnClickListener(v -> {
            String user  = usernameEditText.getText().toString().trim();
            String pass  = passwordEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();

            if (user.isEmpty() || pass.isEmpty() || phone.isEmpty()) {
                errorTextView.setText(R.string.error_empty_fields);
                return;
            }

            boolean registered = sessionManager.register(user, pass, phone);
            if (registered) {
                Toast.makeText(this, R.string.registration_success, Toast.LENGTH_SHORT).show();
                finish(); // go back to Login
            } else {
                errorTextView.setText(R.string.error_username_exists);
            }
        });

        backButton.setOnClickListener(v -> finish());
    }
}

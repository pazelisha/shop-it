package com.example.shopit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shopit.utils.SessionManager;

/**
 * Splash/Main activity - entry point of the application
 */
public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_TIMEOUT = 2000; // 2 seconds
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize session manager
        sessionManager = SessionManager.getInstance(this);

        // Initialize UI components
        ImageView logoImageView = findViewById(R.id.logoImageView);
        TextView appNameTextView = findViewById(R.id.appNameTextView);

        // Set app logo and name animations if needed

        // Handler to start the next activity after SPLASH_TIMEOUT
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check if user is already logged in
                if (sessionManager.isLoggedIn()) {
                    // Go to shopping activity directly
                    Intent intent = new Intent(MainActivity.this, ShoppingActivity.class);
                    startActivity(intent);
                } else {
                    // Go to login activity
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

                // Close this activity
                finish();
            }
        }, SPLASH_TIMEOUT);
    }
}
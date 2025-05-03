package com.example.shopit.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Session manager to store and handle user session data
 */
public class SessionManager {

    // Shared preferences constants
    private static final String PREF_NAME = "ShoppingAppPref";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USERNAME = "username";
    private static final int PRIVATE_MODE = 0;

    // Shared preferences and editor
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    // Singleton instance
    private static SessionManager instance;

    /**
     * Private constructor
     * @param context Application context
     */
    private SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    /**
     * Get singleton instance
     * @param context Application context
     * @return Session manager instance
     */
    public static synchronized SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context);
        }
        return instance;
    }

    /**
     * Create login session
     * @param username User's username
     */
    public void createLoginSession(String username) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USERNAME, username);
        editor.commit();
    }

    /**
     * Get logged in user's username
     * @return Username or null if not logged in
     */
    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, null);
    }

    /**
     * Check if user is logged in
     * @return true if logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    /**
     * Clear session data and log out
     */
    public void logout() {
        editor.clear();
        editor.commit();
    }
}
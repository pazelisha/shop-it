package com.example.shopit.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREFS_NAME = "user_prefs";
    private static final String KEY_USERNAME = "key_username";
    private static final String KEY_PASSWORD = "key_password";
    private static final String KEY_PHONE    = "key_phone";
    private static final String KEY_LOGGED_IN = "key_logged_in";

    private static SessionManager instance;
    private SharedPreferences prefs;

    private SessionManager(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context);
        }
        return instance;
    }


    public boolean register(String username, String password, String phone) {
        if (prefs.contains(KEY_USERNAME)) {
            return false;
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_PHONE, phone);
        editor.apply();
        return true;
    }


    public boolean login(String username, String password) {
        String storedUser = prefs.getString(KEY_USERNAME, null);
        String storedPass = prefs.getString(KEY_PASSWORD, null);
        if (storedUser != null && storedUser.equals(username)
                && storedPass != null && storedPass.equals(password)) {
            prefs.edit().putBoolean(KEY_LOGGED_IN, true).apply();
            return true;
        }
        return false;
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_LOGGED_IN, false);
    }


    public void logout() {
        prefs.edit().putBoolean(KEY_LOGGED_IN, false).apply();
    }


    public String getUsername() {
        return prefs.getString(KEY_USERNAME, null);
    }
}

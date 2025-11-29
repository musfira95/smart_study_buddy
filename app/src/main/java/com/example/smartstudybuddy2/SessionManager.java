package com.example.smartstudybuddy2;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "SmartStudyBuddySession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ROLE = "role"; // new key

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    // Create login session with role
    public void createLoginSession(String email, String role) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ROLE, role); // save role
        editor.apply();
    }

    // Check login state
    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Get stored email
    public String getUserEmail() {
        return prefs.getString(KEY_EMAIL, null);
    }

    // Get stored role
    public String getUserRole() {
        return prefs.getString(KEY_ROLE, "user"); // default user
    }

    // 1️⃣ Add a key for user name
    private static final String KEY_NAME = "name";

    // 2️⃣ Update createLoginSession to save name too
    public void createLoginSession(String name, String email, String role) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_NAME, name);      // save name
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ROLE, role);      // save role
        editor.apply();
    }

    // 3️⃣ Add getter for user name
    public String getUserName() {
        return prefs.getString(KEY_NAME, "User");
    }


    // Logout user
    public void logoutUser() {
        editor.clear();
        editor.apply();
    }
}

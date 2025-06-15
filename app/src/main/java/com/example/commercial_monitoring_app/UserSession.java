package com.example.commercial_monitoring_app;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSession {
    // Singleton instance
    private static UserSession instance;

    // SharedPreferences constants
    private static final String PREF_NAME = "UserSessionPref";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_EMAIL = "user_email";

    private static SharedPreferences prefs = null;
    private final SharedPreferences.Editor editor;

    private UserSession(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public static synchronized UserSession getInstance(Context context) {
        if (instance == null) {
            instance = new UserSession(context);
        }
        return instance;
    }

    public static int getLoggedUserId() {
        return prefs.getInt(KEY_USER_ID, -1);
    }

    public void saveUserSession(int id, String email) {
        editor.putInt(KEY_USER_ID, id);
        editor.putString(KEY_USER_EMAIL, email);
        editor.apply();
    }

    public String getUserEmail() {
        return prefs.getString(KEY_USER_EMAIL, null);
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }

    public boolean isLoggedIn() {
        return getLoggedUserId() != -1;
    }

}
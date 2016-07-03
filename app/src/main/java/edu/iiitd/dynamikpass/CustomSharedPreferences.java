package edu.iiitd.dynamikpass;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by deepaksood619 on 3/7/16.
 */
public class CustomSharedPreferences {

    private SharedPreferences sharedPreferences;

    private static final String LOGIN_KEY = "login_key";

    private CustomSharedPreferences sharedInstance;
    private CustomSharedPreferences shared() {
        if(sharedInstance == null) {
            sharedInstance = new CustomSharedPreferences();
        }
        return sharedInstance;
    }

    private void init(Context context) {
        if(sharedPreferences == null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
    }

    public boolean setLogin(Boolean login) {
        if(sharedPreferences == null) {
            return false;
        }
        return sharedPreferences.edit().putBoolean(LOGIN_KEY, login).commit();
    }

    public boolean getLogin() {
        return sharedPreferences.getBoolean(LOGIN_KEY, true);
    }

}

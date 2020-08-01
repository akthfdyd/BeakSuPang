
package com.madteam.beaksupang.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {

    private static Preferences instance;
    private static SharedPreferences preferences;

    public static Preferences getInstance(Context context) {
        if (instance == null) {
            instance = new Preferences(context.getApplicationContext());
        }

        return instance;
    }

    private Preferences(Context context) {
        if (preferences == null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
    }

    public void setScoreData(String str) {
        preferences.edit().putString("scoreData", str).commit();
    }

    public String getScoreData() {
        return preferences.getString("scoreData", "");
    }

}

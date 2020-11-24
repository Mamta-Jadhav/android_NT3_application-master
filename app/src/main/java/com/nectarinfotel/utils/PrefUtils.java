package com.nectarinfotel.utils;

/**
 * Created by Nectar on 17-09-2018.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class PrefUtils {
    /**
     * Storing API Key in shared preferences to
     * add it in header part of every retrofit request
     */
    public PrefUtils() {
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("APP_PREF", Context.MODE_PRIVATE);
    }

    public static void storeApiKey(Context context, String apiKey) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("API_KEY", apiKey);
        editor.commit();
    }

    public static String getKey(Context context, String key) {
        return getSharedPreferences(context).getString(key, null);
    }

    public static void storeKey(Context context, String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getORG(Context context) {
        return getSharedPreferences(context).getString("ORG", "");
    }

    public static void setORG(Context context, String value) {
        Log.d("ORG", "" + value);
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("ORG", value);
        editor.commit();
    }

    public static String getApiKey(Context context) {
        return getSharedPreferences(context).getString("API_KEY", null);
    }
}
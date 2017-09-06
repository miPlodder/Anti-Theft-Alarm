package com.example.saksham.pitpocketingapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by saksham on 9/6/2017.
 */

public class Constants {

    public static final String TAG = "Constants";

    public static class SharedPrefsConstants {


        public static int getValue(String key, Context context) {

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

            int rv = Integer.parseInt(prefs.getString(key, "-1"));

            return rv;
        }


    }

}

package com.example.saksham.pitpocketingapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by saksham on 9/6/2017.
 */

public class Constants {

    public static final String TAG = "Constants";

    public static class SharedPrefsConstants {


        public static final String IS_FIRST_TIME_LAUNCH = "IsFirstTime";

        public static int getValue(String key, Context context) {

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

            int rv = Integer.parseInt(prefs.getString(key, "-1"));

            return rv;
        }


        public static boolean isFirstTime(Context context) {

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

            if (prefs.getBoolean(IS_FIRST_TIME_LAUNCH, true)) {

                setForFirstTime(context);
                return true;
            }
            return false;
        }


        public static void setForFirstTime(Context context) {

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = prefs.edit();

            editor.putBoolean(IS_FIRST_TIME_LAUNCH, false);
            editor.commit();

        }

    }

}

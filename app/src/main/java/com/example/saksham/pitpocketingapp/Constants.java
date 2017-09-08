package com.example.saksham.pitpocketingapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by saksham on 9/6/2017.
 */

public class Constants {

    public static final String TAG = "Constants";

    public static final String HELP = "To enable protection while you are in crowded area, follow the below steps, \n\n\n" +
            "(1) Click the Start Button to start Anti-Theft " +
            "Protection.\n\n" +
            "(2) Then the Sleep Time starts. (manage time in " +
            "Settings)\n\n" +
            "(3) Lock your device within that time or just keep " +
            "your phone in your Pocket.\n\n" +
            "(4) Now, Whenever you take your phone out from " +
            "your Pocket, Enter your password within Wake Up " +
            "Time. (manage it from Settings)\n\n" +
            "(5) If you are unable to Unlock the device within " +
            "Wake Up, just click the STOP Button to stop the " +
            "ALARM." ;

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

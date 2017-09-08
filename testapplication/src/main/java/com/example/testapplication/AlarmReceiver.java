package com.example.testapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by saksham on 9/9/2017.
 */

public class AlarmReceiver extends WakefulBroadcastReceiver {

    public static final String TAG = "ALarmReceiver" ;
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "inside broadcast receiver");

        Intent service = new Intent(context, MyService.class);
        startWakefulService(context, service);

        if(Intent.ACTION_SCREEN_OFF.equals("android.intent.action.SCREEN_OFF")){

            Log.d(TAG, "inside br on screen OFF");
            Intent ser = new Intent(context, MyService.class);
            startWakefulService(context, ser);


        }
    }
}

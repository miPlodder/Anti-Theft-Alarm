package com.example.saksham.pitpocketingapp;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import static android.content.Context.KEYGUARD_SERVICE;

public class WakeUpReceiver extends BroadcastReceiver {

    public static final String TAG = "WakeUpReceiver";
    private Context context ;
    private OnWakeUp onWakeUp;
    KeyguardManager km;
    Thread fiveSecondThread;

    public WakeUpReceiver(){

    }

    public WakeUpReceiver(Context context, OnWakeUp onWakeUp) {

        this.context = context;
        this.onWakeUp = onWakeUp;

    }

    interface OnWakeUp {

        void setOnWakeUp();
    }

    @Override
    public void onReceive(final Context context, Intent intent) {

        Log.d(TAG, "onReceive: ");
        //Toast.makeText(context, "HELLO FROM GUYS", Toast.LENGTH_SHORT).show();
        //Log.d(TAG, "onReceive: again");
        //checks for keyguard is active or not

        if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {

            Log.d(TAG, "onReceive: User Present");
            if (fiveSecondThread != null) {
                //destroying the thread when phone is unlocked
                fiveSecondThread.interrupt();
            }
            Toast.makeText(context, "Welcome user", Toast.LENGTH_SHORT).show();
        }

        //TODO check for proximity sensor
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

            Log.d(TAG, "onReceive: Screen On");
            km = (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);

            fiveSecondThread = new Thread(new Runnable() {
                @Override
                public void run() {

                    try {

                        Thread.sleep(5000);
                        if (km.inKeyguardRestrictedInputMode()) {

                            //screen is locked
                            Log.d(TAG, "run: SCREEN IS LOCKED");
                            onWakeUp.setOnWakeUp();

                        } else {

                            Log.d(TAG, "onReceive: SCREEN UNLOCKED");
                            //this case will be handled by ACTION_SCREEN_ON intent
                        }
                    } catch (InterruptedException e)

                    {
                        e.printStackTrace();
                    }

                }
            });
            fiveSecondThread.start();


            //just logging that screen is off
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {

                Log.d(TAG, "onReceive: Screen Off");
                //register sensor manager here
                //proxmity work done here
               /*TODO work here on sensor manager waittime -> infinity
                *
                * MySensorManager sm = new MySensorManager(context);
                sm.startProximity();
                *
                *
                */
            }
        }
    }
}
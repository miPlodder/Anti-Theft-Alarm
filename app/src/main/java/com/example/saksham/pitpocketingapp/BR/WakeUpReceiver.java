package com.example.saksham.pitpocketingapp.BR;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.saksham.pitpocketingapp.Constants;

import static android.content.Context.KEYGUARD_SERVICE;

public class WakeUpReceiver extends BroadcastReceiver {

    public static final String TAG = "WakeUpReceiver";
    private Context context;
    private OnWakeUp onWakeUp;
    KeyguardManager km;
    Thread nSecThread;

    public WakeUpReceiver() {
        this.context = context;
    }

    public WakeUpReceiver(Context context, OnWakeUp onWakeUp) {

        this.context = context;
        this.onWakeUp = onWakeUp;

    }

    public interface OnWakeUp {

        void setOnWakeUp();
    }

    @Override
    public void onReceive(final Context context, Intent intent) {

        //broadcast receiver triggered when users logs in Keyguard
        if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {

            Log.d(TAG, "onReceive: User Present");
            if (nSecThread != null) {

                //destroying the thread when phone is unlocked
                nSecThread.interrupt();
            }

            Toast.makeText(context, "\t\t\t\t\t\tWELCOME USER \n\nClick STOP button to stop the ALARM", Toast.LENGTH_SHORT).show();
        }

        //TODO check for proximity sensor
        //broadcast receiver when screen gets ON
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

            km = (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);

            nSecThread = new Thread(new Runnable() {
                @Override
                public void run() {

                    try {


                        int sleepTime = Constants.SharedPrefsConstants.getValue("wakeTime",context) * 1000;
                        Log.d(TAG, "time: "+sleepTime);
                        Thread.sleep(sleepTime);

                        //this checks whether keyguard is still there or user has unlocked the device
                        if (km.inKeyguardRestrictedInputMode()) {

                            //screen is locked, so ALERT USER
                            onWakeUp.setOnWakeUp();

                        } else {

                            //screen is unlocked here
                            //this case will be handled by ACTION_SCREEN_ON intent
                        }
                    } catch (InterruptedException e)

                    {
                        e.printStackTrace();
                    }
                }
            });

            nSecThread.start();

            //TODO work here on sensor manager WAITTIME -> INFINITY
            /*
                No requirement to start proximity
             */
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {

                Log.d(TAG, "onReceive: Screen Off");
                //register sensor manager here
                //proxmity work done here
               /*
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
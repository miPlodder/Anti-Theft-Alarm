package com.example.testapplication;

import android.app.IntentService;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by saksham on 9/9/2017.
 */

public class MyService extends IntentService {

    public static final String TAG = "MyService";


    public MyService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {

        Log.d(TAG, "inside service");

        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);

        SensorEventListener sel = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                //inactive
                if(event.values[0] ==0){

                    try {
                        Log.d(TAG, "sleeping again");
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "safe ");
                }
                //theft
                else{

                    Log.d(TAG, "theft");
                    AlarmReceiver.completeWakefulIntent(intent);
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

                Log.d(TAG, "onAccuracyChanged: WORKING");

            }
        };
        Sensor sensor = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sm.registerListener(sel, sensor, 1000);

    }

}

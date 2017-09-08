package com.example.saksham.pitpocketingapp.Hardwares;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.PowerManager;
import android.util.Log;
import android.view.WindowManager;

/**
 * Created by saksham on 8/24/2017.
 */

public class MySensorManager {

    Context context;
    SensorManager sm; //sensor name, and sensor onclick listener
    Sensor proximty;
    SensorEventListener psel;
    public static final String TAG = "MySensorManager";
    onWakeUp onWakeUp;
    PowerManager.WakeLock wl;

    public interface onWakeUp {

        void setOnWakeUp();
    }


    public MySensorManager(Context context, onWakeUp onWakeUp) {
        this.context = context;
        sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.onWakeUp = onWakeUp;
    }

    public void startProximity(final PowerManager.WakeLock wl) {

        proximty = sm.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (wl.isHeld()) {
            Log.d(TAG, "startProximity: held");
        } else {
            Log.d(TAG, "startProximity: not held");
        }


        psel = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent event) {


                if (event.values[0] == 0) {
                    Log.d(TAG, "onSensorChanged: 0" + ", device INACTIVE");
                } else {
                    //TODO wake the device up
                    wl.release();
                    Log.d(TAG, "onSensorChanged: 6" + ", device ACTIVE");
                    onWakeUp.setOnWakeUp();
                    if (wl.isHeld()) {
                        Log.d(TAG, "startProximity: held");
                    } else {
                        Log.d(TAG, "startProximity: not held");
                    }

                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

                //do nothing here
            }
        };
        Log.d(TAG, "registering the proximity sensor");
        sm.registerListener(psel, proximty, 1000);

    }

    public void stopProximity() {

        Log.d(TAG, "unregistering the proximity sensor: ");
        sm.unregisterListener(psel, proximty);
    }


}

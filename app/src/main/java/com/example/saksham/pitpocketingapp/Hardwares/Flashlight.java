package com.example.saksham.pitpocketingapp.Hardwares;

import android.content.Context;
import android.graphics.Camera;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by saksham on 7/30/2017.
 */

public class Flashlight {

    android.hardware.Camera camera;
    android.hardware.Camera.Parameters params;
    int delay = 100;      // time is always in MILLISECONDS

    public static boolean isInitialise = false;
    public static final String TAG = "FlashLight";
    ArrayList<Blinker> al;

    public Flashlight(Context context) {

        //we should never open camera again and again
        //unlike mediaplayer which has to be created again, after stopping it
        camera = android.hardware.Camera.open();
        params = camera.getParameters();
        al = new ArrayList<>();

    }

    public void startFlash() {

        isInitialise = true;
        if (al.size() != 0) {
            if (!(AsyncTask.Status.RUNNING == al.get(0).getStatus())) {

                //not running async task
                al.get(0).execute();
                Log.d(TAG, "not running async task");

            } else {
                //running start
            }
        } else {

            //creating
            Log.d(TAG, "running asynctask and inside else");
            al.add(new Blinker());
            startFlash();

        }
    }


    public void stopFlash() {

        Log.d(TAG, "stopFlash: insdide stopflash");
        isInitialise = false;
        if (al.size() != 0) {

            al.get(0).cancel(true);
            al.remove(0);
            Log.d(TAG, "inside stop if condition, size->"+al.size());
            params.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
        } else {

            Log.d(TAG, "inside stop else condition");
        }
    }

    static public boolean isInitialised() {
        return isInitialise;
    }

    private class Blinker extends AsyncTask<Void, Void, Void> {

        public static final String TAG = "Blinker";
        Boolean isFlashOn = true;

        @Override
        protected Void doInBackground(Void... input) {

            while (!isCancelled()) {

                isFlashOn = isOn(isFlashOn);

                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {

                    Log.d(TAG, "InterruptedException " + e.getMessage());
                }

            }

            return null;
        }

        public boolean isOn(boolean isFlashOn) {

            if (isFlashOn) {

                params.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_TORCH);
            } else {

                params.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_OFF);

            }
            try {
                camera.setParameters(params);
            } catch (Exception e) {
                Log.d(TAG, "isOn: " + params + "inside try-catch thread id" + Thread.currentThread().toString());
                Log.d(TAG, "isOn: ------------------" + e.getMessage() + "," + e.getLocalizedMessage() + "," + e.toString());
            }

            return !isFlashOn;
        }
    }
}
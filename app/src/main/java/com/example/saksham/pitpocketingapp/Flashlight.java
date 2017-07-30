package com.example.saksham.pitpocketingapp;

import android.content.Context;
import android.graphics.Camera;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by saksham on 7/30/2017.
 */

public class Flashlight {

    android.hardware.Camera camera;
    android.hardware.Camera.Parameters params;
    int delay = 100; //millisec
    Blinker blinker;
    boolean isInitialise = false;
    public static final String TAG = "FlashLight";

    public Flashlight(Context context) {

    }

    public void startFlash() {

        camera = android.hardware.Camera.open();
        Log.d(TAG, "startFlash: ");
        params = camera.getParameters();
        blinker = new Blinker();
        blinker.execute();
        isInitialise = true;
    }

    public void stopFlash() {

        isInitialise = false;
        blinker.cancel(true);
        camera.release();

    }

    public boolean isInitialised() {
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
                Log.d(TAG, "isOn: ------------------" + e.getMessage());
            }

            Log.d(TAG, "isOn: " + params.toString());

            return !isFlashOn;
        }


    }


}

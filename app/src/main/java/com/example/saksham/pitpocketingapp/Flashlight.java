package com.example.saksham.pitpocketingapp;

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
    int delay = 100; //millisec
    Blinker blinker;
    boolean isInitialise = false;
    public static final String TAG = "FlashLight";
    static ArrayList<Blinker> threadsFlashlight = new ArrayList<>();

    public Flashlight(Context context) {

    }

    public void startFlash() {

        Log.d(TAG, "startFlash: list of threads " + threadsFlashlight);
        if (threadsFlashlight.size() > 1) {
            //delete previous threads
            for (int i = 0; i < threadsFlashlight.size() - 1; i++) {

                threadsFlashlight.get(i).cancel(true);
                Log.d(TAG, "startFlash: cancelling threads");
            }
        }

        camera = android.hardware.Camera.open();
        Log.d(TAG, "startFlash:1 " + params);
        params = camera.getParameters();
        Log.d(TAG, "startFlash:2 " + params);
        blinker = new Blinker();
        blinker.execute();
        isInitialise = true;
        threadsFlashlight.add(blinker);
        Log.d(TAG, "startFlash: "+threadsFlashlight.size()+"size");

    }

    public void stopFlash() {

        isInitialise = false;
        blinker.cancel(true);
        camera.release();
        Log.d(TAG, "stopFlash3: " + params);

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
                Log.d(TAG, "isOn: " + params + "inside try-catch thread id" + Thread.currentThread().toString());
                Log.d(TAG, "isOn: ------------------" + e.getMessage() + "," + e.getLocalizedMessage() + "," + e.toString());
            }

            /*Log.d(TAG, "isOn: " + params.toString());*/

            return !isFlashOn;
        }


    }


}

package com.example.testapplication;

import android.os.CountDownTimer;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new CountDownTimer(15000, 1000) {

            int counter = 0 ;
            @Override
            public void onTick(long millisUntilFinished) {

                Log.d(TAG, "onTick: "+millisUntilFinished);
                counter++;
            }

            @Override
            public void onFinish() {

                Log.d(TAG, "onFinish: "+counter);

            }
        }.start();

    }
}

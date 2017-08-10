package com.example.saksham.pitpocketingapp;

import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.security.Policy;

public class MainActivity extends AppCompatActivity {

    Button btnStart, btnStop;
    WakeUpReceiver mReceiver;
    public static final String TAG = "MainActivity";
    MediaPlayer mp;
    AudioManager am;
    android.hardware.Camera camera;
    android.hardware.Camera.Parameters params;
    Thread onOffFlash;
    BackgroundAudio backgroundAudio;
    Flashlight flashlight;
    IntentFilter i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        camera = android.hardware.Camera.open();
        params = camera.getParameters();

        i = new IntentFilter();


        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);

        backgroundAudio = new BackgroundAudio(MainActivity.this);
        flashlight = new Flashlight(MainActivity.this);


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                flashlight.isInitialise = true;
                backgroundAudio.isInitialise = true;
                Toast.makeText(MainActivity.this, "Lock your phone in 5 seconds ", Toast.LENGTH_SHORT).show();

                //pending work here //todo


                mReceiver = new WakeUpReceiver(MainActivity.this, new WakeUpReceiver.OnWakeUp() {
                    @Override
                    public void setOnWakeUp() {

                        if (flashlight.isInitialise && backgroundAudio.isInitialise) {

                            backgroundAudio.startAudio();
                            //flashlight.startFlash();
                        } else {
                            Log.d(TAG, "setOnWakeUp: do nothing");
                        }
                    }
                });

                //registering the broadcast receiver here

                i.addAction(Intent.ACTION_SCREEN_OFF);
                i.addAction(Intent.ACTION_SCREEN_ON);

                //checks that the keyguard is active or not
                i.addAction(Intent.ACTION_USER_PRESENT);
                registerReceiver(mReceiver, i);

            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flashlight.isInitialise && backgroundAudio.isInitialise) {

                    //flashlight.isInitialise = false;
                    backgroundAudio.isInitialise = false;

                    if (backgroundAudio != null && flashlight != null) {

                        backgroundAudio.stopAudio();
                        //flashlight.stopFlash();

                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
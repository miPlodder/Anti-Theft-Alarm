package com.example.saksham.pitpocketingapp;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
    ActionBar actionBar;
    boolean toggle = true; //toggle is used to register the broadcast receiver once only
    CountDownTimer waitTime;
    MySensorManager proximitySensor;
    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName mComponentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        camera = android.hardware.Camera.open();
        params = camera.getParameters();

        i = new IntentFilter();

        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);

        backgroundAudio = new BackgroundAudio(MainActivity.this);
        flashlight = new Flashlight(MainActivity.this);

        mDevicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        mComponentName = new ComponentName(this, MyAdminReceiver.class);

        //doing registering of WakeUpReceiver work here
        mReceiver = new WakeUpReceiver(MainActivity.this, new WakeUpReceiver.OnWakeUp() {
            @Override
            public void setOnWakeUp() {

                //this check is important
                if (flashlight.isInitialise && backgroundAudio.isInitialise) {

                    backgroundAudio.startAudio();
                    flashlight.startFlash();
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
        toggle = false;


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //changing staates of hardware here
                flashlight.isInitialise = true;
                backgroundAudio.isInitialise = true;
                Toast.makeText(MainActivity.this, "Lock your phone in 5 seconds ", Toast.LENGTH_SHORT).show();
                waitTime.start();
                //pending work here //todo


            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flashlight.isInitialise && backgroundAudio.isInitialise) {

                    flashlight.isInitialise = false;
                    backgroundAudio.isInitialise = false;
                    waitTime.cancel();

                    if (backgroundAudio != null && flashlight != null) {

                        Log.d(TAG, "onClick: inside if if");
                        backgroundAudio.stopAudio();
                        flashlight.stopFlash();
                    }
                }
            }
        });

        //adding countdown counter
        proximitySensor = new MySensorManager(MainActivity.this, new MySensorManager.onWakeUp() {
            @Override
            public void setOnWakeUp() {

                //TODO screen turn on work to be done here
                Log.d(TAG, "setOnWakeUp: SCREEN WAKEUP");
                PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "tag");
                wl.acquire();
                wl.release();
                //turning off proximity
                proximitySensor.stopProximity();

            }
        });


        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "description");
        startActivityForResult(intent, 15);

        //used to turn screen off
        waitTime = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //TODO add progressbar here
                Log.d(TAG, "onTick: " + millisUntilFinished);

            }

            @Override
            public void onFinish() {

                //screen turn off work to be done here
                //changes the system settings timeout for screen to 15 seconds(lowest possible)
                //Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, 15000);
                boolean isAdmin = mDevicePolicyManager.isAdminActive(mComponentName);
                if (isAdmin) {
                    mDevicePolicyManager.lockNow();
                } else {
                    Toast.makeText(getApplicationContext(), "Not Registered as admin", Toast.LENGTH_SHORT).show();
                }

                proximitySensor.startProximity();
                Log.d(TAG, "onFinish: ");

            }
        };

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case R.id.setting:
                Toast.makeText(this, "Setting clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.About:
                break;
            case R.id.feedback:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        unregisterReceiver(mReceiver);
        backgroundAudio.destroyInstance();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 15) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Registered As Admin", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to register as Admin", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
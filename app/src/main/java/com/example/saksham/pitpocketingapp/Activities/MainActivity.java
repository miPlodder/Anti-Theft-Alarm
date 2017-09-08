package com.example.saksham.pitpocketingapp.Activities;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saksham.pitpocketingapp.Constants;
import com.example.saksham.pitpocketingapp.Hardwares.BackgroundAudio;
import com.example.saksham.pitpocketingapp.Hardwares.Flashlight;
import com.example.saksham.pitpocketingapp.BR.MyAdminReceiver;
import com.example.saksham.pitpocketingapp.Hardwares.MySensorManager;
import com.example.saksham.pitpocketingapp.R;
import com.example.saksham.pitpocketingapp.BR.WakeUpReceiver;

import java.lang.reflect.InvocationTargetException;

import is.arontibo.library.ElasticDownloadView;

public class MainActivity extends AppCompatActivity {

    Button btnStart, btnStop;
    WakeUpReceiver mReceiver;
    public static final String TAG = "MainActivity";
    /*android.hardware.Camera camera;
    android.hardware.Camera.Parameters params;*/
    BackgroundAudio backgroundAudio;
    Flashlight flashlight;
    IntentFilter i;
    boolean toggle = true; //toggle is used to register the broadcast receiver once only
    CountDownTimer waitTime;
    MySensorManager proximitySensor;
    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName mComponentName;
    ElasticDownloadView edv;
    CountDownTimer cdt;
    int currSleepTime = 0;
    TextView tvHelp;

    PowerManager pm;
    PowerManager.WakeLock wl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*camera = android.hardware.Camera.open();
        params = camera.getParameters();*/

        //to keep the CPU Awake for longer duration of Time
        pm = (PowerManager) getSystemService(POWER_SERVICE);
        //wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "tag");
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "tag");

        //not needed in newer device, but is recommended to use as per Android Documentation
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Context context = this;
        Log.d(TAG, "onCreate: " + sharedPreferences.getString("audioVolume", "3") + context);
        Log.d(TAG, "onCreate: " + sharedPreferences.getString("wakeTime", "3"));

        i = new IntentFilter();

        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);
        edv = (ElasticDownloadView) findViewById(R.id.edv);
        tvHelp = (TextView) findViewById(R.id.tvHelp);

        tvHelp.setText(Constants.HELP);


        currSleepTime = (Constants.SharedPrefsConstants.getValue("sleepTime", this) * 1000);
        Log.d(TAG, "onCreate: " + currSleepTime);
        cdt = new CountDownTimer(currSleepTime, 1000) {

            long timeSoFar = 0;

            @Override
            public void onTick(long millisUntilFinished) {

                timeSoFar += 1000;
                float result = Float.parseFloat(Integer.toString(((int) (timeSoFar * 100) / currSleepTime)));
                //Log.d(TAG, "onTick: " + result);
                if (result > 100) {
                } else
                    edv.setProgress(result);

            }

            @Override
            public void onFinish() {

                edv.setProgress(100);
                edv.setVisibility(View.INVISIBLE);
                //resetting the time so far
                timeSoFar = 0;

            }
        };
        //edv.setVisibility(View.INVISIBLE);
        edv.startIntro();

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
        Log.d(TAG, "onCreate: " + Constants.HELP);

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

                //preventing the CPU from turning off
                wl.acquire();

                //changing states of hardware here
                edv.setVisibility(View.VISIBLE);
                tvHelp.setVisibility(View.GONE);
                cdt.start();
                flashlight.isInitialise = true;
                backgroundAudio.isInitialise = true;
                int timeToSleep = Constants.SharedPrefsConstants.getValue("sleepTime", MainActivity.this);
                Toast.makeText(MainActivity.this, "Lock your phone in " + timeToSleep + " seconds", Toast.LENGTH_SHORT).show();
                waitTime.start();
                //pending work here //todo


            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                edv.setProgress(0);
                if (cdt != null) {
                    cdt.cancel();
                    cdt.onFinish();
                }

                if (wl.isHeld()) {
                    //releaing the Wake Up lock
                    wl.release();
                }

                edv.setVisibility(View.GONE);
                tvHelp.setVisibility(View.VISIBLE);

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

        proximitySensor = new MySensorManager(MainActivity.this, new MySensorManager.onWakeUp() {

            @Override
            public void setOnWakeUp() {

                //TODO screen turn on work to be done here HACK TO WORK ON

                //turning off proximity sensor
                Log.d(TAG, "setOnWakeUp: turning on the screen");
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
                proximitySensor.stopProximity();

                //turning screen ON
                PowerManager.WakeLock screenLock = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(
                        PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
                screenLock.acquire();
                screenLock.release();

            }
        });


        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "description");
        startActivityForResult(intent, 15);

        //used to turn screen OFF and start the proximity after the screen is turned OFF
        waitTime = new CountDownTimer(currSleepTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

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

                proximitySensor.startProximity(wl);
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
                startActivity(new Intent(
                        this,
                        SettingActivity.class
                ));
                break;
            /*case R.id.About:
                break;
            case R.id.feedback:
                break;*/

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
package com.example.saksham.pitpocketingapp;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * Created by saksham on 7/30/2017.
 */

public class BackgroundAudio {

    Context context;
    AudioManager am;
    static MediaPlayer mp;
    public static final String TAG = "BackgroundAudio";
    static boolean isInitialise = false;


    public BackgroundAudio(Context context) {

        this.context = context;
        am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mp = MediaPlayer.create(context, R.raw.music);
    }

    public void startAudio() {



            am.setStreamVolume(AudioManager.STREAM_MUSIC,
                    3, //am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
                    1);

            Log.d(TAG, "startAudio: else playing media player");

            mp.start();
            isInitialise = true;

    }

    public void stopAudio() {

        isInitialise = false;

        mp.stop();
        mp.release();
        mp = MediaPlayer.create(context, R.raw.music);
    }

    public static boolean isInitialised() {
        return isInitialise;
    }

    public void destroyInstance() {

        mp.release();

    }

}
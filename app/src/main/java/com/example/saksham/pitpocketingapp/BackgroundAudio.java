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
    MediaPlayer mp;
    public static final String TAG = "BackgroundAudio";
    boolean isInitialise = false;


    public BackgroundAudio(Context context) {

        this.context = context;
        am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

    }

    public void startAudio() {

        mp = MediaPlayer.create(context, R.raw.music);
        am.setStreamVolume(AudioManager.STREAM_MUSIC,
                3, //am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
                1);

        mp.start();
        isInitialise = true;
    }

    public void stopAudio() {

        isInitialise = false;
        mp.stop();
        mp.release();

    }

    public boolean isInitialised() {
        return isInitialise;
    }

}
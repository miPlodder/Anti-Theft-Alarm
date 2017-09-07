package com.example.saksham.pitpocketingapp.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.saksham.pitpocketingapp.R;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View easySplashScreenView = new EasySplashScreen(SplashActivity.this)
                .withFullScreen()
                .withTargetActivity(MainActivity.class)
                .withSplashTimeOut(5000)
                .withBackgroundResource(R.drawable.gradient_demo)
                /*.withHeaderText("Header")*/
                /*.withFooterText("Copyright 2017")*/
                /*.withBeforeLogoText("My cool company")*/
                //.withLogo(R.drawable.logo)
                /*.withAfterLogoText("Some more details")*/
                .create();

        setContentView(easySplashScreenView);
    }
}

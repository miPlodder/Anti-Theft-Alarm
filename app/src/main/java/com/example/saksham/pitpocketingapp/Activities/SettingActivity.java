package com.example.saksham.pitpocketingapp.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.saksham.pitpocketingapp.Fragments.SettingsFragment;
import com.example.saksham.pitpocketingapp.R;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_setting);

       getFragmentManager().beginTransaction()
                .replace(android.R.id.content,new SettingsFragment()).commit();

    }
}

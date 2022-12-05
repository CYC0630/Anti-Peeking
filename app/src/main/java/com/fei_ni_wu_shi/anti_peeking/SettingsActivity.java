package com.fei_ni_wu_shi.anti_peeking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

public class SettingsActivity extends AppCompatActivity
{

    @Override

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //------swicth and open detect face-------
        Button apModeSwitch = findViewById(R.id.apModeSwitch);
        apModeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this,FaceDetectActivity.class));
            }
        });

        Button insertImageButton = findViewById(R.id.insertImageButton);
        insertImageButton.setOnClickListener(view -> startActivity(new Intent(SettingsActivity.this, GalleryActivity.class)));
    }
}
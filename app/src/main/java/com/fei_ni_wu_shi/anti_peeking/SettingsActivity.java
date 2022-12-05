package com.fei_ni_wu_shi.anti_peeking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        FaceDetectActivity.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pokemon_go);

        //------switch and open detect face-------
        SwitchCompat apModeSwitch = findViewById(R.id.apModeSwitch);
        apModeSwitch.setOnCheckedChangeListener(((buttonView, isChecked) ->
        {
            if (isChecked)
                startActivity(new Intent(SettingsActivity.this, FaceDetectActivity.class));
        }));


        Button insertImageButton = findViewById(R.id.insertImageButton);
        insertImageButton.setOnClickListener(view -> startActivity(new Intent(SettingsActivity.this, GalleryActivity.class)));
    }
}
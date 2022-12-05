package com.fei_ni_wu_shi.anti_peeking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button insertImageButton = findViewById(R.id.insertImageButton);
        insertImageButton.setOnClickListener(view -> startActivity(new Intent(SettingsActivity.this, GalleryActivity.class)));
    }
}
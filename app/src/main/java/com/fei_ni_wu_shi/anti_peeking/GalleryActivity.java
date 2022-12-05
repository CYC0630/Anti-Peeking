package com.fei_ni_wu_shi.anti_peeking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class GalleryActivity extends AppCompatActivity
{
    //this is a comment
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
}
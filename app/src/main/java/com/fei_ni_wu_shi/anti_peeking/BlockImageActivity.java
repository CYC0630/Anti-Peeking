package com.fei_ni_wu_shi.anti_peeking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class BlockImageActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_image);

        ImageView blockImage = findViewById(R.id.blockImage);
        blockImage.setImageBitmap(FaceDetectActivity.bitmap);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
}
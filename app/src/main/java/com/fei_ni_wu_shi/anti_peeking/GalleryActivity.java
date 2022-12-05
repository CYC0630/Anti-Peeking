package com.fei_ni_wu_shi.anti_peeking;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity
{
    private final ArrayList<View> imageViewArrayList = new ArrayList<>();
    private ImageView selectedImage;
    private ImageView firstImage;
    private LinearLayout albumLayout;
    private final View.OnClickListener onClickListener = view ->
    {
        Drawable drawable = ((ImageView) view).getDrawable();
        selectedImage.setImageDrawable(drawable);
        FaceDetectActivity.bitmap = ((BitmapDrawable) drawable).getBitmap();
    };

    //this is a comment
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        albumLayout = findViewById(R.id.albumLayout);
        for (int i = 0; i < albumLayout.getChildCount(); i++)
            imageViewArrayList.add(albumLayout.getChildAt(i));

        firstImage = (ImageView) imageViewArrayList.get(0);
        selectedImage = findViewById(R.id.selectedImage);
        selectedImage.setImageDrawable(firstImage.getDrawable());
        for (View view : imageViewArrayList)
            view.setOnClickListener(onClickListener);

        ActivityResultLauncher<Intent> activityResultLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result ->
                {
                    if (result.getResultCode() == Activity.RESULT_OK)
                    {
                        Intent data = result.getData();
                        if (data != null)
                        {
                            ImageView galleryImage = new ImageView(GalleryActivity.this);
                            galleryImage.setImageURI(data.getData());
                            galleryImage.setLayoutParams(firstImage.getLayoutParams());
                            galleryImage.setOnClickListener(onClickListener);
                            albumLayout.addView(galleryImage);
                        }
                    }
                });

        findViewById(R.id.rightIconImage).setOnClickListener(view ->
                activityResultLauncher.launch(new Intent(Intent.ACTION_OPEN_DOCUMENT).addCategory(Intent.CATEGORY_OPENABLE).setType("image/*")));
    }
}
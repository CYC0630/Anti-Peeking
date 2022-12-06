package com.fei_ni_wu_shi.anti_peeking;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FaceDetectActivity extends AppCompatActivity
{
    static Bitmap bitmap;
    
    JavaCameraView javaCameraView;
    CascadeClassifier faceDetector;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_detect);

        new Thread(() ->
        {
            WebView webView = findViewById(R.id.webView);
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl("https://www.wikipedia.org/");

            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
        }).start();

        javaCameraView = findViewById(R.id.javaCameraView);
        javaCameraView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_FRONT);
        javaCameraView.setAlpha(0);

        if (!OpenCVLoader.initDebug())
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, baseCallback);
        else
            baseCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);

        javaCameraView.setCvCameraViewListener(new MyViewListener2());
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }

    private final BaseLoaderCallback baseCallback = new BaseLoaderCallback(this)
    {
        @Override
        public void onManagerConnected(int status)
        {
            if (status == LoaderCallbackInterface.SUCCESS)
            {
                File caseFile;
                InputStream is = getResources().openRawResource(R.raw.haarcascade_frontalface_default);
                File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                caseFile = new File(cascadeDir, "haarcascade_frontalface_default.xml");

                FileOutputStream fos;

                byte[] buffer = new byte[4096];
                int bytesRead ;

                while (true)
                {
                    try
                    {
                        fos = new FileOutputStream(caseFile);
                        if ((bytesRead = is.read(buffer)) == -1)
                            break;
                        fos.write(buffer, 0, bytesRead);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                        return;
                    }
                }
                try
                {
                    is.close();
                    fos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }


                faceDetector = new CascadeClassifier(caseFile.getAbsolutePath());
                if (faceDetector.empty())
                    faceDetector = null;
                else
                    if (!cascadeDir.delete())
                        System.out.println("Hi");
                javaCameraView.enableView();
            }
            else
                super.onManagerConnected(status);
        }
    };

    class MyViewListener2 implements MyCameraBridgeViewBase.CvCameraViewListener2
    {
        private Mat mRgba, mGrey;

        @Override
        public void onCameraViewStarted(int width, int height)
        {
            mRgba = new Mat();
            mGrey = new Mat();
        }

        @Override
        public void onCameraViewStopped()
        {
            mRgba.release();
            mGrey.release();
        }

        @Override
        public Mat onCameraFrame(MyCameraBridgeViewBase.CvCameraViewFrame inputFrame)
        {
            mRgba = inputFrame.rgba();
            mGrey = inputFrame.gray();

            //detect Face
            MatOfRect faceDetections = new MatOfRect();
            faceDetector.detectMultiScale(mRgba, faceDetections);
            //   int count=0;
            Rect[] faces = faceDetections.toArray();
            for (Rect rect : faces)
            {
                //     count++;
                //     if(count>1)
                Imgproc.rectangle(mRgba, new Point(rect.x, rect.y),
                        new Point(rect.x + rect.width, rect.y + rect.height),
                        new Scalar(255, 0, 0));
            }

            if (faces.length > 2)
                startActivity(new Intent(FaceDetectActivity.this, BlockImageActivity.class));

            Core.rotate(mRgba, mRgba, Core.ROTATE_90_COUNTERCLOCKWISE);
            Core.rotate(mGrey, mGrey, Core.ROTATE_90_COUNTERCLOCKWISE);
            Core.flip(mRgba, mRgba, 1);
            Core.flip(mGrey, mGrey, 1);

            return mRgba;

        }
    }
}


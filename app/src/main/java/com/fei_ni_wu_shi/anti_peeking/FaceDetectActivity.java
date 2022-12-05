package com.fei_ni_wu_shi.anti_peeking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
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

public class FaceDetectActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    JavaCameraView javaCameraView;
    File caseFile;
    CascadeClassifier faceDetector;
    private Mat mRgba,mGrey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_detect);
        javaCameraView = (JavaCameraView) findViewById(R.id.javaCameraView);

        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, baseCallback);
        } else {
            baseCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

        javaCameraView.setCvCameraViewListener(this);
    }
    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat();
        mGrey = new Mat();

    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
        mGrey.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        mGrey = inputFrame.gray();

        //detect Face
        MatOfRect facedetections = new MatOfRect();
        faceDetector.detectMultiScale(mRgba,facedetections);
        //   int count=0;
        for(Rect react: facedetections.toArray()){
            //     count++;
            //     if(count>1)
            Imgproc.rectangle(mRgba, new Point(react.x,react.y),
                    new Point(react.x + react.width, react.y + react.height),
                    new Scalar(255,0,0));
        }

        return mRgba;
    }

    private final BaseLoaderCallback baseCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            if (status == LoaderCallbackInterface.SUCCESS) {
                InputStream is = getResources().openRawResource(R.raw.haarcascade_frontalface_default);
                File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                caseFile = new File(cascadeDir, "haarcascade_frontalface_default.xml");

                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(caseFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                byte[] buffer = new byte[4096];
                int bytesRead = 0;

                while (true) {
                    try {
                        if ((bytesRead = is.read(buffer)) == -1) break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        assert fos != null;
                        fos.write(buffer, 0, bytesRead);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    is.close();
                    assert fos != null;
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                faceDetector = new CascadeClassifier(caseFile.getAbsolutePath());
                if (faceDetector.empty()) {
                    faceDetector = null;
                } else {
                    cascadeDir.delete();
                }
                javaCameraView.enableView();
            } else {
                super.onManagerConnected(status);
            }
        }
    };
}

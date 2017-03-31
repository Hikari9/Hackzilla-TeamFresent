package com.fresent.fresent.ai;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

import com.fresent.fresent.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FaceDetection extends ContextWrapper {

    public static final String CASCADE_CLASSIFIER = "haarcascade_frontalface_default.xml";
    private static final String TAG = "FACE_DETECTION";
    private float relativeFaceSize = 0.2f; // tweak this
    private int absoluteFaceSize = 0;

    public CascadeClassifier getViolaJonesClassifier() {
        return violaJonesClassifier;
    }

    private CascadeClassifier violaJonesClassifier;

    public FaceDetection(Context context) {
        super(context);
        loadClassifier();
    }

    public float getRelativeFaceSize() {
        return relativeFaceSize;
    }

    public int getAbsoluteFaceSize() {
        return absoluteFaceSize;
    }

    public void setRelativeFaceSize(float relativeFaceSize) {
        this.relativeFaceSize = relativeFaceSize;
    }

    public void setAbsoluteFaceSize(int absoluteFaceSize) {
        this.absoluteFaceSize = absoluteFaceSize;
    }

    public void loadClassifier() {
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, loaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it.");
             loaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    // async loader of opencv resources
    private BaseLoaderCallback loaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    try {
                        // load cascade file from application resources

                        // load classifier
                        InputStream is = getResources().openRawResource(R.raw.haarcascade_frontalface_default);
                        File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                        File classifierFile = new File(cascadeDir, CASCADE_CLASSIFIER);
                        FileOutputStream os = new FileOutputStream(classifierFile);

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                        is.close();
                        os.close();

                        violaJonesClassifier = new CascadeClassifier(classifierFile.getAbsolutePath());
                        if (violaJonesClassifier.empty()) {
                            Log.e(TAG, "Failed to load cascade classifier");
                            violaJonesClassifier = null;
                        } else
                            Log.i(TAG, "Loaded cascade classifier from " + classifierFile.getAbsolutePath());

                        cascadeDir.delete();

                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Failed to load cascade. Exception thrown: " + e);
                    }
                    break;
                }
                default: {
                    super.onManagerConnected(status);
                    break;
                }
            }
        }
    };


}

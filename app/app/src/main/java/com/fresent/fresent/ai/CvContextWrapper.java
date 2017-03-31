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

public abstract class CvContextWrapper extends ContextWrapper {

    public static final String TAG = "CV_CONTEXT";

    public CvContextWrapper(Context base) {
        super(base);
    }

    public void loadOpenCV() {
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, loaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it.");
            loaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    protected File loadRawResource(int resourceId, String fileName) {
        InputStream is = getResources().openRawResource(resourceId);
        File dir = getDir("tmp", Context.MODE_PRIVATE);
        File classifierFile = new File(dir,fileName);
        try {
            FileOutputStream os = new FileOutputStream(classifierFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();
        } catch (IOException ignore) {}
        return classifierFile;
    }

    protected void cleanUpResources() {
        File dir = getDir("tmp", Context.MODE_PRIVATE);
        dir.delete();
    }

    protected abstract void onLoad();

    // async loader of opencv resources
    private BaseLoaderCallback loaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    onLoad();
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

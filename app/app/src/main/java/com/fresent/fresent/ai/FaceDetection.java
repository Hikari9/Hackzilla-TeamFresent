package com.fresent.fresent.ai;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.media.Image;
import android.util.Log;

import com.fresent.fresent.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FaceDetection extends CvContextWrapper {

    public static final String CASCADE_CLASSIFIER = "haarcascade_frontalface_default.xml";
    private static final String TAG = "FACE_DETECTION";
    private float relativeFaceSize = 0.02f; // tweak this
    private int absoluteFaceSize = 0;

    public double getScaleFactor() {
        return scaleFactor;
    }

    public void setScaleFactor(double scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    public int getMinNeighbors() {
        return minNeighbors;
    }

    public void setMinNeighbors(int minNeighbors) {
        this.minNeighbors = minNeighbors;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    private double scaleFactor = 1.1;
    private int minNeighbors = 2;
    private int flags = 2;

    public CascadeClassifier getViolaJonesClassifier() {
        return violaJonesClassifier;
    }

    private CascadeClassifier violaJonesClassifier;

    public FaceDetection(Context context) {
        super(context);
    }

    public float getRelativeFaceSize() {
        return relativeFaceSize;
    }

    public int getAbsoluteFaceSize() {
        return absoluteFaceSize;
    }

    public Size getMinSize() {
        return new Size(absoluteFaceSize, absoluteFaceSize);
    }

    public void setRelativeFaceSize(float relativeFaceSize) {
        this.relativeFaceSize = relativeFaceSize;
    }

    public void setAbsoluteFaceSize(int absoluteFaceSize) {
        this.absoluteFaceSize = absoluteFaceSize;
    }

    public Mat convertBitmapToMat(Bitmap image) {
        Bitmap image32 = image.copy(Bitmap.Config.RGB_565, true);
        Mat mat = new Mat();
        Utils.bitmapToMat(image32, mat);
        return mat;
    }

    public Rect[] detectFaces(Bitmap image) {
        return detectFaces(convertBitmapToMat(image));
    }

    // detects all face rectangles from Mat image
    public Rect[] detectFaces(Mat image) {
        Mat gray = new Mat();
        Mat mat = new Mat();
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_RGB2GRAY);
        Imgproc.equalizeHist(gray, mat);
        if (getAbsoluteFaceSize() == 0) {
            int height = mat.rows();
            if (Math.round(height * getRelativeFaceSize()) > 0) {
                setAbsoluteFaceSize(Math.round(height * relativeFaceSize));
            }
        }
        MatOfRect faces = new MatOfRect();
        if (getViolaJonesClassifier() != null)
            getViolaJonesClassifier()
                .detectMultiScale(mat, faces, getScaleFactor(),
                    getMinNeighbors(), getFlags(), getMinSize(), new Size());
        else
            Log.e(TAG, "Viola-jones classifier is not loaded!");
        return faces.toArray();
    }

    protected void onLoad() {
        File classifierFile = this.loadRawResource(R.raw.haarcascade_frontalface_default,
                                                   "haarcascade_frontalface_default.xml");
        String absolutePath = classifierFile.getAbsolutePath();
        violaJonesClassifier = new CascadeClassifier(absolutePath);
        violaJonesClassifier.load(absolutePath); // need to reload because empty bug
        if (violaJonesClassifier.empty()) {
            Log.e(TAG, "Failed to load cascade classifier");
            violaJonesClassifier = null;
        } else
            Log.i(TAG, "Loaded cascade classifier from " + classifierFile.getAbsolutePath());
        cleanUpResources();
    }


}

package com.fresent.fresent.ai;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Predicts face label based on XML file classifiers.
 * For proof of concept, we implement a linear search of classifiers from the resources
 */
public class FaceRecognition extends CvContextWrapper {


    public FaceRecognition(Context base) {
        super(base);
    }

    @Override
    protected void onLoad() {
        System.loadLibrary("native-lib");
    }

    /**
     * Saves an image to given path
     *
     * @param image
     * @param path
     * @return
     */
    public File saveImage(Bitmap image, String path) {
        FileOutputStream outputStream;
        File file = new File(path);
        try {
            if (!file.exists())
                file.createNewFile();
            outputStream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Matches classifier using local binary patterns histogram.
     *
     * @param classifier
     * @param image
     * @return
     */
    public boolean matchesClassifier(File classifier, Bitmap image) {
        File imageFile = saveImage(image, "test.jpg");
        return new NativeFaceRecognition().nativeMatchesClassifier(classifier.getAbsolutePath(), imageFile.getAbsolutePath());
    }


}

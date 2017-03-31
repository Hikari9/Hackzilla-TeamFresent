package com.fresent.fresent.ai;

import android.content.Context;
import android.content.ContextWrapper;

/**
 * Predicts face label based on XML file classifiers.
 * For proof of concept, we implement a linear search of classifiers from the resources
 */
public class FaceRecognition extends ContextWrapper {

    public FaceRecognition(Context base) {
        super(base);
    }

}

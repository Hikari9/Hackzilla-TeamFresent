#include <jni.h>
#include <string>
#include <iostream>

/* NAMESPACE TEMPLATE */

namespace face_recognition {
    bool match_classifier(const char *file, const char *image);
}

/* NATIVE INTERFACE WITH JAVA */

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_fresent_fresent_ai_FaceRecognition_nativeMatchesClassifier(JNIEnv *env, jobject instance,
                                                                    jstring filePath_,
                                                                    jstring imagePath_) {
    const char *filePath = env->GetStringUTFChars(filePath_, 0);
    const char *imagePath = env->GetStringUTFChars(imagePath_, 0);

    bool res = face_recognition::match_classifier(filePath, imagePath);

    env->ReleaseStringUTFChars(filePath_, filePath);
    env->ReleaseStringUTFChars(imagePath_, imagePath);

    return (jboolean) res;
}

/* NAMESPACE IMPLEMENTATION */

namespace face_recognition {
    /**
     *
     * @param file
     * @param image
     */
    bool match_classifier(const char *file, const char *image) {
        return false;
    }
}
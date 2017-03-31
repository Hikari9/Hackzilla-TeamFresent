#include <jni.h>

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_fresent_fresent_ai_NativeFaceRecognition_nativeMatchesClassifier (JNIEnv * env, jobject instance, jstring classifierPath, jstring imagePath) {
    return (jboolean) false;
}




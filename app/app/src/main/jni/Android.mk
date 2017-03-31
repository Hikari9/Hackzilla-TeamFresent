LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

#opencv
OPENCVROOT:=/home/rico/OpenCV-android-sdk
OPENCV_CAMERA_MODULES:=on
OPENCV_INSTALL_MODULES:=on
OPENCV_LIB_TYPE:=SHARED
include ${OPENCVROOT}/sdk/native/jni/OpenCV.mk

LOCAL_SRC_FILES := com_fresent_fresent_ai_NativeFaceRecognition.cpp
LOCAL_LDLIBS += -llog -ldl
LOCAL_MODULE := FaceRecognitionLibrary

include $(BUILD_SHARED_LIBRARY)
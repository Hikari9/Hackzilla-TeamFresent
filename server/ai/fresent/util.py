import cv2, cv2.face
import os, os.path
import sys
import numpy as np

CASCADE_PATH = 'data/haarcascade_frontalface_default.xml'

SIZE = 160
MARGIN = 8

def load_grayscale_image(image_file,
                         equalize_hist=True,
                         median_blur=3):

    if not isinstance(image_file, str):
        return image_file

    raw_image = cv2.imread(image_file)
    gray_image = cv2.cvtColor(raw_image, cv2.COLOR_BGR2GRAY)
    image = gray_image

    if equalize_hist:
        test = cv2.equalizeHist(image)

    if median_blur:
        image = cv2.medianBlur(image, median_blur)

    return image

__VIOLA_JONES_CASCADER = None

def viola_jones_detector(cascade_path=CASCADE_PATH):
    global __VIOLA_JONES_CASCADER

    if __VIOLA_JONES_CASCADER is None:

        # load detector instance dynamically
        if not os.path.exists(cascade_path):
            # try relative to the server/ai folder
            file_dir = os.path.dirname(os.path.abspath(__file__))
            cascade_path = os.path.join(file_dir, '..', cascade_path)

        __VIOLA_JONES_CASCADER = cv2.CascadeClassifier(cascade_path)

    return __VIOLA_JONES_CASCADER

def get_roi_resized_rect(image, roi):
    image = load_grayscale_image(image)
    x, y, w, h = roi
    mx = int(round(MARGIN*w/(SIZE-2*MARGIN)))
    my = int(round(MARGIN*h/(SIZE-2*MARGIN)))
    Y, H, X, W = (max(0, y-my), min(image.shape[0], y+h+my)-y, max(0, x-mx), min(image.shape[1], x+w+mx)-x)
    return X, Y, W, H

def get_resized_image(image, roi):
    x, y, w, h = roi
    img = image[y : y + h, x : x + w]
    return cv2.resize(img, (SIZE, SIZE))

def detect_faces(image, scaleFactor=1.1, minNeighbors=4, **kwargs):
    image = load_grayscale_image(image)
    faces = viola_jones_detector(**kwargs).detectMultiScale(image, scaleFactor=scaleFactor, minNeighbors=minNeighbors, **kwargs)
    return [get_roi_resized_rect(image, face) for face in faces]

# function that gets images with respective labels from a given folder
def get_images_and_labels(folder,
                          debug=False,
                          debug_faces=False,
                          **kwargs):

    labels = []
    images = []

    for label in os.listdir(folder):

        if debug: print(label)
        path = os.path.join(folder, label)

        if os.path.isdir(path):

            for filename in os.listdir(path):

                image_path = os.path.join(path, filename)
                image = load_grayscale_image(image_path)
                faces = detect_faces(image)
                if len(faces) == 0:
                    image = cv2.resize(image, (SIZE, SIZE))
                    images.append(image)
                    labels.append(label)
                    if debug_faces:
                        cv2.imshow('Adding faces to training set...', image)
                        cv2.waitKey(50)
                else:
                    for roi in faces[:1]:
                        x, y, w, h = roi
                        imageROI = get_resized_image(image, roi)
                        images.append(imageROI)
                        labels.append(label)
                        if debug_faces:
                            cv2.imshow('Adding faces to training set...', imageROI)
                            cv2.waitKey(50)

                if debug: print('\t' + filename + (' [%d face]' % ok))

        if debug: print('[Added %d images]' % added)

    if debug_faces:
        cv2.destroyAllWindows()

    return images, labels

import cv2, cv2.face
import os, os.path
import sys
import numpy as np

CASCADE_PATH = 'data/haarcascade_frontalface_default.xml'

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

def detect_faces(image, scaleFactor=1.1, minNeighbors=2, **kwargs):
    image = load_grayscale_image(image)
    return viola_jones_detector(**kwargs).detectMultiScale(image, **kwargs)

# function that gets images with respective labels from a given folder
def get_images_and_labels(folder,
                          cascader=None,
                          cascader_args={},
                          debug=False,
                          debug_faces=False,
                          debug_accuracy=False,
                          **kwargs):

    labels = []
    images = []
    added = 0
    expected = 0

    for label in os.listdir(folder):

        if debug: print(label)
        path = os.path.join(folder, label)

        if os.path.isdir(path):

            for filename in os.listdir(path):

                ok = 0
                expected += 1

                try:

                    image_path = os.path.join(path, filename)
                    image = load_grayscale_image(image_path, **kwargs)

                    if cascader is not None:
                        # get the face using Viola-Jones detector
                        faces = cascader.detectMultiScale(image, **cascader_args)
                        for (x, y, w, h) in faces:
                            images.append(image[y:y+h, x:x+w])
                            labels.append(label)
                            if debug_faces:
                                cv2.imshow('Adding faces to training set...', image[y:y+h, x:x+w])
                                cv2.waitKey(50)
                        ok += len(faces)
                        if len(faces) > 0:
                            added += 1
                    else:
                        images.append(image)
                        labels.append(label)
                        ok = 1
                        added += 1

                except Exception as e:
                    print(e)
                    pass

                if debug: print('\t' + filename + (' [%d face]' % ok))

        if debug: print('[Added %d images]' % added)

    if debug_faces:
        cv2.destroyAllWindows()

    if debug_accuracy:
        print('Accuracy: %.5f%%' % (100 * added / max(1, expected)))

    return images, labels

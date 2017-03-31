import cv2, cv2.face
import os, os.path
import sys
import numpy as np
import train
from util import get_images_and_labels

# Returns a boolean if an image matches with classifier, attach with confidence
# -> is_match, confidence
def match_classifier(classifer_file,
                     image,
                     algorithm=train.ALGO_LOCAL_BINARY_PATTERNS):

    # create recognizer
    FaceRecognizer = algorithm
    recognizer = FaceRecognizer()

    # load recognizer from classifier file
    recognizer.load(classifer_file)

    # perform prediction and confidence
    label, confidence = recognizer.predict(image)
    label = True if label == 1 else False

    return label, confidence

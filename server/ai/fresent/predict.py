import cv2, cv2.face
import os, os.path
import sys
import numpy as np
import fresent.train as train
import fresent.util as util

# Match a face with a group of classifiers using LBPH algorithm
# (image, classifier_folder [, algorithm]) -> [(distance, label string) in ascending order]
def predict_face(image, classifier_folder, algorithm=train.ALGO_LOCAL_BINARY_PATTERNS):
    FaceRecognizer = algorithm

    # get the label with the min distance (i.e. max confidence)
    results = []

    if not os.path.exists(classifier_folder):
        # try relative to server/ai folder
        ai = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
        classifier_folder = os.path.join(ai, classifier_folder)

    for classifier_filename in os.listdir(classifier_folder):
        classifier = os.path.join(classifier_folder, classifier_filename)
        try:
            recognizer = FaceRecognizer()
            recognizer.load(classifier)
            label, distance = recognizer.predict(image)
            if label != -1:
                label_string = recognizer.getLabelInfo(label)
                results.append((distance, label_string))
        except:
            pass

    results.sort()
    return results

# Gets all region of interest in the given image, and assigns it a unique label
# Greedy assignment of images with by minimizing distance
# (image, classifier_folder [, algorithm]) -> {label: (x, y, w, h) ... }
def predict_all_faces(image, classifier_folder, algorithm=train.ALGO_LOCAL_BINARY_PATTERNS):

    faces = util.detect_faces(image)
    heap = []
    index = 0

    for (x, y, w, h) in faces:

        imageROI = image[y:y+h, x:x+w]
        predictions = predict_face(imageROI, classifier_folder, algorithm)

        for distance, label_string in predictions:
            heap.append((distance, label_string, index))

        index += 1

    heap.sort()
    need = len(faces)
    vis = [False] * len(faces)
    assignment = {}

    for distance, label, index in heap:
        if not vis[index] and label not in assignment:
            # assign ROI rectangle to label_string
            assignment[label] = faces[index]
            vis[index] = True

    return assignment

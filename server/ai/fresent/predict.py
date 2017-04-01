import cv2, cv2.face
import os, os.path
import sys
import numpy as np
import fresent.train as train
import fresent.util as util

# Match a face with a group of classifiers using LBPH algorithm
# (image, classifier_path [, algorithm]) -> [(distance, label string) in ascending order]
def predict_face(image, classifier_path, algorithm=train.ALGO_DEFAULT, preprocess=True):

    if preprocess:
        image = util.load_grayscale_image(image)
        image = util.get_resized_image(image)

    FaceRecognizer = algorithm

    # get the label with the min distance (i.e. max confidence)
    results = []

    if not os.path.exists(classifier_path):
        # try relative to server/ai folder
        ai = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
        classifier_path = os.path.join(ai, classifier_path)

    if os.path.isfile(classifier_path):
        # predict with one big classifier
        recognizer = FaceRecognizer()
        recognizer.load(classifier_path)
        label, distance = recognizer.predict(image)
        if label != -1:
            return (recognizer.getLabelInfo(label), distance)

    elif os.path.isdir(classifier_path):
        # predict with many classifiers
        for classifier_filename in os.listdir(classifier_path):
            classifier = os.path.join(classifier_path, classifier_filename)
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

    return None

# Gets all region of interest in the given image, and assigns it a unique label
# Greedy assignment of images with by minimizing distance
# (image, classifier_path [, algorithm]) -> {label: (x, y, w, h) ... }
def predict_all_faces(image, classifier_path,
                      algorithm=train.ALGO_DEFAULT):

    image = util.load_grayscale_image(image)
    faces = util.detect_faces(image)
    heap = []
    index = 0
    for roi in faces:
        imageROI = util.get_resized_image(image, roi)
        predictions = predict_face(imageROI, classifier_path, algorithm, preprocess=False)

        if predictions is None:
            continue

        if not isinstance(predictions, list):
            heap.append((predictions[1], predictions[0], index))

        else:
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
            assignment[label] = (faces[index], distance)
            vis[index] = True

    return assignment

import cv2, cv2.face
import os, os.path
import sys
import numpy as np
import train
import .util
from heapq import heapify, heappop

# Match a face with a group of classifiers using LBPH algorithm
# (image, classifier_list [, algorithm]) -> [(distance, label string) in ascending order]
def predict_face(image, classifier_list, algorithm=train.ALGO_LOCAL_BINARY_PATTERNS):
    FaceRecognizer = algorithm

    # get the label with the min distance (i.e. max confidence)
    results = []

    for classifier in classifier_list:
        recognizer = FaceRecognizer()
        recognizer.load(classifier)
        label, distance = recognizer.predict(image)
        if label != -1:
            label_string = recognizer.getLabelInfo(label)
            results.append((distance, label_string))

    results.sort()
    return results

# Gets all region of interest in the given image, and assigns it a unique label
# Greedy assignment of images with by minimizing distance
# (image, classifier_list [, algorithm]) -> {label: (x, y, w, h) ... }
def predict_all_faces(image, classifier_list, algorithm=train.ALGO_LOCAL_BINARY_PATTERNS):

    faces = util.detect_faces(image_file)
    heap = []
    index = 0

    for (x, y, w, h) in faces:

        imageROI = image[y:y+h, x:x+w]
        predictions = predict_face(imageROI, classifier_list, algorithm)

        for distance, label_string in predictions:
            heap.append((distance, label_string, index))
        index += 1

    heapify(heap)
    need = len(faces)
    vis = [False] * len(faces)
    assignment = {}

    while len(heap) > 0:

        distance, label, index = heappop(heap)
        if not vis[index] and label not in assignment:
            # assign ROI rectangle to label_string
            assignment[label] = faces[index]

    return assignment

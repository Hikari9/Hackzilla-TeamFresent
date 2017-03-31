import cv2, cv2.face
import os, os.path
import sys
import numpy as np
from util import get_images_and_labels

# constants
CASCADE_NAME = 'haarcascade_frontalface_default.xml'
ALGO_LOCAL_BINARY_PATTERNS = cv2.face.createLBPHFaceRecognizer
ALGO_EIGEN = cv2.face.createEigenFaceRecognizer
ALGO_FISHER = cv2.face.createFisherFaceRecognizer

# perform training and generate classifiers
def generate_classifiers(training_folder,
                         classifier_folder,
                         cascade_folder,
                         algorithm=ALGO_LOCAL_BINARY_PATTERNS):

    # Check if classifier folder exists
    if not os.path.exists(classifier_folder):
        print('Creating folder `%s`' % classifier_folder)
        os.makedirs(classifier_folder)

    # Viola-Jones classifier for Haar feature extraction
    cascade_path = os.path.join(cascade_folder, CASCADE_NAME)
    cascader = cv2.CascadeClassifier(cascade_path)
    assert(not cascader.empty())

    # Setup cascader args
    cascader_args = {
        'scaleFactor': 1.1,
        'minNeighbors': 2,
        'flags': 2
    }

    # Get images and labels
    images, labels = get_images_and_labels(training_folder,
                                           cascader=cascader,
                                           detect_faces=algorithm is ALGO_LOCAL_BINARY_PATTERNS,
                                           cascader_args=cascader_args,
                                           debug_accuracy=True)

    print('Collected %d images [%d labels]' % (len(images), len(set(labels))))

    FaceRecognizer = algorithm

    label_dict = {label: 0 for label in labels}
    index = 2
    for label in label_dict:
        label_dict[label] = index
        index += 1

    for label in label_dict:

        recognizer = FaceRecognizer()

        # create a binary recognizer per image
        # current complexity: O(n^2)
        # TODO: optimize this and integrate to TensorFlow
        # recognizer.train(images, np.array([1 if label == cur_label else 0 for image, cur_label in zip(images, labels)]))
        recognizer.train(images, np.array([
            1 if label == cur_label else label_dict[label]
            for cur_label in labels]))

        # Save classifier to text file
        file_name = label + '.xml'
        file_path = os.path.join(classifier_folder, file_name)

        recognizer.save(file_path)
        print('Saved classifier ' + os.path.abspath(file_path))

if __name__ == '__main__':
    args = sys.argv
    if len(args) < 3:
        print('Usage: python %s <training_folder> <classifier_folder> [<cascade_folder>]' % args[0])
    else:
        generate_classifiers(args[1], args[2], '../data/' if len(args) == 3 else args[3])

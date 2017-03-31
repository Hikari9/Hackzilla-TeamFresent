import cv2, cv2.face
import os, os.path
import sys
import numpy as np
from .util import get_images_and_labels

# constants
CASCADE_NAME = 'haarcascade_frontalface_default.xml'
ALGO_LOCAL_BINARY_PATTERNS = cv2.face.createLBPHFaceRecognizer
ALGO_EIGEN = cv2.face.createEigenFaceRecognizer
ALGO_FISHER = cv2.face.createFisherFaceRecognizer

# perform training and generate classifiers
def generate_classifiers(training_folder,
                         classifier_folder,
                         cascade_folder=None,
                         algorithm=ALGO_LOCAL_BINARY_PATTERNS):

    # Check if classifier folder exists
    if not os.path.exists(classifier_folder):
        print('Creating folder `%s`' % classifier_folder)
        os.makedirs(classifier_folder)

    # Viola-Jones classifier for Haar feature extraction
    cascader = None
    if cascade_folder is not None:
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
                                           cascader_args=cascader_args,
                                           debug_accuracy=True)

    print('Collected %d images [%d labels]' % (len(images), len(set(labels))))

    FaceRecognizer = algorithm

    label_dict = {}
    for label in labels:
        if label not in label_dict:
            label_dict[label] = len(label_dict)

    # label id -> list of images
    label_images = []
    label_labels = []
    for image, label in zip(images, labels):
        label_id = label_dict[label]
        while label_id >= len(label_images):
            label_images.append([])
        while label_id >= len(label_labels):
            label_labels.append(None)
        label_images[label_id].append(image)
        label_labels[label_id] = label

    # create a recognizer for each label
    for label_id, image_list in enumerate(label_images):
        label = label_labels[label_id]
        # train recognizer
        recognizer = FaceRecognizer()
        recognizer.train(image_list, np.array([label_id] * len(image_list)))

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

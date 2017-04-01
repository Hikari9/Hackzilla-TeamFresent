import cv2, cv2.face
import os, os.path
import sys
import numpy as np
from fresent.util import get_images_and_labels

# constants
ALGO_LOCAL_BINARY_PATTERNS = cv2.face.createLBPHFaceRecognizer
ALGO_EIGEN = cv2.face.createEigenFaceRecognizer
ALGO_FISHER = cv2.face.createFisherFaceRecognizer
ALGO_DEFAULT = ALGO_LOCAL_BINARY_PATTERNS

# perform training and generate classifiers
def generate_classifiers(training_folder,
                         classifier_path,
                         one_big_classifier=True,
                         algorithm=ALGO_DEFAULT):

    # Get images and labels
    images, labels = get_images_and_labels(training_folder)

    print('Collected %d images [%d labels]' % (len(images), len(set(labels))))

    FaceRecognizer = algorithm

    label_dict = {}
    for label in labels:
        if label not in label_dict:
            label_dict[label] = len(label_dict)

    if one_big_classifier:
        # create a main recognizer for all labels
        label_ids = [label_dict[label] for label in labels]
        recognizer = FaceRecognizer()
        recognizer.train(images, np.array(label_ids))
        for label, id in label_dict.items():
            recognizer.setLabelInfo(id, label)

        if not os.path.exists(os.path.dirname(classifier_path)):
            os.makedirs(os.path.dirname(classifier_path))

        print(classifier_path)
        recognizer.save(classifier_path)
        print('Saved main classifier ' + os.path.abspath(classifier_path))

    else:

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

            # set label info for given string
            recognizer.setLabelInfo(label_id, label)

            # Save classifier to text file
            file_name = label + '.xml'
            file_path = os.path.join(classifier_path, file_name)

            recognizer.save(file_path)
            print('Saved classifier ' + os.path.abspath(file_path))

if __name__ == '__main__':
    args = sys.argv
    if len(args) < 3:
        print('Usage: python %s <training_folder> <classifier_path>' % args[0])
    else:
        if args[2].endswith('.xml'):
            generate_classifiers(args[1], args[2], one_big_classifier=True)

        else:
            generate_classifiers(args[1], args[2], one_big_classifier=False)

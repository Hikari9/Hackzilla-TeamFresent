# perform training

import cv2, cv2.face
import os, os.path
import sys
import numpy as np

# constants
CASCADE_NAME = 'haarcascade_frontalface_default.xml'

def generate_classifiers(training_folder, classifier_folder, cascade_folder):

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
                                           cascader_args=cascader_args,
                                           debug_accuracy=True)

    print('Collected %d images [%d labels]' % (len(images), len(set(labels))))

    for label in set(labels):
        recognizer = cv2.face.createLBPHFaceRecognizer()

        # create a binary recognizer per image
        # current complexity: O(n^2)
        # TODO: optimize this and integrate to TensorFlow
        recognizer.train(images, np.array([1 if label == cur_label else 0 for image, cur_label in zip(images, labels)]))

        # Save classifier to text file
        file_name = label + '.xml'
        file_path = os.path.join(classifier_folder, file_name)

        recognizer.save(file_path)
        print('Saved classifier ' + os.path.abspath(file_path))


# function that gets images with respective labels from a given folder
def get_images_and_labels(folder,
                          cascader=None,
                          cascader_args={},
                          detect_faces=True,
                          median_blur=3,
                          equalize_hist=True,
                          debug=False,
                          debug_faces=False,
                          debug_accuracy=True):

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

                    #image_pil = Image.open(image_path).convert('L')
                    #image = np.array(image_pil, 'uint8')
                    #cv2.equalizeHist(image, image)

                    # get grayscale image
                    # print(os.path.abspath(image_path))
                    raw_image = cv2.imread(image_path)
                    cv2.imshow("HI", raw_image)
                    gray_image = cv2.cvtColor(raw_image, cv2.COLOR_BGR2GRAY)
                    image = gray_image

                    if equalize_hist:
                        image = cv2.equalizeHist(gray_image)
                    if median_blur:
                        image = cv2.medianBlur(image, median_blur)

                    if detect_faces:
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


if __name__ == '__main__':
    args = sys.argv
    if len(args) < 3:
        print('Usage: python %s <training_folder> <classifier_folder> [<cascade_folder>]' % args[0])
    else:
        generate_classifiers(args[1], args[2], '../data/' if len(args) == 3 else args[3])

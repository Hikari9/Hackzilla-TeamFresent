import fresent.train as training

TRAINING_FOLDER = 'data/progvar_aligned'
CLASSIFIER_FOLDER = 'classifiers'

def main():
    print('Generating classifiers...')
    training.generate_classifiers(TRAINING_FOLDER,
                                  CLASSIFIER_FOLDER,
                                  algorithm=training.ALGO_LOCAL_BINARY_PATTERNS)

if __name__ == '__main__':
    main()

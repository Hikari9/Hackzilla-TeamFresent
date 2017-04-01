import fresent.train as training

TRAINING_FOLDER = 'data/progvar_aligned'
CLASSIFIER_PATH = 'models/model.xml'

def main():
    print('Generating classifiers...')
    training.generate_classifiers(TRAINING_FOLDER,
                                  CLASSIFIER_PATH,
                                  one_big_classifier=True,
                                  algorithm=training.ALGO_DEFAULT)

if __name__ == '__main__':
    main()

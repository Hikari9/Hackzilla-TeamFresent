import os, os.path
import shutil
# import tensorflow as tf
# import facenet
# import facenet.align.align_dataset_mtcnn as MTCNN

RAW_DATASET = 'data/progvar_raw'
OUTPUT_DATASET = 'data/progvar_aligned'

def main():
    dir_path = os.path.dirname(os.path.realpath(__file__))

    # clean output dataset
    raw_path = os.path.join(dir_path, RAW_DATASET)
    output_path = os.path.join(dir_path, OUTPUT_DATASET)

    if os.path.exists(output_path):
        print('Cleaning ' + output_path)
        shutil.rmtree(output_path)

    # generate aligned images
    facenet_align = os.path.join('facenet/align/align_dataset_mtcnn.py')
    args = ['python', facenet_align, raw_path, output_path,
        '--gpu_memory_fraction', '0.68']
    os.system(' '.join(args))
    # MTCNN.main(MTCNN.parse_arguments(args))

if __name__ == '__main__':
    main()

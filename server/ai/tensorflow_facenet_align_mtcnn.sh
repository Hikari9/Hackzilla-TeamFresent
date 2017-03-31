#!/bin/sh
rm -rf data/progvar_aligned
python facenet/align/align_dataset_mtcnn.py \
    data/progvar_raw \
    data/progvar_aligned \
    --gpu_memory_fraction 0.68

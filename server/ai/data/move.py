import os, sys, os.path


for dd in os.listdir('.'):
    if os.path.isdir(os.path.join(dd)):
        index = 1
        for d in os.listdir(dd):
            os.rename(os.path.join(dd, d), os.path.join(dd, str(index) + '.jpg'))
            index += 1

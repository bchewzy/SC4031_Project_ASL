# Import Libraries
import os
import string
import random
#import keras
import numpy as np
import pandas as pd
import glob

# Models
from sklearn.model_selection import train_test_split

# Directories for Train and Test Data
TRAIN_DIR = "./asl_alphabet/asl_alphabet_train/"
TEST_DIR = "./asl_alphabet/asl_alphabet_test/"

# Get Labels
labels = []
for folder in os.listdir(TRAIN_DIR):
    labels.append(folder)
    
print(labels)

# Import Training Data
image_list = []
label_list = []
for label in labels:
    image_paths = glob.glob(os.path.join(TRAIN_DIR, label, "*"))        # E.g. ../asl_alphabet_train/A/*
    image_labels = [label] * len(image_paths)
    
    image_list.extend(image_paths)
    label_list.extend(image_labels)

dataset = pd.DataFrame({
    "label": label_list,
    "image": image_list
})
    
print(dataset)

# Split Training Data
X_train, X_test, y_train, y_test = train_test_split(
    dataset['image'],
    dataset['label'],
    test_size=0.25,
    shuffle=True
)

data_train = pd.DataFrame({
    "label": y_train,
    "image": X_train
})

print(data_train)
# Import Libraries
import os
import glob
import string
import skimage as ski
import numpy as np
import pandas as pd
import seaborn as sns
#import keras

'''
from tensorflow.keras.applications import VGG16
from tensorflow.keras.layers import Dense, Flatten, Dropout
from keras.models import load_model, Model
from keras.optimizers import Adam
from keras.callbacks import ModelCheckpoint, EarlyStopping
'''

# Models
from sklearn.model_selection import train_test_split

# Directories for Train and Test Data
TRAIN_DIR = "./asl_alphabet/asl_alphabet_train/"
TEST_DIR = "./asl_alphabet/asl_alphabet_test/"

IMG_SIZE = 64

# Get Labels
labels = []
for folder in os.listdir(TRAIN_DIR):
    labels.append(folder)
    
print(labels)

# Import Training Data
image_list = []
label_list = []

for label in labels:
    label_dir = os.listdir(TRAIN_DIR + label)
    
    for img_file in label_dir:
        img = ski.io.imread(TRAIN_DIR + label + "/" + img_file)
        
        if img is not None:
            img = ski.transform.resize(img, (IMG_SIZE, IMG_SIZE, 3))
    # Append label list
    #label_list.extend(image_labels)
    
    # image_paths = glob.glob(os.path.join(TRAIN_DIR, label, "*"))        # E.g. ../asl_alphabet_train/A/*
    # image_labels = [label] * len(image_paths)
    # image_list.extend(image_paths)
    
    # Transform images and append to image list
    
    
'''
dataset = pd.DataFrame({
    "label": label_list,
    "image": image_list
})

# Split Dataset to Training and Test 
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

data_train

base_model = VGG16(weights='imagenet', include_top=False, input_shape=(IMG_SIZE, IMG_SIZE, 3))

for layer in base_model.layers:
    layer.trainable = False
    
x = base_model.output
x = Flatten()(x)
x = Dense(512, activation='relu')(x)
x = Dropout(0.5)(x)
x = Dense(512, activation='relu')(x)
x = Dropout(0.5)(x)
predictions = Dense(29, activation='softmax')(x)

model = Model(inputs=base_model.input, outputs=predictions)
print(model.summary())

# Compile and train the model
model.compile(optimizer=Adam(learning_rate=0.001), loss='categorical_crossentropy', metrics=['accuracy'])

# Callbacks
checkpoint = ModelCheckpoint('asl_vgg16_best_weights.h5', save_best_only=True, monitor='val_accuracy', mode='max')

# Train the Model
history = model.fit(X_train, y_train, epochs=10, validation_data=(X_test, y_test), verbose=1, callbacks=[checkpoint])

score = model.evaludate(X_test, y_test, verbose=0)
print('\nKeras CNN - accuracy:', score[1], '\n')

# history = model.fit(
#     train_generator,
#     steps_per_epoch=train_generator.samples // CFG.batch_size,
#     epochs=CFG.epochs,
#     validation_data=validation_generator,
#     validation_steps=validation_generator.samples // CFG.batch_size,
#     callbacks=[checkpoint]
# )
'''
# SC4031 Course Project: American Sign Language (ASL) Detection
## Introduction
In this course project, an Android application is developed to detect and recognize alphabets signed in ASL to translate them onto the mobile app.

## Dataset
The model was trained on a total of 28,869 images, with a total of 1,967 additional test images. While there are 26 letters in the English alphabet, the letters 'J' and 'Z' were excluded in this training and application as they are denoted with gestures. As a future improvement, the model can be improved to train based on gestures instead of static images. However, for the scope of this course project, we focus on static images only.

## Model Architecture
Model: "sequential"
_________________________________________________________________
 Layer (type)                Output Shape              Param #   
=================================================================
 conv2d (Conv2D)             (None, 48, 48, 128)       3328      
                                                                 
 max_pooling2d (MaxPooling2  (None, 24, 24, 128)       0         
 D)                                                              
                                                                 
 dropout (Dropout)           (None, 24, 24, 128)       0         
                                                                 
 conv2d_1 (Conv2D)           (None, 24, 24, 64)        32832     
                                                                 
 max_pooling2d_1 (MaxPoolin  (None, 12, 12, 64)        0         
 g2D)                                                            
                                                                 
 dropout_1 (Dropout)         (None, 12, 12, 64)        0         
                                                                 
 conv2d_2 (Conv2D)           (None, 12, 12, 32)        8224      
                                                                 
 max_pooling2d_2 (MaxPoolin  (None, 6, 6, 32)          0         
 g2D)                                                            
                                                                 
 dropout_2 (Dropout)         (None, 6, 6, 32)          0         
                                                                 
 flatten (Flatten)           (None, 1152)              0         
                                                                 
 dense (Dense)               (None, 512)               590336    
                                                                 
 dropout_3 (Dropout)         (None, 512)               0         
                                                                 
 dense_1 (Dense)             (None, 24)                12312     
                                                                 
=================================================================
Total params: 647032 (2.47 MB)
Trainable params: 647032 (2.47 MB)
Non-trainable params: 0 (0.00 Byte)
_________________________________________________________________


## Model Fitting
Epoch 1/20
225/225 [==============================] - 274s 1s/step - loss: 1.9754 - accuracy: 0.3655 - val_loss: 0.8952 - val_accuracy: 0.7510 - lr: 0.0010
Epoch 2/20
225/225 [==============================] - 142s 632ms/step - loss: 0.7305 - accuracy: 0.7333 - val_loss: 0.6914 - val_accuracy: 0.7937 - lr: 0.0010
Epoch 3/20
225/225 [==============================] - 149s 663ms/step - loss: 0.4406 - accuracy: 0.8420 - val_loss: 0.7170 - val_accuracy: 0.7906 - lr: 0.0010
Epoch 4/20
225/225 [==============================] - 147s 651ms/step - loss: 0.3152 - accuracy: 0.8880 - val_loss: 0.6842 - val_accuracy: 0.8177 - lr: 0.0010
Epoch 5/20
225/225 [==============================] - 154s 682ms/step - loss: 0.2255 - accuracy: 0.9203 - val_loss: 0.6232 - val_accuracy: 0.7969 - lr: 0.0010
Epoch 6/20
225/225 [==============================] - ETA: 0s - loss: 0.1934 - accuracy: 0.9320
Epoch 6: ReduceLROnPlateau reducing learning rate to 0.0005000000237487257.
225/225 [==============================] - 162s 718ms/step - loss: 0.1934 - accuracy: 0.9320 - val_loss: 0.6418 - val_accuracy: 0.8172 - lr: 0.0010
Epoch 7/20
225/225 [==============================] - 163s 723ms/step - loss: 0.1457 - accuracy: 0.9479 - val_loss: 0.5528 - val_accuracy: 0.8516 - lr: 5.0000e-04
Epoch 8/20
225/225 [==============================] - 149s 661ms/step - loss: 0.1216 - accuracy: 0.9581 - val_loss: 0.5924 - val_accuracy: 0.8464 - lr: 5.0000e-04
Epoch 9/20
225/225 [==============================] - 140s 621ms/step - loss: 0.1156 - accuracy: 0.9599 - val_loss: 0.4552 - val_accuracy: 0.8667 - lr: 5.0000e-04
Epoch 10/20
225/225 [==============================] - 130s 576ms/step - loss: 0.1058 - accuracy: 0.9628 - val_loss: 0.5258 - val_accuracy: 0.8641 - lr: 5.0000e-04
Epoch 11/20
225/225 [==============================] - ETA: 0s - loss: 0.0970 - accuracy: 0.9663
Epoch 11: ReduceLROnPlateau reducing learning rate to 0.0002500000118743628.
225/225 [==============================] - 133s 588ms/step - loss: 0.0970 - accuracy: 0.9663 - val_loss: 0.6298 - val_accuracy: 0.8318 - lr: 5.0000e-04
Epoch 12/20
225/225 [==============================] - 132s 586ms/step - loss: 0.0762 - accuracy: 0.9744 - val_loss: 0.6254 - val_accuracy: 0.8443 - lr: 2.5000e-04
Epoch 13/20
225/225 [==============================] - ETA: 0s - loss: 0.0768 - accuracy: 0.9725
Epoch 13: ReduceLROnPlateau reducing learning rate to 0.0001250000059371814.
225/225 [==============================] - 133s 591ms/step - loss: 0.0768 - accuracy: 0.9725 - val_loss: 0.5663 - val_accuracy: 0.8615 - lr: 2.5000e-04
Epoch 14/20
225/225 [==============================] - 144s 637ms/step - loss: 0.0677 - accuracy: 0.9766 - val_loss: 0.5847 - val_accuracy: 0.8646 - lr: 1.2500e-04
Epoch 15/20
225/225 [==============================] - ETA: 0s - loss: 0.0654 - accuracy: 0.9775
Epoch 15: ReduceLROnPlateau reducing learning rate to 6.25000029685907e-05.
225/225 [==============================] - 145s 645ms/step - loss: 0.0654 - accuracy: 0.9775 - val_loss: 0.6021 - val_accuracy: 0.8557 - lr: 1.2500e-04
Epoch 16/20
225/225 [==============================] - 150s 667ms/step - loss: 0.0618 - accuracy: 0.9790 - val_loss: 0.6270 - val_accuracy: 0.8536 - lr: 6.2500e-05
Epoch 17/20
225/225 [==============================] - ETA: 0s - loss: 0.0641 - accuracy: 0.9781
Epoch 17: ReduceLROnPlateau reducing learning rate to 3.125000148429535e-05.
225/225 [==============================] - 144s 637ms/step - loss: 0.0641 - accuracy: 0.9781 - val_loss: 0.5816 - val_accuracy: 0.8573 - lr: 6.2500e-05
Epoch 18/20
225/225 [==============================] - 142s 630ms/step - loss: 0.0624 - accuracy: 0.9798 - val_loss: 0.6122 - val_accuracy: 0.8526 - lr: 3.1250e-05
Epoch 19/20
225/225 [==============================] - ETA: 0s - loss: 0.0603 - accuracy: 0.9793
Epoch 19: ReduceLROnPlateau reducing learning rate to 1.5625000742147677e-05.
225/225 [==============================] - 142s 632ms/step - loss: 0.0603 - accuracy: 0.9793 - val_loss: 0.6147 - val_accuracy: 0.8547 - lr: 3.1250e-05
Epoch 20/20
225/225 [==============================] - 154s 684ms/step - loss: 0.0580 - accuracy: 0.9794 - val_loss: 0.5976 - val_accuracy: 0.8583 - lr: 1.5625e-05

## Final Accuracies
The final trained model achieved an accuracy of 97.94% and a validation accuracy of 85.83%. 
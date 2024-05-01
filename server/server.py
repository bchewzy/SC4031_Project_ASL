from flask import Flask, request, make_response
from flask_socketio import SocketIO, emit
# from engineio.payload import Payload

import os
import numpy as np
import cv2
import time
import json
import mediapipe as mp
from keras.models import load_model
from cvzone.HandTrackingModule import HandDetector


app = Flask(__name__)
app.config['SECRET_KEY'] = 'sc4031'
# Payload.max_decode_packets = 500
socketio = SocketIO(app, cors_allowed_origins="*")

# Load Model
# model = load_model("../MNIST/models/model_all_alpha_85.h5")
model = load_model("../MNIST/models/test.h5")

# Retrieve Labels
labels = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y']
labels = np.array(labels)

mp_hands = mp.solutions.hands
mp_drawing = mp.solutions.drawing_utils

hands = mp_hands.Hands()

# Process image (byte array) received from device
def preprocess_image(image):
    img = cv2.imdecode(image, flags=cv2.IMREAD_COLOR)

    img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)

    # img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    
    return img

def extract_features(img):
    features = np.array(img)
    features = features.reshape(1, 48, 48, 1)
    features = features/255.0
    
    return features

def predict_letter(img):
    frame = img
    h, w, c = frame.shape
    offset = 20

    with mp_hands.Hands() as hands:
        results = hands.process(img)
        hand_landmarks = results.multi_hand_landmarks

        if hand_landmarks:
            analysis_frame = frame

            for hand_lm in hand_landmarks:
                mp_drawing.draw_landmarks(frame, hand_lm, mp_hands.HAND_CONNECTIONS)

                # cv2.imwrite('./images/landmark/' + str(time.time()) + ".jpg", frame)
                
                x_max = 0
                y_max = 0
                x_min = w
                y_min = h
                for lm in hand_lm.landmark:
                    x, y = int(lm.x * w), int(lm.y * h)
                    if x > x_max:
                        x_max = x
                    if x < x_min:
                        x_min = x
                    if y > y_max:
                        y_max = y
                    if y < y_min:
                        y_min = y
                        
                y_min -= 20
                y_max += 20
                x_min -= 20
                x_max += 20

                # Calculate the max length of square bounding box
                hand_width = x_max - x_min
                hand_height = y_max - y_min
                side_length = max(hand_width, hand_height)

                # Calculate the coordinates of the top-left corner of the square
                x_square = x_min + (hand_width - side_length) // 2
                y_square = y_min + (hand_height - side_length) // 2

                # Get bounding box coords
                x_start = x_square - offset
                y_start = y_square - offset
                x_end = x_square + side_length + offset
                y_end = y_square + side_length + offset

                # Crop out hand only from frame
                cropped = frame[y_start:y_end, x_start:x_end]

                if cropped.shape[0] > 0 and cropped.shape[1] > 0:
                    # cv2.imshow("Cropped", cropped)
                    cv2.imwrite('./images/crop/' + str(time.time()) + ".jpg", cropped)
             
            # Set Frame to Analyse as Cropped Hand
            if cropped.shape[0] > 0 and cropped.shape[1] > 0:
                predict_frame = cropped
                predict_frame = cv2.cvtColor(predict_frame, cv2.COLOR_BGR2GRAY)
                predict_frame = cv2.resize(predict_frame, (48, 48))
                predict_frame = extract_features(predict_frame)

                predictions = model.predict(predict_frame)
                pred_array = np.array(predictions)
                predicted_class_index = np.argmax(predictions)

                print(predictions)

                # Map the predicted index to class label
                predicted_label = labels[predicted_class_index]
                
                # Print the predicted label
                # print("Predicted label:", predicted_label)

                # # Send back predicted value to client
                # emit('prediction', predicted_label)
                return predicted_label


# On Connect
@socketio.on('connect')
def handle_connect():
    client_ip = request.remote_addr
    print(f"\n === Client connected with IP: {client_ip} === \n")

@socketio.on('uploadTrain')
def upload_images(msg):
    client_ip = request.remote_addr

    # images = json.loads(msg)

    # print(f"{client_ip}: Uploaded {len(images)} images for training.")

    print(type(msg))

@socketio.on('transfer')
def handle_image(msg):
    client_ip = request.remote_addr

    if(isinstance(msg, bytes)):
        img_array = np.frombuffer(msg, dtype=np.uint8)

        preprocessed_img = preprocess_image(img_array)

        predicted_label = predict_letter(preprocessed_img)

        if predicted_label is not None:
            print(f"{client_ip}: Predicted Label: {predicted_label}")
        
        emit('prediction', predicted_label)


if __name__ == '__main__':
    socketio.run(app, host='0.0.0.0', port=5000)
	
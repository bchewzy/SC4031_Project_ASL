from flask import Flask, request
from flask_socketio import SocketIO, emit
# from engineio.payload import Payload

import os
import numpy as np
import cv2
from PIL import Image
from io import BytesIO
from keras.models import load_model
import base64



app = Flask(__name__)
app.config['SECRET_KEY'] = 'sc4031'
# Payload.max_decode_packets = 500
socketio = SocketIO(app, cors_allowed_origins="*")

# Load Model
model = load_model("../new_model_test_A.h5")

# Retrieve Labels
TRAIN_DIR = "../asl_alphabet/asl_alphabet_train/"
labels = []
for folder in os.listdir("./" + TRAIN_DIR):
    labels.append(folder)

label_list = np.array(labels)
print(label_list)

# Process image (byte array) received from device
def preprocess_image(image):
    # pass
    img = cv2.imdecode(image, flags=cv2.IMREAD_COLOR)

    # Resize image
    img = cv2.resize(img, (64, 64))
    
    # Convert image to RGB format
    # img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
    
    img = img.astype(np.float32) / 255.0
    img = np.expand_dims(img, axis=0)
    
    return img
    
    #return img

    # # Decode byte array into image
    # img = Image.open(BytesIO(byte_array))
    
    # # Convert to numpy array
    # img = np.array(img)
    
    # # if img_np.shape[-1] == 4:
    # #     img_np = img_np[..., :3]
    
    

# On Connect
@socketio.on('connect')
def handle_connect():
    client_ip = request.remote_addr
    print(f"\n === Client connected with IP: {client_ip} === \n")

@socketio.on('transfer')
def handle_image(msg):
    # print(f'Received: {msg}\n\n')
    if(isinstance(msg, bytes)):
        print("processing img...\n")
        img_array = np.frombuffer(msg, dtype=np.uint8)

        preprocessed_img = preprocess_image(img_array)

        prediction = model.predict(preprocessed_img)

        predicted_class_index = np.argmax(prediction)
        predicted_label = label_list[predicted_class_index]
        
        # Print the predicted label
        print(f"Predicted label: {predicted_label}")

        # Send back predicted value to client
        emit('prediction', predicted_label)


    # img_bytes = base64.b64decode(msg)
    # preprocessed_img = preprocess_image(img_bytes)
    # prediction = model.predict(preprocessed_img)
    
    # # Map the predicted index to class label
    # predicted_class_index = np.argmax(prediction)
    # predicted_label = label_list[predicted_class_index]
    
    # # Print the predicted label
    # print(f"Predicted label: {predicted_label}")


if __name__ == '__main__':
    socketio.run(app, host='0.0.0.0', port=5000)
	
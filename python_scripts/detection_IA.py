import cv2
import mediapipe as mp
import time
from datetime import datetime
from paho.mqtt import publish
from paho.mqtt import client as mqtt
import base64

cap = cv2.VideoCapture(0)
mpFaces = mp.solutions.face_detection
mpDraw = mp.solutions.drawing_utils
mpHands = mp.solutions.hands

faces = mpFaces.FaceDetection()
hands = mpHands.Hands()

pTime = 0
cTime = 0

streaming = True

MQTT_SERVER = "localhost"
MQTT_TOPIC = "Image"
MQTT_PORT = 5555
MQTT_CLIENT ="camera_1"
MQTT_PATH = "/sensor/camera/"+MQTT_CLIENT+"/"
MQTT_STREAMING = "Streaming"
MQTT_LOG = "Camera Log"


night = False
brightness = 150

prev = time.time()

while True:
    img = cap.read()[1]
    original = img.copy()

    if night: img[img <= 150] += brightness

    imgRGB = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)


    face_results = faces.process(imgRGB)
    hand_results = hands.process(imgRGB)

    record = ""

    if face_results.detections:
        for id, detection in enumerate(face_results.detections):
            #print("I am",*100,"% sure this is a face")
            #mpDraw.draw_detection(img,detection)
            pass
        record += "face "

    if(hand_results.multi_hand_landmarks):
        for handLms in hand_results.multi_hand_landmarks:
                #mpDraw.draw_landmarks(img, handLms, mpHands.HAND_CONNECTIONS)
                pass
        record += "hand "

    if "hand" in record or "face" in record or night:
        cTime = time.time()
        fps = 1/(cTime-pTime)
        pTime = cTime

        #cv2.putText(img, "FPS: "+str(int(fps)), (10,50), cv2.FONT_HERSHEY_COMPLEX,1,(0,255,0), 2)
        #cv2.putText(img, "Label: "+record, (10,80), cv2.FONT_HERSHEY_COMPLEX, 0.7, (0,255,0), 2)
        ts = "Timestamp:"+datetime.strftime(datetime.now(),"%d-%m-%Y %H:%M:%S")
        cv2.imshow("Detection", img)
        #cv2.imshow("Original", original)

        # Encode image
        byteArr = cv2.imencode('.jpg', img)[1].tobytes()
        #print(byteArr)

        # Encode image using base64
        base64_bytes = base64.b64encode(byteArr)
        
        try:
            if(streaming):
                publish.single(MQTT_PATH+MQTT_STREAMING, base64_bytes, hostname=MQTT_SERVER, port=MQTT_PORT)
                publish.single(MQTT_LOG,"Detected face at "+str(datetime.now().strftime("%d/%m/%Y, %H:%M:%S")), hostname=MQTT_SERVER, port=MQTT_PORT,client_id=MQTT_CLIENT)
                print("Image sent to stream")
        
            if (time.time() - prev > 1):
                publish.single(MQTT_PATH+MQTT_TOPIC, base64_bytes, hostname=MQTT_SERVER, port=MQTT_PORT, client_id=MQTT_CLIENT)
                publish.single(MQTT_LOG,"Detected face at "+str(datetime.now().strftime("%d/%m/%Y, %H:%M:%S")), hostname=MQTT_SERVER, port=MQTT_PORT, client_id=MQTT_CLIENT)
                print("Detected face at "+str(datetime.now().strftime("%d/%m/%Y, %H:%M:%S")))
                print("Image sent to server")
                prev = time.time()
        except:
            print("Error sending image")            

    cv2.waitKey(1)
    
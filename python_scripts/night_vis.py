# Increase the contrast of a night image

import cv2 as cv
import numpy as np
import time

cap = cv.VideoCapture(0)
while True:
    img = cap.read()[1]
    img = cv.cvtColor(img, cv.COLOR_BGR2RGB)
    img = cv.cvtColor(img, cv.COLOR_RGB2GRAY)

    bright_img = img + 200

    cv.imshow("Original", img)
    cv.imshow("Bright", bright_img)
    cv.waitKey(1)
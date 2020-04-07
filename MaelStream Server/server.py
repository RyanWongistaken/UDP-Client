#!/usr/bin/env python
# coding: utf-8

# Server code for MaelStream
# Modified by Lemuel Santos
# Version 1 - 2020 March 30

import socket
import cv2
import sys
import argparse
from base64 import b64encode
import video_grabber
from PyQt5.QtWidgets import *
from PyQt5.QtGui import *
from PyQt5.QtCore import pyqtSlot

#Default parameters
jpeg_quality = 50
host = ''
#port = args.port
encoder = 'cv2'
serverRunning = True

class Window(QMainWindow):

    #Constructor
    def __init__(self):
        super(Window, self).__init__()

        self.initUI()
        self.show()

    def initUI(self):
        self.setGeometry(50, 50, 300, 100)
        self.setWindowTitle("MaelStream Server")
        self.setStyleSheet("QLabel {font: 10pt Helvetica}")

        #Main Label
        label = QLabel('MaelStream Server', self)
        label.move(0, 0)
        label.adjustSize()
        label.show()

        #Main button
        btn = QPushButton("START SERVER", self)
        btn.setToolTip("Start MaelStream server on this machine")
        btn.setCheckable(True)
        #btn.setStyleSheet("background-color: green")

        btn.move(100,60)
        btn.clicked[bool].connect(self.onClick)

        #IP Address Label
        ipLabel = QLabel('IP Address: ' + socket.gethostbyname(socket.gethostname()), self)
        ipLabel.adjustSize()
        ipLabel.move(0,30)

        #Port Entry
        self.textbox = QLineEdit(self)
        self.textbox.move(0,61)
        self.textbox.resize(100,29)

    def onClick(self, pressed):

        if pressed:
            print('Started server')
            self.sender().setText("STOP SERVER")

            # The grabber of the webcam
            grabber = video_grabber.VideoGrabber(jpeg_quality, encoder)
            grabber.start()
            get_message = lambda: grabber.get_buffer()

            keep_running = True

            sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

            port = int(self.textbox.text())

            # Bind the socket to the port
            server_address = (host, port)

            print('starting up on %s port %s\n' % server_address)

            sock.bind(server_address)

            ipAddr = socket.gethostbyname(socket.gethostname())

            print("Server IP: ", ipAddr)

            frame_idx = 0

            while (keep_running):
                data, address = sock.recvfrom(4)
                data = data.decode('utf-8')
                if (data == "get"):
                    bufferRaw = get_message()
                    buffer = b64encode(bufferRaw)
                    frame_idx += 1
                    # print("Rec: ", data, " Frame: ", str(frame_idx), " Buff size", len(buffer), " b64: ", buffer[0], buffer[1], buffer[2],
                    #      bufferRaw[0], bufferRaw[1], bufferRaw[2], type(buffer))

                    if buffer is None:
                        continue
                    if len(buffer) > 65507:
                        print(
                            "The message is too large to be sent within a single UDP datagram. We do not handle splitting the message in multiple datagrams")
                        sock.sendto("FAIL".encode('utf-8'), address)
                        continue
                    # We send back the buffer to the client
                    sock.sendto(buffer, address)
                elif (data == "quit"):
                    grabber.stop()
                    keep_running = False

            print("Quitting..")
            grabber.join()
            sock.close()

        else:
            print('Disconnected')
            self.sender().setText("START SERVER")

app = QApplication([])
GUI = Window()
app.exec_()
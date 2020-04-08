package com.MaelStream.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

// OpenCV imports
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;


import androidx.appcompat.app.AppCompatActivity;

import com.MaelStream.app.networkPackage.clientReceive;

public class videoActivity extends AppCompatActivity {

    Mat frameHolder1;
    Boolean imageFilter = false;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("Dev:: OpenCV", "OpenCV loaded successfully");
                    frameHolder1=new Mat();
                } break;
                default:
                {
                    Log.i("Dev:: OpenCV", "OpenCV not loaded successfully");
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        TextView viewAddress = findViewById(R.id.textAddr);
        TextView viewPort = findViewById(R.id.textPort);

        String serverAddress = getIntent().getStringExtra("address");
        String serverPort = getIntent().getStringExtra("port");

        viewAddress.setText(serverAddress);
        viewPort.setText(serverPort);

        final Button buttonClose = findViewById(R.id.btnReturn);

        //Create UDP Thread
        UdpClientHandler udpClientHandler = new UdpClientHandler(this);
        final clientReceive udpClient = new clientReceive(serverAddress, serverPort, udpClientHandler);
        Thread udpThread = new Thread(udpClient);
        udpThread.start();

        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                udpClient.terminate();
                finish();
            }
        });

        final ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButton);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    imageFilter = true;
                } else {
                    imageFilter = false;
                }
            }
        });

    }

    /* Refresh the Image
    *
    *
     */
    private void updateState(String rawData) {
        //textViewState.setText(state);
        ImageView wispImage = findViewById(R.id.wispView);
        byte[] msgByte = Base64.decode(rawData, Base64.DEFAULT);
        Bitmap imgBitmap = BitmapFactory.decodeByteArray(msgByte, 0, msgByte.length);
        Log.i("Dev:: Image Filter? | ", imageFilter.toString());
        // Check if OpenCV is loaded first
        if (OpenCVLoader.initDebug() && imageFilter) {
            // Create blank canvas
            frameHolder1 = new Mat();
            // change bitmap to canvans
            Utils.bitmapToMat(imgBitmap, frameHolder1);

            //manipulate image
            Imgproc.cvtColor(frameHolder1, frameHolder1, Imgproc.COLOR_BGR2GRAY);

            // convert back to bitmap
            Utils.matToBitmap(frameHolder1, imgBitmap);
        }

        wispImage.setImageBitmap(imgBitmap);
    }

    private void updateRxMsg(String rxmsg){
        //textViewRx.append(rxmsg + "\n");
    }

    private void clientEnd(){
        //textViewState.setText("clientEnd()");
        //buttonConnect.setEnable(true);
    }

    public static class UdpClientHandler extends Handler {
        public static final int UPDATE_STATE = 0;
        public static final int UPDATE_MSG = 1;
        public static final int UPDATE_END = 2;
        private videoActivity parent;

        public UdpClientHandler(videoActivity parent) {
            super();
            this.parent = parent;
        }

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case UPDATE_STATE:
                    parent.updateState((String)msg.obj);
                    break;
                case UPDATE_MSG:
                    parent.updateRxMsg((String)msg.obj);
                    break;
                case UPDATE_END:
                    parent.clientEnd();
                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    }
}










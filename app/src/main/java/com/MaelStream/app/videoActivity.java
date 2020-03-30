package com.MaelStream.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

// OpenCV imports
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;


import androidx.appcompat.app.AppCompatActivity;

public class videoActivity extends AppCompatActivity {

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

        //String[] ipData = {serverAddress, serverPort};

        //udpConnect = new Thread(new clientReceive(serverAddress, serverPort)).start();
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

        /*
        updateImageTask obj = new updateImageTask();

        try {
            String img = obj.execute(ipData).get();
            byte[] msgByte = Base64.decode(img, Base64.DEFAULT);
            Bitmap imgBitmap = BitmapFactory.decodeByteArray(msgByte, 0, msgByte.length);
            wispImage.setImageBitmap(imgBitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }

         */

    }

    /* Refresh the Image
    *
    *
     */
    private void updateState(String rawData) {
        //textViewState.setText(state);
        ImageView wispImage = (ImageView) findViewById(R.id.wispView);
        byte[] msgByte = Base64.decode(rawData, Base64.DEFAULT);
        Bitmap imgBitmap = BitmapFactory.decodeByteArray(msgByte, 0, msgByte.length);

        // Create blank canvas
        Mat frameHolder1 = new Mat();
        // change bitmap to canvans
        Utils.bitmapToMat(imgBitmap, frameHolder1);

        //manipulate image
        Imgproc.cvtColor(frameHolder1, frameHolder1, Imgproc.COLOR_BGR2GRAY);

        // convert back to bitmap
        Utils.matToBitmap(frameHolder1, imgBitmap);

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










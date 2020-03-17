package com.ryanwongistaken.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;



public class videoActivity extends AppCompatActivity {

    //updateImageTask wispStream;
    UdpClientThread udpClientThread;

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

        String[] ipData = {serverAddress, serverPort};

        ImageView wispImage = (ImageView) findViewById(R.id.wispView);

        updateImageTask obj = new updateImageTask();
        try {
            String img = obj.execute(ipData).get();
            byte[] msgByte = Base64.decode(img, Base64.DEFAULT);
            Bitmap imgBitmap = BitmapFactory.decodeByteArray(msgByte, 0, msgByte.length);
            wispImage.setImageBitmap(imgBitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}









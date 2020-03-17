package com.ryanwongistaken.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    UdpClientThread udpClientThread;
    TextView textViewState;
    TextView textViewRx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewRx = findViewById(R.id.received);
        textViewState = findViewById(R.id.state);
        final EditText editTextAddress = findViewById(R.id.address);
        final EditText editTextPort = findViewById(R.id.port);
        final Button buttonConnect = findViewById(R.id.connect);

        final udpHandler udpClientHandler = new udpHandler();

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("connect button:", "button clicked!");

                udpClientThread = new UdpClientThread(
                        editTextAddress.getText().toString(),
                        Integer.parseInt(editTextPort.getText().toString()),
                        udpClientHandler);

                udpClientThread.start();

                //Get data string from handler
                String message = udpClientHandler.getMsg();

                    if (message != null) {
                        Log.i("Dev:: Received message (bytes): ", Integer.toString(message.length()));
                        displayPhoto(message);
                    } else
                        Log.i("Dev:: No message received", "");
                }
        });

    }

    private void displayPhoto(String msgString) {
        ImageView iv = (ImageView) findViewById(R.id.imageView);
        byte[] msgByte = Base64.decode( msgString, Base64.DEFAULT);

        Bitmap imgBitmap = BitmapFactory.decodeByteArray(msgByte, 0, msgByte.length);
        iv.setImageBitmap(imgBitmap);
    }

}

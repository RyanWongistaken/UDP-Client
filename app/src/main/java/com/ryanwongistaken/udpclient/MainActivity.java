package com.ryanwongistaken.udpclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.*;
import java.net.*;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Object creation
        final Button connectBtn = findViewById(R.id.conBtn);
        final EditText IP = findViewById(R.id.inputIP);
        final EditText port = findViewById(R.id.inputPort);

        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Connect Button:", "attempting to connect");
//                Socket client = new Socket(IP, port);
//                Toast.makeText(getApplicationContext(),"Connected to: " + client.getRemoteSocketAddress(), Toast.LENGTH_SHORT).show();
//
//                try {
//                    OutputStream outToServer = client.getOutputStream();
//                    DataOutputStream out = new DataOutputStream(outToServer);
//                }catch(IOException e) {
//                    Log.i("Connect Button:", "recieved no data");
//                }
                while(true){
                    try{
                        DatagramSocket clientSocket = new DatagramSocket((int)(port.toString());
                        byte[] receiveData = new byte[65507];
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        clientSocket.receive(receivePacket);
                        String modifiedSentence = new String(receivePacket.getData());
                        System.out.println("FROM SERVER:" + modifiedSentence);
                        clientSocket.close();

                    } catch(IOException e){
                        Log.i("Connect Button:", "could not connect");

                    }
                }





            }
        });







    }
}

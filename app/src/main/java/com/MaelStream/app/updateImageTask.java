package com.MaelStream.app;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class updateImageTask extends AsyncTask<String, Void, String> {
    DatagramSocket socket;
    //String dataLine;
    String address;
    Integer port;
    InetAddress serverAddress;

    @Override
    protected String doInBackground(String... params){

        String dataLine = "";
        Log.i("Dev::", "Getting packets");
        try {
            Log.i("Dev::", "Connection success");
            socket = new DatagramSocket();
            Log.i("Dev::", "Socket created");
            serverAddress = InetAddress.getByName(params[0]);
            Log.i("Dev::", "Address created");
            port = Integer.parseInt(params[1]);
            Log.i("Dev:: Connected to: ", params[0]+ " | " + params[1]);
            // send request
            byte[] buf = "get".getBytes("UTF-8");
            DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddress, port);
            socket.send(packet);

            byte[] bufResponse = new byte[65507]; //Maximum UDP datagram size minus headers
            DatagramPacket packetReceived = new DatagramPacket(bufResponse, bufResponse.length);
            socket.receive(packetReceived);

            //Decode buffered input from Base64 to ASCII
            dataLine = new String(packetReceived.getData(), 0, packetReceived.getLength(), StandardCharsets.US_ASCII);
            //Decoded data check (Server and Client should see the same data)
            byte[] recByte = Base64.decode(dataLine, Base64.DEFAULT);
            Log.d("Dev:: Raw: "+ Integer.toString(packetReceived.getLength()) + " Received buffer length",  recByte.length + " Str length " + Integer.toString(dataLine.length()));

        } catch (SocketException e){
            Log.i("Dev::", "Socket exception");
            e.printStackTrace();
        }catch (UnknownHostException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(socket != null){
                socket.close();
            }
        }
        return dataLine;
    }

    protected Void onPostExecute(Void result){
        updateImageTask obj = new updateImageTask();
        try {
            obj.execute().get();
        } catch(Exception e){
            e.printStackTrace();
        }

        return  null;
    }
}

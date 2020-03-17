package com.ryanwongistaken.test;

import android.os.Message;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class UdpClientThread extends Thread{

    String dstAddress;
    int dstPort;
    private boolean running;
    //MainActivity.UdpClientHandler handler;
    udpHandler handler;

    DatagramSocket socket;
    InetAddress address;

    public UdpClientThread(String addr, int port, udpHandler udphandler) {
        super();
        dstAddress = addr;
        dstPort = port;
        handler = udphandler;
    }

    public void setRunning(boolean running){
        this.running = running;
    }

//    private void sendState(String state){
//        handler.sendMessage(
//                Message.obtain(handler,
//                        MainActivity..UPDATE_STATE, state));
//    }

    @Override
    public void run() {
//        sendState("connecting...");

        running = true;

        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName(dstAddress);

            // send request
            byte[] buf = "get".getBytes("UTF-8");

            DatagramPacket packet =
                    new DatagramPacket(buf, buf.length, address, dstPort);
            socket.send(packet);

//            sendState("connected");

            // get response
            byte[] bufResponse = new byte[65507]; //Maximum UDP datagram size minus headers
            DatagramPacket packetReceived = new DatagramPacket(bufResponse, bufResponse.length);
            socket.receive(packetReceived);

            //String line = new String(bufResponse, StandardCharsets.US_ASCII);
            //byte[] recByte = line.getBytes(StandardCharsets.US_ASCII);

            String line = new String(packetReceived.getData(), 0, packetReceived.getLength(), StandardCharsets.US_ASCII);
            //byte[] recByte = line.getBytes(StandardCharsets.US_ASCII);

            //String line = new String(packetReceived.getData(), 0, packetReceived.getLength());
            byte[] recByte = Base64.decode(line, Base64.DEFAULT);

            Log.d("Dev:: Raw: "+ Integer.toString(packetReceived.getLength()) + " Received buffer length",  recByte.length + " Str length " + Integer.toString(line.length()));

            //Bitmap imgBitmap = BitmapFactory.decodeByteArray(bufResponse, 0, bufResponse.length);

            int test = handler.UPDATE_END;

            handler.sendMessage(
                    Message.obtain(handler, handler.UPDATE_MSG, line));

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(socket != null){
                socket.close();
                handler.sendEmptyMessage(handler.UPDATE_END);
            }
        }

    }
}


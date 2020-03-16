package com.ryanwongistaken.test;

import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UdpClientThread extends Thread{

    String dstAddress;
    int dstPort;
    private boolean running;
    MainActivity.UdpClientHandler handler;

    DatagramSocket socket;
    InetAddress address;

    public UdpClientThread(String addr, int port, MainActivity.UdpClientHandler handler) {
        super();
        dstAddress = addr;
        dstPort = port;
        this.handler = handler;
    }

    public void setRunning(boolean running){
        this.running = running;
    }

//    private void sendState(String state){
//        handler.sendMessage(
//                Message.obtain(handler,
//                        MainActivity.UdpClientHandler.UPDATE_STATE, state));
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

            String line = new String(packetReceived.getData(), 0, packetReceived.getLength());

            Log.d("Dev:: Received packet length", Integer.toString(packetReceived.getLength()));



            handler.sendMessage(
                    Message.obtain(handler, MainActivity.UdpClientHandler.UPDATE_MSG, line));

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(socket != null){
                socket.close();
                handler.sendEmptyMessage(MainActivity.UdpClientHandler.UPDATE_END);
            }
        }

    }
}


package com.MaelStream.app.networkPackage;

import android.os.Message;
import android.util.Base64;
import android.util.Log;

import com.MaelStream.app.serverPackage.serverClass;
import com.MaelStream.app.videoActivity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class clientReceive implements Runnable {
    DatagramSocket _socket;
    serverClass server;
    InetAddress _serverAddress;
    videoActivity.UdpClientHandler handler;
    boolean run = false;

    public clientReceive(String ipAddress, String socketPort, videoActivity.UdpClientHandler handler){
        super();
        server = new serverClass(ipAddress, socketPort);
        this.handler = handler;
    }

    public void terminate(){
        run = false;
    }

    @Override
    public void run() {
        run = true;
        while(run) {
            String dataLine = "";
            //Log.i("Dev::", "Getting packets");
            try {
                //Log.i("Dev::", "Connection success");
                _socket = new DatagramSocket();
                //Log.i("Dev::", "Socket created");
                _serverAddress = InetAddress.getByName(server.getIP());
                //Log.i("Dev::", "Address created");

                //Log.i("Dev:: Connected to: ", server.getIP() + " | " + server.getPort());

                // send request
                byte[] buf = "get".getBytes(StandardCharsets.UTF_8);
                DatagramPacket packet = new DatagramPacket(buf, buf.length, _serverAddress, Integer.parseInt(server.getPort()));
                _socket.send(packet);

                _socket.setSoTimeout(100);

                // Receive image data from server
                byte[] bufResponse = new byte[65507]; //Maximum UDP datagram size minus headers
                DatagramPacket packetReceived = new DatagramPacket(bufResponse, bufResponse.length);
                try {
                    _socket.receive(packetReceived);
                } catch (SocketTimeoutException e) {
                    Log.i("Dev::", "Socket Timeout!");
                    continue;
                }

                //Decode buffered input from Base64 to ASCII
                dataLine = new String(packetReceived.getData(), 0, packetReceived.getLength(), StandardCharsets.US_ASCII);

                //Decoded data check (Server and Client should see the same data)
                byte[] recByte = Base64.decode(dataLine, Base64.DEFAULT);

                //Log.d("Dev:: Raw: "+ (packetReceived.getLength()) + " Received buffer length",  recByte.length + " Str length " + Integer.toString(dataLine.length()));

                handler.sendMessage(Message.obtain(handler, videoActivity.UdpClientHandler.UPDATE_STATE, dataLine));

            } catch (SocketException e){
                Log.i("Dev::", "Socket exception");
                e.printStackTrace();
            }catch (UnknownHostException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(_socket != null){
                    _socket.close();
                }
            }
        }

    }
}

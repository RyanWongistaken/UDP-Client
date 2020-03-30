package com.MaelStream.app;

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

public class clientReceive implements Runnable {
    DatagramSocket _socket;
    String _address;
    String _port;
    InetAddress _serverAddress;
    videoActivity.UdpClientHandler handler;

    public clientReceive(String ipAddress, String socketPort, videoActivity.UdpClientHandler handler){
        super();
        _address = ipAddress;
        _port = socketPort;
        this.handler = handler;
    }

    @Override
    public void run() {
        boolean run = true;
        while(run) {
            String dataLine = "";
            Log.i("Dev::", "Getting packets");
            try {
                //Log.i("Dev::", "Connection success");
                _socket = new DatagramSocket();
                //Log.i("Dev::", "Socket created");
                _serverAddress = InetAddress.getByName(_address);
                //Log.i("Dev::", "Address created");

                Log.i("Dev:: Connected to: ", _address + " | " + _port);
                // send request
                byte[] buf = "get".getBytes("UTF-8");
                DatagramPacket packet = new DatagramPacket(buf, buf.length, _serverAddress, Integer.parseInt(_port));
                _socket.send(packet);

                byte[] bufResponse = new byte[65507]; //Maximum UDP datagram size minus headers
                DatagramPacket packetReceived = new DatagramPacket(bufResponse, bufResponse.length);
                _socket.receive(packetReceived);

                //Decode buffered input from Base64 to ASCII
                dataLine = new String(packetReceived.getData(), 0, packetReceived.getLength(), StandardCharsets.US_ASCII);

                //Decoded data check (Server and Client should see the same data)
                byte[] recByte = Base64.decode(dataLine, Base64.DEFAULT);
                Log.d("Dev:: Raw: "+ (packetReceived.getLength()) + " Received buffer length",  recByte.length + " Str length " + Integer.toString(dataLine.length()));

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

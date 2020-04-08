package com.MaelStream.app.serverPackage;

public class serverClass {
    String ipAddress;
    String Port;

    public serverClass(String ip, String port){
        ipAddress = ip;
        Port = port;
    }

    public String getIP(){
        return ipAddress;
    }

    public String getPort() {
        return Port;
    }
}

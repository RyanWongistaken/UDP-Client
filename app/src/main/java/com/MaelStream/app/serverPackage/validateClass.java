package com.MaelStream.app.serverPackage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class validateClass {
    public boolean IP(String ipAddress){
        Pattern pattern = Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");
        Matcher match = pattern.matcher(ipAddress);
        return match.find();
    }

    public boolean Port(String port){
        Pattern pattern = Pattern.compile("\\d{1,5}");
        Matcher match = pattern.matcher(port);
        return match.find();
    }
}

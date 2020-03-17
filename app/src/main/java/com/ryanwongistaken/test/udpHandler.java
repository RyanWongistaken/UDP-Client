package com.ryanwongistaken.test;

import android.os.Handler;
import android.os.Message;

public class udpHandler extends Handler {
    public static final int UPDATE_STATE = 0;
    public static final int UPDATE_MSG = 1;
    public static final int UPDATE_END = 2;
    //private MainActivity parent;

    /*
    public UdpClientHandler() {
        //super();
        //this.parent = parent;
    }
    */
    private String message;

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what){
            case UPDATE_STATE:
                //parent.updateState((String)msg.obj);
                break;
            case UPDATE_MSG:
                updateRxMsg((String)msg.obj);
                break;
            case UPDATE_END:
                //parent.clientEnd();
                break;
            default:
                //super.handleMessage(msg);
        }

    }


    private void updateState(String state) {

    }

    private void updateRxMsg(String rxmsg){
        message = rxmsg;
    }

    private void clientEnd(){

    }

    public String getMsg(){
        return message;
    }
}
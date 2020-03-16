package com.ryanwongistaken.test;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    UdpClientHandler udpClientHandler;
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

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("connect button:", "button clicked!");
                udpClientThread = new UdpClientThread(
                        editTextAddress.getText().toString(),
                        Integer.parseInt(editTextPort.getText().toString()),
                        udpClientHandler);
                udpClientThread.start();
                //buttonConnect.setEnabled(false);
            }
        });
    }

    private void updateState(String state) {
        textViewState.setText(state);
    }

    private void updateRxMsg(String rxmsg){
        textViewRx.append(rxmsg + "\n");
    }

    private void clientEnd(){
        udpClientThread = null;
        textViewState.setText("clientEnd()");
        //buttonConnect.setEnable(true);
    }

    public static class UdpClientHandler extends Handler {
        public static final int UPDATE_STATE = 0;
        public static final int UPDATE_MSG = 1;
        public static final int UPDATE_END = 2;
        private MainActivity parent;

        public UdpClientHandler(MainActivity parent) {
            super();
            this.parent = parent;
        }

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case UPDATE_STATE:
                    parent.updateState((String)msg.obj);
                    break;
                case UPDATE_MSG:
                    parent.updateRxMsg((String)msg.obj);
                    break;
                case UPDATE_END:
                    parent.clientEnd();
                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    }
}

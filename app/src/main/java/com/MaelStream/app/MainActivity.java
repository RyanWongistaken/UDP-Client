package com.MaelStream.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    //Initializations
    UdpClientThread udpClientThread;
    TextView textViewState;
    TextView textViewRx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewRx = findViewById(R.id.received);
        textViewState = findViewById(R.id.state);
        final Button buttonConnect = findViewById(R.id.connect);


        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("connect button:", "button clicked!");

                openVideoActivity();
                }
        });

    }

    private boolean validateIP(String ipAddress){
        Pattern pattern = Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");
        Matcher match = pattern.matcher(ipAddress);
        return match.find();
    }

    private boolean validatePort(String port){
        Pattern pattern = Pattern.compile("\\d{5}");
        Matcher match = pattern.matcher(port);
        return match.find();
    }

    private void openVideoActivity() {
        EditText editTextAddress = findViewById(R.id.address);
        EditText editTextPort = findViewById(R.id.port);

        if(editTextAddress.getText().toString().isEmpty() || editTextPort.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Please enter server and port to connect! ", Toast.LENGTH_LONG).show();
            Log.i("IP:: ", Boolean.toString(validateIP(editTextAddress.getText().toString())));
        }
        else {

            Boolean isIpValid = validateIP(editTextAddress.getText().toString());
            Boolean isPortValid = validatePort(editTextPort.getText().toString());

            if(isIpValid && isPortValid) {
                Intent intent = new Intent(this, videoActivity.class);
                intent.putExtra("address", editTextAddress.getText().toString());
                intent.putExtra("port", editTextPort.getText().toString());
                startActivityForResult(intent, 999);
            }
            else
                Toast.makeText(getApplicationContext(), "Please enter valid server and port!", Toast.LENGTH_LONG).show();
        }
    }

    /*
    private void displayPhoto(String msgString) {
        ImageView iv = (ImageView) findViewById(R.id.imageView);
        //Decode string from Base64 to original image bytes
        byte[] msgByte = Base64.decode( msgString, Base64.DEFAULT);

        //Convert to bitmap
        Bitmap imgBitmap = BitmapFactory.decodeByteArray(msgByte, 0, msgByte.length);
        iv.setImageBitmap(imgBitmap);
    }
    */
}

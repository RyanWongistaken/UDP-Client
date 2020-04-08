package com.MaelStream.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.MaelStream.app.serverPackage.serverClass;
import com.MaelStream.app.serverPackage.validateClass;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    //Initializations
    validateClass validate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button buttonConnect = findViewById(R.id.connect);

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("connect button:", "button clicked!");

                openVideoActivity();
                }
        });
    }

    private void openVideoActivity() {
        EditText editTextAddress = findViewById(R.id.address);
        EditText editTextPort = findViewById(R.id.port);

        serverClass server = new serverClass(editTextAddress.getText().toString(), editTextPort.getText().toString());

        if(server.getIP().isEmpty() || server.getPort().isEmpty()){
            Toast.makeText(getApplicationContext(), "Please enter server and port to connect! ", Toast.LENGTH_LONG).show();
            Log.i("IP:: ", Boolean.toString(validate.IP(server.getIP())));
        }
        else {

            Boolean isIpValid = validate.IP(server.getIP());
            Boolean isPortValid = validate.Port(server.getPort());

            if(isIpValid && isPortValid) {
                Intent intent = new Intent(this, videoActivity.class);
                intent.putExtra("address", server.getIP());
                intent.putExtra("port", server.getPort());
                startActivityForResult(intent, 999);
            }
            else
                Toast.makeText(getApplicationContext(), "Please enter valid server and port!", Toast.LENGTH_LONG).show();
        }
    }
}

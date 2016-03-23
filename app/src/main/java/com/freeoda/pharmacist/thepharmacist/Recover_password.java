package com.freeoda.pharmacist.thepharmacist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Recover_password extends AppCompatActivity {

    EditText email = (EditText)findViewById(R.id.reset_email);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);

        Button resetButton = (Button)findViewById(R.id.button_reset_password);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuffer chaine = new StringBuffer("");
                try{
                    URL url = new URL("http://pharmacist2016.netau.net/reset_password.php");
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestProperty("User-Agent", "");
                    connection.setRequestMethod("POST");
                    connection.setDoInput(true);
                    connection.connect();

                    InputStream inputStream = connection.getInputStream();

                    BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    while ((line = rd.readLine()) != null) {
                        chaine.append(line);
                    }

                } catch (IOException e) {
                    // writing exception to log
                    e.printStackTrace();
                }

                Log.d("eeeeeee",chaine.toString());
            }
        });
    }
}

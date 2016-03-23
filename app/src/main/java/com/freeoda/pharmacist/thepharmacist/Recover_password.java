package com.freeoda.pharmacist.thepharmacist;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Recover_password extends AppCompatActivity {

    //EditText email = (EditText)findViewById(R.id.reset_email);

    class RetrieveFeedTask extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... voids) {
            StringBuffer chaine = new StringBuffer("");
            String response = "";
            try {
                URL url = new URL("http://pharmacist2016.netau.net/reset_password.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("User-Agent", "");
                connection.setRequestMethod("POST");
                connection.setDoInput(true);

                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write("mail=hi");

                writer.flush();
                writer.close();
                os.close();
                int responseCode=connection.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        response+=line;
                    }
                }
                else {
                    response="";

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //return response;

            Log.d("eeeeeee", response);
            Log.d("dddddddd", "------------------------------------------------");
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);

        Button resetButton = (Button)findViewById(R.id.button_reset_password);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RetrieveFeedTask().execute();
            }
        });
    }
}

package com.freeoda.pharmacist.thepharmacist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class EnterNumberActivity extends AppCompatActivity {
    EditText txtMobileNo;
    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_number);


        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        String country;
        for( Locale loc : locale ){
            country = loc.getDisplayCountry();
            if( country.length() > 0 && !countries.contains(country) ){
                countries.add( country );
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);

        Spinner citizenship = (Spinner)findViewById(R.id.drpCountries);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, countries);
        citizenship.setAdapter(adapter);

        txtMobileNo = (EditText)findViewById(R.id.txtMobileNumber);
        Button btnMobileNo  =(Button)findViewById(R.id.btnMobileNo);
        btnMobileNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtMobileNo.getText().toString().equals("")) {
                    txtMobileNo.setError("Please enter Mobile Number");
                } else {
                    SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("mobile",txtMobileNo.getText().toString() );
                    editor.commit();
                    startActivity(new Intent(EnterNumberActivity.this, EnterNameActivity.class));
                }
            }
        });

        Button btnMobileNumber  =(Button)findViewById(R.id.txtEnterMobileNumberButton);
        btnMobileNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    //global.personDetails.setMobileNo(txtMobileNo.getText().toString());
                    startActivity(new Intent(EnterNumberActivity.this, EnterEmailActivity.class));

                }

        });


    }
}


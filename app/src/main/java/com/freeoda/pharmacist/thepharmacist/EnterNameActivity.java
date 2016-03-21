package com.freeoda.pharmacist.thepharmacist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class EnterNameActivity extends AppCompatActivity {
    EditText firstName, lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_name);

        ArrayList<String> arr = new ArrayList<String>();
        arr.add("Mr");
        arr.add("Mrs");
        arr.add("Miss");

        Spinner spinner = (Spinner) findViewById(R.id.spinnerMr);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arr);
        spinner.setAdapter(adapter);

        firstName = (EditText) findViewById(R.id.txtFirstName);
        lastName = (EditText) findViewById(R.id.txtLastName);

        Button btnNameToBirthDay = (Button) findViewById(R.id.btnNameToBirthDay);
        btnNameToBirthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!firstName.getText().toString().equals("") && !lastName.getText().toString().equals("")) {
                    Log.i("TAG", firstName.getText().toString());
                    //global.personDetails.setFirstName("kkk");
                    //global.personDetails.setLastName(lastName.getText().toString());
                    SharedPreferences sharedpreferences = getSharedPreferences(EnterNumberActivity.MyPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("firstName",firstName.getText().toString() );
                    editor.putString("lastName",lastName.getText().toString() );
                    editor.commit();
                    startActivity(new Intent(EnterNameActivity.this, EnterBirthDayActivity.class));
                } else {

                    if (firstName.getText().toString().equals("")) {
                        firstName.setError("Please Enter First Name ");
                    }
                    if (lastName.getText().toString().equals("")) {
                        lastName.setError("Please Enter Last Name ");
                    }
                }

            }
        });

    }
}

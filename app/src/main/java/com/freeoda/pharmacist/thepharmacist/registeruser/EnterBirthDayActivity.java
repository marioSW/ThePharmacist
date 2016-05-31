package com.freeoda.pharmacist.thepharmacist.registeruser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.freeoda.pharmacist.thepharmacist.R;

public class EnterBirthDayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_birth_day);

        DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker1);
        String date = String.valueOf(datePicker.getDayOfMonth());
        String month = String.valueOf(datePicker.getMonth());
        String year = String.valueOf(datePicker.getYear());

        final String bday = date+"/"+month+"/"+year;


        Button btnPwd  =(Button)findViewById(R.id.txtPwd);
        btnPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //LoginSession.personDetails.setBirthDate(bday);
                SharedPreferences sharedpreferences = getSharedPreferences(EnterNumberActivity.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("birthday",bday );
                editor.commit();
                startActivity(new Intent(EnterBirthDayActivity.this, EnterPwdActivity.class));
            }
        });
    }


}

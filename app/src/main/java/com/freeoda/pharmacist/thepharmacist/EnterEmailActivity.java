package com.freeoda.pharmacist.thepharmacist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EnterEmailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_email);

        final EditText txtEmail = (EditText) findViewById(R.id.txtEmail1);
        Button btnMobile = (Button) findViewById(R.id.btnSignUpWithMobile);
        btnMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Button btnMobileNo = (Button) findViewById(R.id.btnEmailToMobileNo);
        btnMobileNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //global.personDetails.setEmail(txtEmail.getText().toString());
                if (txtEmail.getText().toString().equals("")) {
                    txtEmail.setError("Please enter Mobile Number");
                } else {
                    SharedPreferences sharedpreferences = getSharedPreferences(EnterNumberActivity.MyPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("Email", txtEmail.getText().toString());
                    editor.commit();
                    startActivity(new Intent(EnterEmailActivity.this, EnterNameActivity.class));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //    finish();

    }
}

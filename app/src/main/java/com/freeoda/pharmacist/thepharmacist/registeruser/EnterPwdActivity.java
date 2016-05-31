package com.freeoda.pharmacist.thepharmacist.registeruser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.freeoda.pharmacist.thepharmacist.LoginActivity;
import com.freeoda.pharmacist.thepharmacist.R;
import com.freeoda.pharmacist.thepharmacist.exceptions.CustomException;
import com.freeoda.pharmacist.thepharmacist.models.ModelApi;
import com.freeoda.pharmacist.thepharmacist.models.User;
import com.freeoda.pharmacist.thepharmacist.network.NetworkCallback;
import com.freeoda.pharmacist.thepharmacist.network.NetworkFacade;

public class EnterPwdActivity extends AppCompatActivity {
    EditText password, ConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pwd);

        password = (EditText) findViewById(R.id.txtPassword);
        ConfirmPassword = (EditText) findViewById(R.id.txtConfirmPassword);
        Button btnSubmitPassword = (Button) findViewById(R.id.btnSubmitPassword);
        btnSubmitPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getText().toString().equals("") || ConfirmPassword.getText().toString().equals("")) {
                    if (password.getText().toString().equals("")) {
                        password.setError("Please Enter the Password");
                    }
                    if (ConfirmPassword.getText().toString().equals("")) {
                        ConfirmPassword.setError("Please Enter the Confirm Password");
                    }

                } else {
                    if (!password.getText().toString().equals(ConfirmPassword.getText().toString())) {
                        Toast.makeText(getBaseContext(), "password does not match", Toast.LENGTH_LONG).show();
                    } else {
                        //LoginSession.personDetails.setPassword(password.getText().toString());
                        SharedPreferences sharedpreferences = getSharedPreferences(EnterNumberActivity.MyPREFERENCES, Context.MODE_PRIVATE);
                        User user = new User();
                        user.setBirthDate(sharedpreferences.getString("birthday",""));
                        user.setFirstName(sharedpreferences.getString("firstName", ""));
                        user.setPassword(password.getText().toString());
                        user.setEmail(sharedpreferences.getString("Email",""));
                        user.setLastName(sharedpreferences.getString("lastName",""));
                        user.setMobileNo(sharedpreferences.getString("mobile",""));


                        NetworkFacade.registerUser(user,getApplicationContext(), new NetworkCallback() {
                            @Override
                            public void onSuccess(ModelApi result) {
                                startActivity(new Intent(EnterPwdActivity.this, LoginActivity.class));
                            }

                            @Override
                            public void onError(CustomException exception) {
                                Toast.makeText(getApplicationContext(), "Tegistation failed", Toast.LENGTH_LONG).show();
                            }
                        });

//                        Toast.makeText(getBaseContext(),"done",Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }
}

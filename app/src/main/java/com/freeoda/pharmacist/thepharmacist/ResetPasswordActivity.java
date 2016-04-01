package com.freeoda.pharmacist.thepharmacist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.freeoda.pharmacist.thepharmacist.exceptions.CustomException;
import com.freeoda.pharmacist.thepharmacist.models.ModelApi;
import com.freeoda.pharmacist.thepharmacist.network.NetworkCallback;
import com.freeoda.pharmacist.thepharmacist.network.NetworkFacade;

/**
 * Created by Lakna on 3/31/2016.
 */
public class ResetPasswordActivity extends Activity {

    EditText newPwd;
    EditText confirmPwd;
    Button resetPwdBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        newPwd = (EditText)findViewById(R.id.newPwdEditTxt);
        confirmPwd= (EditText)findViewById(R.id.confirmPwdEditTxt);
        resetPwdBtn = (Button)findViewById(R.id.resetPwdtn);

        resetPwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newPwd.getText().toString().equals("") && confirmPwd.getText().toString().equals("")){
                    newPwd.setError("Field is empty");
                    confirmPwd.setError("Field is empty");
                }
                else if (!(newPwd.getText().toString().equals(confirmPwd.getText().toString()) )) {
                        Toast.makeText(getApplicationContext(), "Passwords do not match!", Toast.LENGTH_SHORT).show();

                }
                else{
                    SharedPreferences sharedpreferences = getSharedPreferences(EnterNumberActivity.MyPREFERENCES, Context.MODE_PRIVATE);
                    String user = sharedpreferences.getString("User","");

                    NetworkFacade.resetPassword(user, newPwd.getText().toString(), getApplicationContext(), new NetworkCallback() {
                        @Override
                        public void onSuccess(ModelApi result) {
                            Log.i("TAG", result.toString());
                            startActivity(new Intent(ResetPasswordActivity.this,LoginActivity.class));
                        }

                        @Override
                        public void onError(CustomException exception) {

                        }
                    });
                }
            }
        });
    }
}

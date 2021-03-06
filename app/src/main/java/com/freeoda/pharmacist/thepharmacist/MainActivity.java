package com.freeoda.pharmacist.thepharmacist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.freeoda.pharmacist.thepharmacist.registeruser.EnterNumberActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        Log.i("TAG","Main Activity");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.logout){

            SharedPreferences sharedpreferences = getSharedPreferences(EnterNumberActivity.MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();

            String loginMethod = sharedpreferences.getString("LoginMethod","");
            Log.i("TAG",loginMethod);
            if(loginMethod.equals("facebook")){
                LoginManager.getInstance().logOut();
                finish();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));

                editor.putString("LoginMethod", "");
                editor.commit();

                // Clear Preferences and other data and go back to login activty
            }
            else if (loginMethod.equals("google")){
                //LoginActivity.googlePlusLogout();
                LoginActivity.mGooglePlusLogoutClicked = true;
                finish();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                editor.putString("LoginMethod", "");
                editor.commit();
            }
            else if (loginMethod.equals("normal")){
                finish();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                editor.putString("LoginMethod", "");
                editor.commit();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}

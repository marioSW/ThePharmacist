package com.freeoda.pharmacist.thepharmacist;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.afollestad.materialdialogs.MaterialDialog;
import com.freeoda.pharmacist.thepharmacist.captureimage.RequestHandler;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SelectPharmacies extends AppCompatActivity {
    ListView listView;
    String my_lat=null;
    String my_lng=null;
    String radiant=null;

    //Retrieve data from server
    JSONObject jsonObject;
    JSONArray jsonArray;
    ProgressDialog prgDialog;
    MarkerDataProvider markerDataProvider2;
    MarkerAdapter adapter2;

    String user_id1=null;
    String order_id1=null;
    String upload_image1=null;

    Bitmap bitmap=null;
    public static final String UPLOAD_URL = "http://thepharmacist.freeoda.com/uploadScript.php";
    public static final String UPLOAD_KEY = "image";
    List<String> list=null;

    MaterialDialog.Builder builder;

    MaterialDialog progress_dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pharmacies);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        byte[] byteArray=getIntent().getByteArrayExtra("image");
        bitmap= BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        Bundle b = getIntent().getExtras();
        my_lat=b.getString("my_lat");
        my_lng=b.getString("my_lng");
        radiant=b.getString("radi");
//        upload_image1=b.getString("UPLOAD_KEY1");
        user_id1=b.getString("USER_ID1");
        order_id1=b.getString("ORDER_ID1");

        listView=(ListView)findViewById(R.id.listView);

      //  Toast.makeText(this,upload_image1,Toast.LENGTH_LONG).show();

        //Retrieve Data from Server
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        adapter2=new MarkerAdapter(getApplicationContext(),0);
        getNearByPharmacies();
        listView.setAdapter(adapter2);

    }

    public void getNearByPharmacies()
    {

        RequestParams params = null;
        params=new RequestParams();
        params.put("lat",my_lat);
        params.put("lng",my_lng);
        params.put("rad", radiant);
        if(haveNetworkConnection()) {
            invokeWS(params);
        }
        else
        {
            internetFailureDialog();
        }
    }

    public void invokeWS(RequestParams params) {
        // Show Progress Dialog
        //prgDialog.show();
       // progress_dialog.show();
        builder=new MaterialDialog.Builder(this)
                .title(R.string.progress_dialog_nearby)
                .content(R.string.please_wait)
                .progress(true, 0);
        progress_dialog=builder.build();
        progress_dialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://thepharmacist.freeoda.com/findPharmacy.php", params, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Hide Progress Dialog
                // prgDialog.hide();

                progress_dialog.dismiss();
                String tag = null;
                String status = null;
                if (responseBody != null && responseBody.length > 0) {
                    try {
                        String s = new String(responseBody);
                        jsonObject = new JSONObject(s);
                        jsonArray = jsonObject.getJSONArray("server_response");
                        int i = 0;
                        StringBuffer buffer = new StringBuffer();
                        adapter2.listClear();
                        while (i < jsonArray.length()) {
                            JSONObject JO = jsonArray.getJSONObject(i);
                            String st = JO.getString("status");
                            if (statusCode == 200 && st.equals("ok")) {
                                markerDataProvider2 = new MarkerDataProvider(JO.getString("name"), JO.getString("address"),
                                        Float.parseFloat(JO.getString("lat")), Float.parseFloat(JO.getString("lng")), Float.parseFloat(JO.getString("distance")), JO.getString("id"));
                                adapter2.add(markerDataProvider2);
                                buffer.append("Taxi Name: " + JO.getString("name") + "\n");
                                buffer.append("Address : " + JO.getString("address") + "\n");
                                buffer.append("Lat : " + JO.getString("lat") + "\n");
                                buffer.append("Lng : " + JO.getString("lng") + "\n");
                                buffer.append("Distance(Km) : " + JO.getString("distance") + "\n");
                            } else {
                                buffer.append("You don't have data in the server..");
                            }
                            i++;
                        }
                        //showMessage("Taxi Details", buffer.toString());
                        //viewFromAdapter();
                        ;


                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();

                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // Hide Progress Dialog
                progress_dialog.dismiss();
                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    public void imageUpload()
    {
        if(haveNetworkConnection()) {
            uploadImage();
        }
        else
        {
            internetFailureDialog();
        }

    }

    public boolean getSelectedPharmaciesID()
    {

       // upload_image1=getStringImage(bitmap);

       // list.add("Java");
        //list.add("C");
        boolean isSelectPhar=false;
        list= new ArrayList<String>(); // Ordered collection
        MarkerDataProvider markerDP=null;
      //  StringBuffer responseText = new StringBuffer();
        for (int i = 0; i < adapter2.getCount(); i++) {
            markerDP = (MarkerDataProvider) adapter2.getItem(i);
            if(markerDP.isSelected()){

                list.add(markerDP.getPhar_id());
                isSelectPhar=true;
                //responseText.append("\n" + markerDP.getPhar_id());
            }
        }
        return isSelectPhar;


//        if(haveNetworkConnection()) {
//            if(list.isEmpty()) {
//                selectFailureDialog();
//            }
//            else
//            {

//            }
//
//        }
//        else
//        {
//            internetFailureDialog();
//        }

       // Toast.makeText(getApplicationContext(),
        //        responseText, Toast.LENGTH_LONG).show();
    }
    public void sendToPharmacy()
    {
        RequestParams params = null;
        params=new RequestParams();
        params.put("USER_ID",user_id1);
        params.put("ORDER_ID",order_id1);
        // params.put("image",upload_image1);
        params.put("pharmacy_ids", list);
        invokeWS1(params);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_pharmacy, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       // int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            getSelectedPharmaciesID();
//            imageUpload();
//
//            return true;
//        }
//        else if(id==android.R.id.home)
//        {
//            finish();
//        }
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
//            case R.id.action_settings:
//                if(haveNetworkConnection()) {
//                    if(!getSelectedPharmaciesID()) {
//                        selectFailureDialog();
//
//                    }
//                    else{
//                        sendToPharmacy();
//                        imageUpload();
//                    }
//                }
//                else
//                internetFailureDialog();
//
//                break;



        }
        return true;

       //return super.onOptionsItemSelected(item);
    }
    public void invokeWS1(RequestParams params) {
        // Show Progress Dialog
      //  prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://thepharmacist.freeoda.com/tester.php", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Hide Progress Dialog
               // prgDialog.hide();

                String tag = null;
                String status = null;
                if (responseBody != null && responseBody.length > 0) {
                    try {
                        String s = new String(responseBody);
                        jsonObject = new JSONObject(s);
                        jsonArray = jsonObject.getJSONArray("server_response");
                        int i = 0;
                        StringBuffer buffer = new StringBuffer();
                        while (i < jsonArray.length()) {
                            JSONObject JO = jsonArray.getJSONObject(i);
                            String st = JO.getString("status");
                            if (statusCode == 200 && st.equals("ok")) {

                                //Toast.makeText(getApplicationContext(), "Insert Success", Toast.LENGTH_LONG).show();

                            } else {
                                //Toast.makeText(getApplicationContext(), "Insert Fail", Toast.LENGTH_LONG).show();
                            }
                            i++;
                        }
                        //showMessage("Taxi Details", buffer.toString());
                        //viewFromAdapter();
                        ;


                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();

                    }
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // Hide Progress Dialog
                // prgDialog.hide();
                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    //Enocode image to String
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }


    //Upload image to server
    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;

            RequestHandler rh = new RequestHandler();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
               // loading = ProgressDialog.show(SelectPharmacies.this, "Uploading...", null,true,true);
                prgDialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
               // loading.dismiss();

                prgDialog.dismiss();
                finishDialogBox();
               // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                upload_image1 = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();
                data.put("USER_ID",user_id1);
                data.put("ORDER_ID",order_id1);
                data.put(UPLOAD_KEY, upload_image1);
                String result = rh.sendPostRequest(UPLOAD_URL,data);



                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();

        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public void internetFailureDialog()
    {
        // Display message in dialog box if you have not internet connection
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("No Internet Connection");
        alertDialogBuilder.setMessage("You are offline please check your internet connection");
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                //Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void selectFailureDialog()
    {
        // Display message in dialog box if you have not internet connection
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("No Selected Pharmacies");
        alertDialogBuilder.setMessage("You are not select the pharmacies!!");
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                //Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void senButtonClick(View view)
    {
        if(haveNetworkConnection()) {
            if(!getSelectedPharmaciesID()) {
                selectFailureDialog();

            }
            else{
                sendToPharmacy();
                imageUpload();


            }
        }
        else
            internetFailureDialog();
    }

    public void finishDialogBox()
    {
        new AlertDialogWrapper.Builder(this)
                .setTitle(R.string.title)
                .setMessage(R.string.message)
                .setCancelable(false)
                .setNegativeButton("Go To Home", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i=new Intent(getApplicationContext(),Home.class);
                        startActivity(i);

                    }
                })
                .setPositiveButton("Back To Map", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .show();
    }

}

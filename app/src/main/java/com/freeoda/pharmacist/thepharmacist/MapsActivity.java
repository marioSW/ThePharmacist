package com.freeoda.pharmacist.thepharmacist;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import cz.msebera.android.httpclient.Header;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    double latitude=0.0;
    double longitude=0.0;
    double de_latitude=0.0;
    double de_longitude=0.0;
    private LocationManager locationManager;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    private static final int REQUEST_PLACE_PICKER=1;

    //Retrieve data from server
    JSONObject jsonObject;
    JSONArray jsonArray;
    ProgressDialog prgDialog;
    MarkerDataProvider markerDataProvider1;
    MarkerAdapter adapter1;
    Marker marker=null;

    //New GPS Tracker
    //*GPSTrackerNew gpsTracker = null;


    GoogleApiClient mGoogleApiClient;


    //Search Location
   // PlaceAutocompleteFragment autocompleteFragment;

    //Get current location
    LocationManager lm;
    String provider;
    Location l;

    TextView name1,address1,attribute1;
    LinearLayout ly;
    ImageButton imclose;

    private GoogleMap mMap;

    private SeekBar seekBar;
    private TextView textView;
    String seekValue;

    String user_id=null;
    String order_id=null;
    String upload_image=null;

    Bitmap bitmap=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        byte[] byteArray=getIntent().getByteArrayExtra("image");
        bitmap=BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);

        Bundle b = getIntent().getExtras();
        user_id=b.getString("USER_ID");
        order_id=b.getString("ORDER_ID");
//        upload_image=b.getString("UPLOAD_KEY");
        //Retrieve Data from Server
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        adapter1=new MarkerAdapter(getApplicationContext(),0);
        initializeVariables();

        // create class object
        //*gpsTracker=new GPSTrackerNew(this);
        seekValue="4";
        initiateMap();
        // Initialize the textview with '0'.

        seekBar.setProgress(4);
        textView.setText("Search Distance: " + seekBar.getProgress() + " Km");

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                // Toast.makeText(getApplicationContext(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //  Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView.setText("Search Distance: " + seekBar.getProgress() + " Km");
                seekValue = Integer.toString(progress);
                // Toast.makeText(getApplicationContext(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();
                mMap.clear();
                initiateMap();
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        MarkerDataProvider markerDP=null;
        googleMap.clear();
        mMap = googleMap;




        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            //mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
            //buildGoogleApiClient();
        }



        // check if GPS enabled
        //if(gps.canGetLocation()) {
        //Toast.makeText(getApplicationContext(),Integer.toString(adapter1.getCount()), Toast.LENGTH_LONG).show();
        LatLng sydney2 = new LatLng(latitude,longitude);
        Bitmap largeIcon1 = BitmapFactory.decodeResource(getResources(), R.drawable.person_pinn);
        marker=mMap.addMarker(new MarkerOptions().position(sydney2).title("My Point").icon(BitmapDescriptorFactory.fromBitmap(largeIcon1)));

        //Toast.makeText(this,adapter1.getCount(),Toast.LENGTH_LONG).show();
        for (int i = 0; i < adapter1.getCount(); i++) {
            markerDP = (MarkerDataProvider) adapter1.getItem(i);
            // Add a marker in Sydney and move the camera

            LatLng sydney = new LatLng(markerDP.getLat(), markerDP.getLng());
            LatLng sydney1 = new LatLng(latitude,longitude);
            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.phar_pinn);
            marker=mMap.addMarker(new MarkerOptions().position(sydney).title(markerDP.getName() + "Distance(Km): " + markerDP.getDistance()).icon(BitmapDescriptorFactory.fromBitmap(largeIcon)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney1, 15));

        }
        //}

        // else{
        // can't get location
        // GPS or Network is not enabled
        // Ask user to enable GPS/network in settings
        //   gps.showSettingsAlert();
        //}

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                initiateMap();
                return false;
            }
        });
    }


    public void initiateMap()
    {
        if(haveNetworkConnection()) {
            if (checkGPS()) {
                findGeoLocation();
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
                }

                getVechile();
            } else {
                GPSValidater();
            }
        }
        else
        {
            internetFailureDialog();
        }


    }

    public void getVechile()
    {

        RequestParams params = null;
        params=new RequestParams();
        params.put("lat", Double.toString(latitude));
        params.put("lng", Double.toString(longitude));
        params.put("rad", seekValue);
       // Toast.makeText(getApplicationContext(),seekValue,Toast.LENGTH_LONG).show();
        invokeWS(params);
    }
    public void invokeWS(RequestParams params) {
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://thepharmacist.freeoda.com/findPharmacy.php", params, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Hide Progress Dialog
                prgDialog.hide();
                String tag = null;
                String status = null;
                if (responseBody != null && responseBody.length > 0) {
                    try {
                        String s = new String(responseBody);
                        jsonObject = new JSONObject(s);
                        jsonArray = jsonObject.getJSONArray("server_response");
                        int i = 0;
                        StringBuffer buffer = new StringBuffer();
                        adapter1.listClear();
                        while (i < jsonArray.length()) {
                            JSONObject JO = jsonArray.getJSONObject(i);
                            String st = JO.getString("status");
                            if (statusCode == 200 && st.equals("ok")) {
                                markerDataProvider1 = new MarkerDataProvider(JO.getString("name"), JO.getString("address"),
                                        Float.parseFloat(JO.getString("lat")), Float.parseFloat(JO.getString("lng")), Float.parseFloat(JO.getString("distance")), JO.getString("id"));

                                adapter1.add(markerDataProvider1);
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

                        // GoogleMap mMap1=null;
                        mMap.clear();
                        marker.remove();

                        onMapReady(mMap);


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
                prgDialog.hide();
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
    public void findGeoLocation()
    {

            lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            Criteria c = new Criteria();

            provider = lm.getBestProvider(c, false);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                return;
            }
            l = lm.getLastKnownLocation(provider);
            if (l != null) {
                //get latitude and longitude of the location
                longitude = l.getLongitude();
                latitude = l.getLatitude();
                //display on text view
                //Toast.makeText(this, latitude + " " + longitude, Toast.LENGTH_LONG).show();

            }


    }


    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        mMap.animateCamera(cameraUpdate);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(this);
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    // A private method to help us initialize our variables.
    private void initializeVariables() {
        seekBar = (SeekBar) findViewById(R.id.seekBar1);
        textView = (TextView) findViewById(R.id.textView1);
    }

    public void transferEvent(View view)
    {
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray=stream.toByteArray();
        Intent i=new Intent(this,SelectPharmacies.class);
        Bundle b=new Bundle();
        b.putString("my_lat",Double.toString(latitude));
        b.putString("my_lng",Double.toString(longitude));
        b.putString("radi",seekValue);
        b.putString("USER_ID1",user_id);
        b.putString("ORDER_ID1",order_id);
        i.putExtra("image",byteArray);
        i.putExtras(b);
        startActivity(i);

    }

    public boolean checkGPS()
    {
        boolean isCheck=true;
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // Check if enabled and if not send user to the GPS settings
        if (!enabled) {
           isCheck=false;
        }
        else{
            isCheck=true;
        }
        return isCheck;
    }

    public void GPSValidater()
    {

        LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Ask the user to enable GPS
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Location Manager");
            builder.setMessage("Would you like to enable GPS?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Launch settings, allowing user to make a change
//                    Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                    startActivity(i);
                    startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //No location service, no Activity
                    finish();
                }
            });
            builder.create().show();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            switch (requestCode) {
                case 1:
                    initiateMap();
                break;
            }
        }
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
}

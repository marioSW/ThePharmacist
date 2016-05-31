package com.freeoda.pharmacist.thepharmacist;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.freeoda.pharmacist.thepharmacist.captureimage.RequestHandler;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

//import com.adobe.creativesdk.aviary.AdobeImageIntent;


public class ImageDisplay extends AppCompatActivity implements OnMenuItemClickListener, OnMenuItemLongClickListener  {

    //Camera and Gallery Action
    static final int CAM_REQUEST=3;
    static final int SELECT_PICTURE = 4;
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    String pathOfImage=null;
    private String selectedImagePath=null;
    //end



    ImageView imV;
    String path=null;
    String galleryPath=null;
    String pathFromBack=null;
    EditText orderID=null;
    EditText userID=null;

    public static final String UPLOAD_URL = "http://thepharmacist.freeoda.com/uploadScript.php";
    public static final String UPLOAD_KEY = "image";
    public Bitmap bitmap;
    String user_id;

    private FragmentManager fragmentManager;
    private ContextMenuDialogFragment mMenuDialogFragment;

    LinearLayout androidDropDownMenuIconItem;

    String uploadImage=null;


    //Get current location
    LocationManager lm;
    String provider;
    Location l;
    double latitude=0.0;
    double longitude=0.0;

    JSONObject jsonObject;
    JSONArray jsonArray;
    ProgressDialog prgDialog;
    String OrderID=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
        fragmentManager = getSupportFragmentManager();
        initToolbar();
        initMenuFragment();
       // imf=new ImageFragment();
       // addFragment(imf, true, R.id.container);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        androidDropDownMenuIconItem = (LinearLayout) findViewById(R.id.horizontal_dropdown_icon_menu_items);
        androidDropDownMenuIconItem.bringToFront();
        androidDropDownMenuIconItem.setVisibility(View.VISIBLE);

        //Get the bundle
        Bundle bundle = getIntent().getExtras();
        bitmap=null;
        //Extract the data…
        path =bundle.getString("filePath");
        galleryPath=bundle.getString("imageUri");
        pathFromBack=bundle.getString("previousBackPath");

       // userID=(EditText)findViewById(R.id.userId1);
        imV=(ImageView)findViewById(R.id.image_for_upload);
        if(path!=null||galleryPath!=null) {

            moveToPhotoEditor();
        }
        else
        {
            Uri imageUriFromBack = Uri.parse(pathFromBack);
            imV.setImageURI(imageUriFromBack);
        }

        //Retrieve Data from Server
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);


    }

    public void moveToPhotoEditor()
    {
        if(path!=null) {
            //   bitmap=setPic(path);
            // Uri imageUri = Uri.parse("http://my-site.com/my-image.jpg");
            Uri imageUri1 = Uri.parse(path);
            imV.setImageURI(imageUri1);
            setBitmap();
            ChangeRotation(path);
            imV.setImageBitmap(bitmap);

//            Intent imageEditorIntent1 = new AdobeImageIntent.Builder(this)
//                    .setData(imageUri1)
//                    .withAccentColor(R.color.colorPrimaryDark)
//                    .build();
//
//            startActivityForResult(imageEditorIntent1,2);
        }
        else
        {

            Uri imageUri = Uri.parse(galleryPath);
            imV.setImageURI(imageUri);
            setBitmap();
            ChangeRotation(galleryPath);
            imV.setImageBitmap(bitmap);

//            Intent imageEditorIntent = new AdobeImageIntent.Builder(this)
//                    .setData(imageUri)
//                    .withAccentColor(R.color.colorPrimaryDark)
//                    .build();
//
//            startActivityForResult(imageEditorIntent, 1);

        }
        getStringImage(bitmap);
        user_id="CHECK_USER_001";
        uploadImage = getStringImage(bitmap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_image, menu);
        return true;
        //getMenuInflater().inflate(R.menu.menu_image, menu);
        //return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
          //  return true;
        //}

        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.context_menu:
                if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mMenuDialogFragment != null && mMenuDialogFragment.isAdded()) {
            mMenuDialogFragment.dismiss();
            Toast.makeText(this, "Back pressed on", Toast.LENGTH_SHORT).show();
        } else{
            this.finish();
           // moveToPhotoEditor();
        }
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {

       // Toast.makeText(this, "Clicked on position: " + position, Toast.LENGTH_SHORT).show();
        if(position==1)
        {
//            getStringImage(bitmap);
//          user_id="TIGER0003";
//          uploadImage = getStringImage(bitmap);
//          uploadImage(user_id);
            if(haveNetworkConnection()) {
                if(checkGPS()) {
                    OrderID=generateOrderId();
                    autoSendPrescription();
                    user_id = "CHECK_USER_001";
                    uploadImage = getStringImage(bitmap);
                    uploadImage(user_id,OrderID);
                }
                else {
                    GPSValidater();
                }
            }
            else
            {
                internetFailureDialog();
            }

        }
        else if(position==2)
        {
            cameraAction();
        }
        else if(position==3)
        {
            galleryAction();


        }
        else if(position==4)
        {

        }
        else if(position==5)
        {
            moveToPhotoEditor();
        }
        else if(position==6)
        {
//            Intent i=new Intent(this,GridViewImage.class);
//            startActivity(i);
        }
        else if(position==7)
        {
            this.finish();
        }


    }

    @Override
    public void onMenuItemLongClick(View clickedView, int position) {
        Toast.makeText(this, "Long clicked on position: " + position, Toast.LENGTH_SHORT).show();

    }

    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(false);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(this);
        mMenuDialogFragment.setItemLongClickListener(this);
    }
    private List<MenuObject> getMenuObjects() {
        // You can use any [resource, bitmap, drawable, color] as image:
         //item.setResource(...)
        // item.setBitmap(...)
        // item.setDrawable(...)
        // item.setColor(...)
        // You can set image ScaleType:
        // item.setScaleType(ScaleType.FIT_XY)
        // You can use any [resource, drawable, color] as background:
        // item.setBgResource(...)
        // item.setBgDrawable(...)
        // item.setBgColor(...)
        // You can use any [color] as text color:
        // item.setTextColor(...)
        // You can set any [color] as divider color:
        // item.setDividerColor(...)

        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResource(R.drawable.close_ping_icon);

        MenuObject send = new MenuObject("Auto Send Prescriptions");
        send.setResource(R.drawable.send_image_icon);

        MenuObject like = new MenuObject("Capture from camera");
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.camera_ping_icon);
        like.setBitmap(b);

        MenuObject addFr = new MenuObject("Capture From Gallery");
        BitmapDrawable bd = new BitmapDrawable(getResources(),
                BitmapFactory.decodeResource(getResources(), R.drawable.gallery_pink_icon));
        addFr.setDrawable(bd);

        MenuObject addFav = new MenuObject("Favourite Pharmacies");
        addFav.setResource(R.drawable.pharmacy_icon);

        MenuObject block = new MenuObject("Back to photo editor");
        block.setResource(R.drawable.edit_image_icon);

        MenuObject Prescriptions = new MenuObject("Previous prescriptions");
        Prescriptions.setResource(R.drawable.image_list_icon);

        MenuObject exit = new MenuObject("Exit");
        exit.setResource(R.drawable.exit_app_icon);

        menuObjects.add(close);
        menuObjects.add(send);
        menuObjects.add(like);
        menuObjects.add(addFr);
        menuObjects.add(addFav);
        menuObjects.add(block);
        menuObjects.add(Prescriptions);
        menuObjects.add(exit);
        return menuObjects;
    }
    private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mToolBarTextView = (TextView) findViewById(R.id.text_view_toolbar_title);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
      //  mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.right_arrow_icon));
      //  mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
      //      @Override
        //    public void onClick(View v) {
                //What to do on back clicked
          //      onBackPressed();
          //  }
       // });

        mToolBarTextView.setText("The Pharmacist");
    }


//    public static Bitmap rotateImage(Bitmap source, float angle) {
//        Bitmap retVal;
//
//        Matrix matrix = new Matrix();
//        matrix.postRotate(angle);
//        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
//
//        return retVal;
//    }

    //Upload image to server
    private void uploadImage(String u_id,String order_id){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ImageDisplay.this, "Uploading...", null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                uploadImage = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();
                data.put("USER_ID",u_id);
                data.put("ORDER_ID",order_id);
                data.put(UPLOAD_KEY, uploadImage);
                String result = rh.sendPostRequest(UPLOAD_URL,data);


                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

    //Enocode image to String
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                /* 4) Make a case for the request code we passed to startActivityForResult() */
                case 1:
                    //Show image to image Viewer and case 1 for Gallery
//                    Uri editedImageUri = data.getData();
//                    imV.setImageURI(editedImageUri);
                    //setBitmap();
                    findGeoLocation();
                    break;
                case 2:
                    //Show image to image Viewer and case 2 for Camera
//                    imV.setImageURI(data.getData());
                    setBitmap();
                    break;
                case CAM_REQUEST:
                    Uri imageUri1 = Uri.parse(pathOfImage);
                    imV.setImageURI(imageUri1);
                    setBitmap();
                    ChangeRotation(pathOfImage);
                    imV.setImageBitmap(bitmap);
//                    Intent imageEditorIntent1 = new AdobeImageIntent.Builder(this)
//                            .setData(imageUri1)
//                            .withAccentColor(R.color.colorPrimaryDark)
//                            .build();
//                    startActivityForResult(imageEditorIntent1, 2);
                    break;
                case SELECT_PICTURE:
                    Uri selectedImageUri = data.getData();
                    Uri imageUri = Uri.parse(selectedImageUri.toString());
                    imV.setImageURI(imageUri);
                    setBitmap();
                    ChangeRotation(selectedImageUri.toString());
                    imV.setImageBitmap(bitmap);

//                    Intent imageEditorIntent = new AdobeImageIntent.Builder(this)
//                            .setData(imageUri)
//                            .withAccentColor(R.color.colorPrimaryDark)
//                            .build();
//
//                    startActivityForResult(imageEditorIntent, 1);
                    break;

                default:
                    break;
            }
        }
    }

    public void setBitmap()
    {
        bitmap =((BitmapDrawable)imV.getDrawable()).getBitmap();
        bitmap=getResizedBitmap(bitmap,640);
    }
    public Bitmap getResizedBitmap(Bitmap imag,int maxSize)
    {
        int width=imag.getWidth();
        int height=imag.getHeight();
        float bitmapRatio=(float)width/(float)height;
        if(bitmapRatio>1)
        {
            width=maxSize;
            height=(int)(width / bitmapRatio);
        }
        else{
            height=maxSize;
            width=(int)(height*bitmapRatio);

        }

        return Bitmap.createScaledBitmap(imag,width,height,true);

    }

    public String generateOrderId()
    {
        String OrderIdTimeStamp = "ORD_"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+user_id;
        return OrderIdTimeStamp;
    }


    public void horizontalDropDownIconMenu(View view) {
        if (androidDropDownMenuIconItem.getVisibility() == View.VISIBLE) {
            androidDropDownMenuIconItem.setVisibility(View.INVISIBLE);
        } else {
            androidDropDownMenuIconItem.setVisibility(View.VISIBLE);
        }
    }

    public void menuItemClick(View view) {
        androidDropDownMenuIconItem.setVisibility(View.VISIBLE);
    }

    public void cameraClickAction(View view)
    {
        androidDropDownMenuIconItem.setVisibility(View.VISIBLE);
        cameraAction();
    }
    public void galleryClickAction(View view)
    {
        androidDropDownMenuIconItem.setVisibility(View.VISIBLE);
        galleryAction();
    }

    public void cameraAction()
    {
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = null;
        file = createImageFile();
        camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(camera_intent, CAM_REQUEST);

    }
    public void galleryAction()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }

    private File createImageFile()
    {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_"+JPEG_FILE_SUFFIX;
        File folder=new File("/sdcard/the_pharmacist");
        pathOfImage="/sdcard/the_pharmacist/"+imageFileName;
        if(!folder.exists())
        {
            folder.mkdir();
        }

        File image_file=new File(folder,imageFileName);
        return image_file;
    }

    public boolean isFileRemove(String filePath)
    {
        boolean deleted=false;
        File file = new File(filePath);
        deleted = file.delete();
        return deleted;
    }

    public void transferEvent(View view)
    {
        androidDropDownMenuIconItem.setVisibility(View.VISIBLE);
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray=stream.toByteArray();


        Intent i=new Intent(this,MapsActivity.class);
        Bundle b=new Bundle();
        b.putString("USER_ID",user_id);
        b.putString("ORDER_ID",generateOrderId());
        i.putExtra("image",byteArray);
        i.putExtras(b);
        startActivity(i);
    }

    public void ChangeRotation(String path)
    {
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

        switch(orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotateImage(bitmap,90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotateImage(bitmap,180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotateImage(bitmap,270);
                break;
            // etc.
        }
    }
    public void rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        bitmap=retVal;
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

    public void autoSendPrescription()
    {

        if(haveNetworkConnection()) {
            if(checkGPS()) {
                findGeoLocation();
                RequestParams params = null;
                params=new RequestParams();
                String str_lat=String.format("%.6f",latitude);
                String str_lng=String.format("%.6f",longitude);
                params.put("lat",str_lat);
                params.put("lng", str_lng);
                params.put("ORDER_ID",OrderID);
                //Toast.makeText(this,str_lat+" "+str_lng+" "+generateOrderId(),Toast.LENGTH_LONG).show();
                invokeWS(params);
            }
            else{
                GPSValidater();
            }
        }
        else
        {
            internetFailureDialog();
        }
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



    public void findGeoLocation()
    {

        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria c = new Criteria();

        provider = lm.getBestProvider(c, false);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    public void invokeWS(RequestParams params) {
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://thepharmacist.freeoda.com/requesthandle/RequestRoute.php", params, new AsyncHttpResponseHandler() {

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
                       // StringBuffer buffer = new StringBuffer();
                        while (i < jsonArray.length()) {
                            JSONObject JO = jsonArray.getJSONObject(i);
                            String st = JO.getString("status");
                            if (statusCode == 200 && st.equals("ok")) {

                                Toast.makeText(getApplicationContext(), "Successfully Send to nearby pharmacies", Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(getApplicationContext(), "Send Fail", Toast.LENGTH_LONG).show();
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


}

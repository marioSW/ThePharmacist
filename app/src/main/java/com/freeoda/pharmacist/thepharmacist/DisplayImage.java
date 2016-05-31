package com.freeoda.pharmacist.thepharmacist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

//import com.adobe.creativesdk.aviary.AdobeImageIntent;
import com.freeoda.pharmacist.thepharmacist.captureimage.RequestHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class DisplayImage extends AppCompatActivity {

    ImageView imV;
    String path=null;
    String galleryPath=null;
    EditText orderID=null;
    EditText userID=null;

    public static final String UPLOAD_URL = "http://thepharmacist.freeoda.com/uploadScript.php";
    public static final String UPLOAD_KEY = "image";
    public Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        //Get the bundle
        Bundle bundle = getIntent().getExtras();
        bitmap=null;
        //Extract the data…
        path =bundle.getString("filePath");
        galleryPath=bundle.getString("imageUri");
      //  userID=(EditText)findViewById(R.id.userId);
       // orderID=(EditText)findViewById(R.id.orderId);
        imV=(ImageView)findViewById(R.id.image_view1);
        if(path!=null) {
            //   bitmap=setPic(path);
            // Uri imageUri = Uri.parse("http://my-site.com/my-image.jpg");
//            Uri imageUri1 = Uri.parse(path);
//            Intent imageEditorIntent1 = new AdobeImageIntent.Builder(this)
//                    .setData(imageUri1)
//                    .build();
//
//            startActivityForResult(imageEditorIntent1,2);
        }
        else
        {

            //  bitmap=setGallery(galleryPath);
            //   Toast.makeText(getApplicationContext(),galleryPath,Toast.LENGTH_LONG).show();
            // Bundle bundle = getIntent().getExtras();
            // Uri imageUri = Uri.parse("http://my-site.com/my-image.jpg");
            Uri imageUri = Uri.parse(galleryPath);

//            Intent imageEditorIntent = new AdobeImageIntent.Builder(this)
//                    .setData(imageUri)
//                    .build();
//
//            startActivityForResult(imageEditorIntent, 1);

        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private Bitmap setPic(String mCurrentPhotoPath) {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
        int targetW = imV.getWidth();
        int targetH = imV.getHeight();

		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        }

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
        bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		/* Associate the Bitmap to the ImageView */
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(mCurrentPhotoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Bitmap bit=null;
        switch(orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                bit=rotateImage(bitmap, 90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                bit=rotateImage(bitmap, 180);
                break;
            // etc.
        }

        return bit;

    }

    private Bitmap setGallery(String mCurrentPhotoPath) {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
        int targetW = imV.getWidth();
        int targetH = imV.getHeight();

		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        }

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		/* Associate the Bitmap to the ImageView */
        imV.setImageBitmap(bitmap);

        return bitmap;




    }
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       /* int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);*/

        switch(item.getItemId())
        {
            case R.id.input_id:
                getStringImage(bitmap);
                String user_id=userID.getText().toString();
                String order_id=orderID.getText().toString();
                uploadImage(user_id,order_id);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Upload image to server
    private void uploadImage(String u_id,String or_id){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(DisplayImage.this, "Uploading...", null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();
                data.put("USER_ID",u_id);
                data.put("ORDER_ID",or_id);
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
                    Uri editedImageUri = data.getData();
                    imV.setImageURI(editedImageUri);
                    setBitmap();
                    break;
                case 2:
                    //Show image to image Viewer and case 2 for Camera
                    imV.setImageURI(data.getData());
                    setBitmap();
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

}

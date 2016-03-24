package com.freeoda.pharmacist.thepharmacist;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Home extends AppCompatActivity {

    Button button;
    Button gallery;
   // ImageView imageView;
    static final int CAM_REQUEST=1;
    static final int SELECT_PICTURE = 2;
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    String path=null;
    private String selectedImagePath=null;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        button=(Button)findViewById(R.id.button2);
        //imageView=(ImageView)findViewById(R.id.image_view);
        gallery=(Button)findViewById(R.id.button3);

        gallery.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // in onCreate or any event where your want the user to
                        // select a file
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,
                                "Select Picture"), SELECT_PICTURE);
                    }
                }
        );

        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File file = null;
                        file = createImageFile();
                        camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                        startActivityForResult(camera_intent, CAM_REQUEST);
                    }
                }
        );
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



    private File createImageFile()
    {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_"+JPEG_FILE_SUFFIX;
        File folder=new File("/sdcard/the_pharmacist");
        path="/sdcard/the_pharmacist/"+imageFileName;
        if(!folder.exists())
        {
            folder.mkdir();
        }

        File image_file=new File(folder,imageFileName);
        return image_file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
       // String path="/sdcard/the_pharmacist/came_image.jpg";
        if((resultCode == RESULT_OK)) {
            if(requestCode==CAM_REQUEST){
            //setPic(path);
            Intent i = new Intent(this, DisplayImage.class);
            //Create the bundle
            Bundle bundle = new Bundle();
            bundle.putString("filePath", path);
            i.putExtras(bundle);
            startActivity(i);
            }
           else if(requestCode==SELECT_PICTURE)
            {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                //setPic(selectedImagePath);
                Intent i = new Intent(this, DisplayImage.class);
                //Create the bundle
                Bundle bundle = new Bundle();
                bundle.putString("galleryPath",selectedImagePath);
                i.putExtras(bundle);
                startActivity(i);
            }
        }
    }
    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }


    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
    }



}
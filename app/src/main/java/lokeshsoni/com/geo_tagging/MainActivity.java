package lokeshsoni.com.geo_tagging;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.R.attr.bitmap;

public class MainActivity extends AppCompatActivity {


    Button read,set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      //  read = (Button)findViewById(R.id.main_read);
        set = (Button)findViewById(R.id.main_set);


        InputStream inputStream = getResources().openRawResource(R.raw.nw);
        CSVFile csvFile = new CSVFile(inputStream);
        List scoreList = csvFile.read();


        String[] score = (String[]) scoreList.get(0);
        Location location = new Location("");

        location.setLatitude(Double.parseDouble(score[7]));
        location.setLongitude(Double.parseDouble(score[8]));

        //Copying File For Now So Can Test This app

        Bitmap bitMap = BitmapFactory.decodeResource(getResources(),R.raw.sqr);
        File mFile1 = Environment.getExternalStorageDirectory();
        String fileName ="sqr.jpg";
        File mFile2 = new File(mFile1,fileName);
        try {
            FileOutputStream outStream;
            outStream = new FileOutputStream(mFile2);
            bitMap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String input = mFile1.getAbsolutePath()+"/"+fileName;

        // Tagging Image Latitude and Longitude
        if(isStoragePermissionGranted()) {
            MarkGeoTagImage(input, location);
        }

        //Reading Tag
        Log.d("TAG ON IMAGE", ReadExif(input));

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MapsActivity.class));
            }
        });


    }


    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Tag","Permission is granted");
                return true;
            } else {

                Log.v("Tag","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                System.out.println("Permission is granted");
                return false;
            }

        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Tag","Permission is granted");
            return true;
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v("Tag", "Permission: " + permissions[0] + "was " + grantResults[0]);
            System.out.println("Permission granted nice");

        }
    }


    public void MarkGeoTagImage(String imagePath,Location location)
    {
        try {
            ExifInterface exif = new ExifInterface(imagePath);
            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, GPS.convert(location.getLatitude()));
            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, GPS.latitudeRef(location.getLatitude()));
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, GPS.convert(location.getLongitude()));
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, GPS.longitudeRef(location.getLongitude()));
            SimpleDateFormat fmt_Exif = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
            exif.setAttribute(ExifInterface.TAG_DATETIME,fmt_Exif.format(new Date(location.getTime())));
            exif.saveAttributes();


        } catch (IOException e) {
            Log.e("Exif Err", e.getLocalizedMessage());
        }
    }


    String ReadExif(String file){
        String exif="Exif: " + file;
        try {
            ExifInterface exifInterface = new ExifInterface(file);
            exif += "\n DATETIME: " + exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
            exif += "\n TAG_GPS_LATITUDE: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            exif += "\n TAG_GPS_LATITUDE_REF: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            exif += "\n TAG_GPS_LONGITUDE: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            exif += "\n TAG_GPS_LONGITUDE_REF: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return exif;
    }
}

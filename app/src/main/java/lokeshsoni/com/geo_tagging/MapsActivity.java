package lokeshsoni.com.geo_tagging;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String[] images = {"chrome.png","facebook.png","p.pmg","youtube.png"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        InputStream inputStream = getResources().openRawResource(R.raw.nw);
        CSVFile csvFile = new CSVFile(inputStream);
        List scoreList = csvFile.read();


        String[] score ;

        LatLng imageLocation;
        int height = 700;
        int width = 700;
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.img);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


         for(int i=7;i<15;i++) {
             score= (String[]) scoreList.get(i);
             imageLocation = new LatLng(Double.parseDouble(score[7]), Double.parseDouble(score[8]));
             mMap.addMarker(new MarkerOptions().position(imageLocation).title("Marker in ImageLocation").icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
             mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(imageLocation, 21));

         }

    }
}

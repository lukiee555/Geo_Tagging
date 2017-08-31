package lokeshsoni.com.geo_tagging;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.InputStream;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

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
        String[] score = (String[]) scoreList.get(0);
//        Location location = new Location("");
//
//        location.setLatitude(Double.parseDouble(score[7]));
//        location.setLongitude(Double.parseDouble(score[8]));


        LatLng imageLocation = new LatLng(Double.parseDouble(score[7]), Double.parseDouble(score[8]));

        mMap.addMarker(new MarkerOptions().position(imageLocation).title("Marker in ImageLocation"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(imageLocation, 12.0f));

    }
}

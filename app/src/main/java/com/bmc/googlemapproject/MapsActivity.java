package com.bmc.googlemapproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                requestPermissions(new String[]
                        {
                                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
                        }, 100);
                return;
            }
        } else {
           // configureButton();
        }
        mMap.setMyLocationEnabled(true);
    }


    public void onSearch(View view)
    {
        EditText searchEditText=(EditText)findViewById(R.id.search);
        EditText latlongEditText=(EditText)findViewById(R.id.latitudelongitude);

        String locationString=searchEditText.getText().toString();

        List<Address> listofAddresses=null;

        if(locationString!=null && locationString!="")
        {
            Geocoder geocoder=new Geocoder(this);

            try {
                 listofAddresses = geocoder.getFromLocationName(locationString,1);
            } catch(IOException e){}

            Address address=listofAddresses.get(0);
            latlongEditText.setText("lat ="+address.getLatitude()+"  long ="+address.getLongitude());

            LatLng latLng=new LatLng(address.getLatitude(),address.getLongitude());

            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in "+locationString));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

        }
        else {
            Toast.makeText(this, " no result found ", Toast.LENGTH_SHORT);
        }

    }


    public void changeMapType(View view)
    {
        if(mMap.getMapType()==GoogleMap.MAP_TYPE_NORMAL)
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        else
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }


    public void onZoom(View view)
    {
        if(view.getId()==R.id.zoomin)
        {
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
        }
        if(view.getId()==R.id.zoomout)
        {
            mMap.animateCamera(CameraUpdateFactory.zoomOut());
        }
    }

}

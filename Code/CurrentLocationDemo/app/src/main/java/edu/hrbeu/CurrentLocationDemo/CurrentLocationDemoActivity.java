package edu.hrbeu.CurrentLocationDemo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

public class CurrentLocationDemoActivity extends Activity {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET };
       
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        String serviceString = Context.LOCATION_SERVICE;
        LocationManager locationManager = (LocationManager) getSystemService(serviceString);

        String provider = LocationManager.GPS_PROVIDER;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }

        Location location = locationManager.getLastKnownLocation(provider);

        getLocationInfo(location);

        locationManager.requestLocationUpdates(provider, 2000, 0, locationListener);
    }

    private void getLocationInfo(Location location){
        String latLongInfo;
        TextView locationText = (TextView)findViewById(R.id.label);

        if (location != null){
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            latLongInfo = "Lat: " + lat + "\nLong: " + lng;
        }
        else{
            latLongInfo = "No location found";
        }

        locationText.setText("Your Current Position is:\n" + latLongInfo);
    }

    private final LocationListener locationListener = new LocationListener(){

        @Override
        public void onLocationChanged(Location location) {
            getLocationInfo(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            getLocationInfo(null);
        }

        @Override
        public void onProviderEnabled(String provider) {
            getLocationInfo(null);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    };

}
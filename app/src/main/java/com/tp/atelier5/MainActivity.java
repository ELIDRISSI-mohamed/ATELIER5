package com.tp.atelier5;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity implements LocationListener {

    static Logger logger = Logger.getLogger(String.valueOf(MainActivity.class));
    private Button button;
    private TextView textView;
    private LocationManager locationManager;
    private LocationListener listener;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView= (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.button);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // first check for permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            logger.info("SDK IFFFFFF");
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
        }
        button.setOnClickListener(e->{
            logger.info("btn clicked");
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location==null){
                String provider = null;

                if ( locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                    logger.info("GPS PRO");
                    provider = LocationManager.GPS_PROVIDER;
                } else {
                    logger.info("NETWORK PRO");
                    if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                        provider = LocationManager.NETWORK_PROVIDER;
                    } else {
                        logger.info("ELSE STATEMENT");
                    }
                }
                if(provider != null) {
                    logger.info("NOT NULL "+provider);
                    locationManager.requestLocationUpdates(provider, 0, 0, this);
                }
            } else{
                String latitude = String.valueOf(location.getLatitude());
                String longitude = String.valueOf(location.getLongitude());
                logger.info("latitude: "+latitude+" longitude: "+longitude);
            }
        });
    }
    @Override
    public void onLocationChanged(Location location) {
        logger.info("location changed");
        textView.setText("Latitude:" + location.getLatitude() + ", \nLongitude:" + location.getLongitude());
    }
    @Override
    public void onProviderDisabled(String provider) {
        logger.info("Provider disable");
    }
    @Override
    public void onProviderEnabled(String provider) {
        logger.info("Provider enable");
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        logger.info("Status changed");
    }
}
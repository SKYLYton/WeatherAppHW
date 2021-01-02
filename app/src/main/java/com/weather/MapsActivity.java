package com.weather;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.weather.services.GPSTracker;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = MapsActivity.class.getSimpleName();

    private static final int PERMISSIONS_REQUEST_ACCESS_LOCATION = 0;

    private GoogleMap mMap;
    private GPSTracker gpsTracker;
    private Marker currentLocation;
    private boolean accessLocation;
    private boolean isGPSLocationOn = true;
    private FloatingActionButton floatingActionButtonChoose;
    private FloatingActionButton floatingActionButtonLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initGPSTracker();
        initControls();
        accessLocation = checkPermission();

    }

    private void initControls() {
        floatingActionButtonChoose = findViewById(R.id.floatingActionButtonChoose);
        floatingActionButtonLocation = findViewById(R.id.floatingActionButtonLocation);

        floatingActionButtonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(Constants.EXTRA_LAT, currentLocation.getPosition().latitude);
                intent.putExtra(Constants.EXTRA_LNG, currentLocation.getPosition().longitude);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        floatingActionButtonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGPSLocationOn = true;

                if(!gpsTracker.isLocationKnown()){
                    mMap.clear();
                    return;
                }

                LatLng latLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
                floatingActionButtonChoose.setVisibility(View.VISIBLE);
                floatingActionButtonLocation.setVisibility(View.GONE);
                if (currentLocation == null) {
                    currentLocation = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                } else {
                    currentLocation.setPosition(latLng);
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f));
            }
        });
    }

    private void initGPSTracker() {
        gpsTracker = new GPSTracker(getApplicationContext());
        gpsTracker.setOnLocationUpdate(new GPSTracker.OnLocationUpdate() {
            @Override
            public void OnUpdate(Location location, LatLng latLng) {
                if (!isGPSLocationOn) {
                    return;
                }

                if (currentLocation == null) {
                    floatingActionButtonChoose.setVisibility(View.VISIBLE);
                    floatingActionButtonLocation.setVisibility(View.GONE);
                    currentLocation = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                } else {
                    currentLocation.setPosition(latLng);
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f));
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                floatingActionButtonLocation.setVisibility(View.VISIBLE);
                isGPSLocationOn = false;
                if (currentLocation == null) {
                    currentLocation = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                } else {
                    currentLocation.setPosition(latLng);
                }
            }
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                floatingActionButtonLocation.setVisibility(View.VISIBLE);
                isGPSLocationOn = false;
            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

            }
        });
    }

    private boolean checkPermission() {

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION},
                PERMISSIONS_REQUEST_ACCESS_LOCATION);

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_LOCATION) {
            if (grantResults.length == 2 &&
                    (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {

                gpsTracker.init();
                accessLocation = true;
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        gpsTracker.stopUsingGPS();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!gpsTracker.canGetLocation()) {
            gpsTracker.init();
        }
    }
}
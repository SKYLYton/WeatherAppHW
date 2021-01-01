package com.weather.services;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;

import static android.content.Context.LOCATION_SERVICE;

public class GPSTracker {
    private OnLocationUpdate onLocationUpdate;

    public void setOnLocationUpdate(OnLocationUpdate onLocationUpdate) {
        this.onLocationUpdate = onLocationUpdate;
    }

    public interface OnLocationUpdate {
        void OnUpdate(Location location, LatLng latLng);
    }

    private final Context mContext;
    private boolean canGetLocation = false;
    private Location location;
    private double latitude;
    private double longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 20;
    private static final long MIN_TIME_BW_UPDATES = 5000;
    private final LocationManager locationManager;

    public GPSTracker(Context context) {
        this.mContext = context;
        locationManager = (LocationManager) mContext
                .getSystemService(LOCATION_SERVICE);

        init();
    }

    public Location getLocation() {
        if (location == null) {
            init();
        }

        return location;
    }

    public void init() {

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        String provider = locationManager.getBestProvider(criteria, true);

        if (provider == null) {
            return;
        }

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager.requestLocationUpdates(provider,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES,
                locationListener);

        canGetLocation = true;
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            GPSTracker.this.location = location;
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            if (onLocationUpdate != null) {
                onLocationUpdate.OnUpdate(location, new LatLng(latitude, longitude));
            }
        }
    };

    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
        canGetLocation = false;
    }

    public boolean isGpsEnable() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public boolean isLocationKnown(){
        return location != null;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }
}
package com.diss.cabadvertisement.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.diss.cabadvertisement.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
//import com.diss.cabadvertisement.ui.CustomMapFragment;
import com.diss.cabadvertisement.ui.MapWrapperLayout.OnDragListener;
import com.google.android.gms.maps.model.MarkerOptions;


public class PickLocation extends Activity implements OnDragListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,View.OnClickListener {

    // Google Map
    private GoogleMap mGoogleMap;
    private CustomMapFragment mCustomMapFragment;

    private View mMarkerParentView;
    private ImageView mMarkerImageView;

    private int imageParentWidth = -1;
    private int imageParentHeight = -1;
    private int imageHeight = -1;
    private int centerX = -1;
    private int centerY = -1;

    private TextView mLocationTextView;

    protected GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    ImageView imageViewback;
    Button btSetLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // InitializeUI
        initializeUI();
        buildGoogleApiClient();
    }

    private void initializeUI() {
        try {
            // Loading map
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }
        mLocationTextView = (TextView) findViewById(R.id.location_text_view);
        mMarkerParentView = findViewById(R.id.marker_view_incl);
        mMarkerImageView = (ImageView) findViewById(R.id.marker_icon_view);
        imageViewback=findViewById(R.id.imageback);
        btSetLocation=findViewById(R.id.bt_set_location);


        imageViewback.setOnClickListener(this);
        btSetLocation.setOnClickListener(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        imageParentWidth = mMarkerParentView.getWidth();
        imageParentHeight = mMarkerParentView.getHeight();
        imageHeight = mMarkerImageView.getHeight();

        centerX = imageParentWidth / 2;
        centerY = (imageParentHeight / 2)+(imageHeight / 2);
    }

    private void initilizeMap() {
        if (mGoogleMap == null) {
            mCustomMapFragment = ((CustomMapFragment) getFragmentManager()
                    .findFragmentById(R.id.map));
            mCustomMapFragment.setOnDragListener(PickLocation.this);
            mCustomMapFragment.getMapAsync(this);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
String locName="",locLati="",locLongi="";
    @Override
    public void onDrag(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            Projection projection = (mGoogleMap != null && mGoogleMap
                    .getProjection() != null) ? mGoogleMap.getProjection()
                    : null;
            //
            if (projection != null) {
                LatLng centerLatLng = projection.fromScreenLocation(new Point(
                        centerX, centerY));
                updateLocation(centerLatLng);
            }
        }
    }

    private void updateLocation(LatLng centerLatLng) {
        if (centerLatLng != null) {
            Geocoder geocoder = new Geocoder(PickLocation.this,
                    Locale.getDefault());

            List<Address> addresses = new ArrayList<Address>();
            try {
                addresses = geocoder.getFromLocation(centerLatLng.latitude,
                        centerLatLng.longitude, 1);
                locLati=String.valueOf(centerLatLng.latitude);
                locLongi=String.valueOf(centerLatLng.longitude);
//                Toast.makeText(this, "latit= "+centerLatLng.latitude+" logi= "+centerLatLng.longitude, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (addresses != null && addresses.size() > 0) {

                String addressIndex0 = (addresses.get(0).getAddressLine(0) != ""/*null*/) ? addresses
                        .get(0).getAddressLine(0) : ""/*null*/;
                String addressIndex1 = (addresses.get(0).getAddressLine(1) != ""/*null*/) ? addresses
                        .get(0).getAddressLine(1) : ""/*null*/;
                String addressIndex2 = (addresses.get(0).getAddressLine(2) !="" /*null*/) ? addresses
                        .get(0).getAddressLine(2) : ""/*null*/;
                String addressIndex3 = (addresses.get(0).getAddressLine(3) != ""/*null*/) ? addresses
                        .get(0).getAddressLine(3) : ""/*null*/;

                String completeAddress = addressIndex0 ;
                locName = addressIndex0 ;

//                String completeAddress = addressIndex0 + "," + addressIndex1;

               /* if (addressIndex2 != null) {
                    completeAddress += "," + addressIndex2;
                }*/
//                if (addressIndex3 != null) {
//                    completeAddress += "," + addressIndex3;
//                }
                if (completeAddress != null) {
                    mLocationTextView.setText(completeAddress);
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
    }
    double lati = 0;//22.71246
    double longi = 0;//75.86491

   //for show current location
   private void buildGoogleApiClient() {
       mGoogleApiClient = new GoogleApiClient.Builder(this)
               .addConnectionCallbacks(this)
               .addOnConnectionFailedListener(this)
               .addApi(LocationServices.API)
               .build();

   }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }
    public void myLocation()
    {
        LatLng latLng = new LatLng(lati, longi);
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        updateLocation(latLng);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            lati = mLastLocation.getLatitude();
            longi = mLastLocation.getLongitude();
        }
        myLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    ///////////////////////////

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imageback:
                onBackPressed();
                break;
           case R.id.bt_set_location:
               Intent returnIntent = new Intent();
               returnIntent.putExtra("loc_name",locName);
               returnIntent.putExtra("loc_lati",locLati);
               returnIntent.putExtra("loc_longi",locLongi);
               setResult(RESULT_OK,returnIntent);
               finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
        Animatoo.animateFade(this);
    }
}
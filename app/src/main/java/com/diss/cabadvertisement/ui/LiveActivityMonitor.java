package com.diss.cabadvertisement.ui;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import com.diss.cabadvertisement.R;
import com.diss.cabadvertisement.ui.model.LocDat;
import com.diss.cabadvertisement.ui.util.AppData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.johnnylambada.location.LocationObserver;
import com.johnnylambada.location.LocationProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.diss.cabadvertisement.ui.MapUtils.getBearing;

public class LiveActivityMonitor extends AppCompatActivity implements View.OnClickListener, LocationObserver, Runnable, LocationListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    ImageView imageViewback;
    AppData appdata;
    double lati = 0;//22.71246
    double longi = 0;//75.86491
    SupportMapFragment mapFragment;
    protected GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    private GoogleMap mMapSession;
    int miliSecond=30000;//10000
    int timerCountdown=1000;//1000

   private DatabaseReference driverAppDatabase;
LinearLayout lyVehicleInfo;
      int  diffSingleTrack=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        appdata=new AppData(this);
        driverAppDatabase = FirebaseDatabase.getInstance().getReference();
        InitCompo();
        Listener();
    }
    @Override
    protected void onResume() {
        super.onResume();
        checkSingle=false;
        diffSingleTrack=0;
        GetLocationDriverData();
        InitTimer();
//        setCurLoc();
        SetOnlineText();

    }
    public void SetOnlineText()
    {

        StartTimer();

//        tvLiveTracking.setText("Tracking Running");//
//        tvOnOffIndicator.setBackgroundResource(R.drawable.circle_green);


//        String logStatus=appdata.getIsOnline();
//        if(logStatus.equals("0")|| TextUtils.isEmpty(logStatus))
//        {
//            tvLiveTracking.setText("Live Tracking");//for go online
//            tvOnOffIndicator.setBackgroundResource(R.drawable.circle_red);
//            cancelTimer();
//        }
//        else
//        {
//            StartTimer();
//            tvLiveTracking.setText("Tracking Running");//for go offline
//            tvOnOffIndicator.setBackgroundResource(R.drawable.circle_green);
//        }
    }

    private void Listener() {
        imageViewback.setOnClickListener(this);
        tvLiveTracking.setOnClickListener(this);
//        tvSingleDriver.setOnClickListener(this);
        lyVehicleInfo.setOnClickListener(this);

    }
    TextView tvLiveTracking,tvOnOffIndicator/*,tvSingleDriver*/,tvVehicleInfo;
    public void InitCompo()
    {

        lyVehicleInfo=findViewById(R.id.ly_vehicle_info);
        tvVehicleInfo=findViewById(R.id.tv_vehicle_info);
        imageViewback=findViewById(R.id.imageback);
        tvLiveTracking=findViewById(R.id.tv_go_online);
        tvOnOffIndicator=findViewById(R.id.tv_online);
//        tvSingleDriver=findViewById(R.id.tv_singel_driver);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        buildGoogleApiClient();

    }
    boolean checkSingle=false;
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
           case R.id.imageback:
                finish();
                Animatoo.animateSlideRight(this);
                break;
//                case R.id.tv_singel_driver:
//                    lyVehicleInfo.setVisibility(View.VISIBLE);
//                break;
              case R.id.ly_vehicle_info:
                  if(arrayDriver!=null&&arrayDriver.length>0)
                  {
                      ShowAlertCarModelDlg();
                  }
                  else {
                      Toast.makeText(this, "driver not found", Toast.LENGTH_SHORT).show();
                  }
                break;
            case R.id.tv_go_online://go live
               if(!checkSingle) {
                   lyVehicleInfo.setVisibility(View.VISIBLE);
                   checkSingle=true;
               }
               else
               {
                   lyVehicleInfo.setVisibility(View.GONE);
               }

//                String logStatus=appdata.getIsOnline();
//
//                if(logStatus.equals("0")|| TextUtils.isEmpty(logStatus))
//                {
//                    appdata.setIsOnline("1");
//                }
//                else
//                {
//                    appdata.setIsOnline("0");
//                }
//                SetOnlineText();
                break;

        }
    }

    //for location

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMapSession = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMapSession.setMyLocationEnabled(true);
//        myLocation();

    }
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
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
//        mCurrLocationMarker =mMapSession.addMarker(markerOptions);
        //move map camera
        mMapSession.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }

    //for location

    //for go live
//    public void setCurLoc()
//    {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkLocationPermission()) {
//                LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//                if (!manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                    startActivityForResult(intent, 101);
//                } else {
//                    initLocation();
//                }
//            } else {
//                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
//            }
//        } else {
//            LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//            if (!manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivityForResult(intent, 101);
//            } else {
//                initLocation();
//            }
//        }
//    }
//    private LocationProvider locationProvider;
//
//    private void initLocation() {
//        locationProvider = new LocationProvider.Builder(this)
//                .locationObserver(this)
//                .intervalMs(miliSecond)//300000
//                .onPermissionDeniedFirstTime(this)
//                .onPermissionDeniedAgain(this)
//                .onPermissionDeniedForever(this)
//                .build();
//        locationProvider.startTrackingLocation();
//    }
//    private boolean checkLocationPermission() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
//        {
//            return false;
//        } else {
//            return true;
//        }
//    }

//for  LocationProvider.Builder
    @Override
    public void onLocation(Location location) {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String address = addresses.get(0).getSubLocality();
            String city = addresses.get(0).getLocality();

            Log.e("","run onLocation lati= "+location.getLatitude()+" longi= "+location.getLongitude());

            lati = location.getLatitude();
            longi = location.getLongitude();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Log.e("","run");

    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        locationProvider.onRequestPermissionsResult(requestCode,permissions,grantResults);
//    }
    //////////////////////////


    ArrayList<LocDat>arStartLatlong=new ArrayList<>();
    ArrayList<Marker>arAllMarker=new ArrayList<>();
    boolean isFirstPosition=true;
    int count=0;
    String[] arrayDriver=null;
    public void GetLocationDriverData()
    {

        try {
            driverAppDatabase.child("S_ID-3").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    count=0;
                    LocDat bean;

                    for (DataSnapshot recSnap : dataSnapshot.getChildren()) {

                        double lat = (double) recSnap.child("lat").getValue();
                        double lng = (double) recSnap.child("lng").getValue();
                        String driverid = recSnap.child("userId").getValue().toString();
                        Log.e("", "shyam tracking lat= " + lat + " lng= " + lng);
                        LatLng startPosition1 = new LatLng(lat, lng);

                        if (isFirstPosition) {
                            bean = new LocDat();
                            bean.setDriverId(driverid);
                            bean.setLat(lat);
                            bean.setLng(lng);
                            arStartLatlong.add(bean);

                           Marker carMarker = mMapSession.addMarker(new MarkerOptions().position(startPosition1).
                                flat(true)/*.icon(BitmapDescriptorFactory.fromResource(R.mipmap.new_car_small))*/
//                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                .icon(BitmapDescriptorFactory.fromBitmap(ResizeMarker()))
                                .title(driverid)
                           );
                        carMarker.setAnchor(0.5f, 0.5f);
                            arAllMarker.add(carMarker);



                        } else {
                                if(arStartLatlong.get(count).getDriverId().equals(driverid))//for move a single car
                                {
                                LatLng  endPosition = new LatLng(lat, lng);
                                LatLng startPosition = new LatLng(arStartLatlong.get(count).getLat(),arStartLatlong.get(count).getLng());

                                if ((startPosition.latitude != endPosition.latitude) || (startPosition1.longitude != endPosition.longitude)) {
                                    Log.e(TAG, "NOT SAME");
                                    startBikeAnimation(startPosition, endPosition,count,driverid);
                                }
                                else
                                {
                                    Log.e(TAG, "SAME");
                                }

                                }
                        }

                    }

                    if(isFirstPosition)
                    {
                        isFirstPosition=false;
                        if(arStartLatlong.size()>0) {
                            arrayDriver = new String[arStartLatlong.size()];

                            for (int i = 0; i < arStartLatlong.size(); i++) {
                                arrayDriver[i] = arStartLatlong.get(i).getDriverId();
                            }
                        }
                    }
Log.e("","arStartLatlong.size()= "+arStartLatlong.size());


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        StartTimer();
        ///////////////

    }

    Marker SinglecarMarker;
    ArrayList<LocDat>arSingleStartLatlong=new ArrayList<>();
    boolean isSingleFirstPosition=true;
    public void GetSingleDriverData()
    {

        try {
            driverAppDatabase.child("S_ID-3").child(trackDriverId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

//                    arStartMarker.clear();
                    count=0;
                    LocDat bean;
                    LatLng  LaststartPosition=null;
//                    for (DataSnapshot recSnap : dataSnapshot.getChildren())
                    {

                        double lat = (double) dataSnapshot.child("lat").getValue();
                        double lng = (double) dataSnapshot.child("lng").getValue();
                        String driverid = dataSnapshot.child("userId").getValue().toString();
                        Log.e("", "shyam tracking lat= " + lat + " lng= " + lng);
                        LatLng startPosition1 = new LatLng(lat, lng);

//                        if (driverid.equals(trackDriverId))
//                        {
                            if (isSingleFirstPosition) {
                                bean = new LocDat();
                                bean.setDriverId(driverid);
                                bean.setLat(lat);
                                bean.setLng(lng);
                                arSingleStartLatlong.add(bean);

                                SinglecarMarker = mMapSession.addMarker(new MarkerOptions().position(startPosition1).
                                                flat(true)/*.icon(BitmapDescriptorFactory.fromResource(R.mipmap.new_car_small))*/
//                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                                .icon(BitmapDescriptorFactory.fromBitmap(ResizeMarker()))
                                                .title(driverid)
                                );
                                SinglecarMarker.setAnchor(0.5f, 0.5f);
//                                arAllMarker.add(carMarker);


                            } else {

//                                if (arStartLatlong.get(count).getDriverId().equals(driverid))//for move a single car
                                {
                                    LatLng endPosition = new LatLng(lat, lng);
                                    LatLng startPosition = new LatLng(arSingleStartLatlong.get(count).getLat(), arSingleStartLatlong.get(count).getLng());

                                    if ((startPosition.latitude != endPosition.latitude) || (startPosition1.longitude != endPosition.longitude)) {
                                        Log.e(TAG, "NOT SAME");
                                        SingleBikeAnimation(startPosition, endPosition, count, driverid);
                                    } else {
                                        Log.e(TAG, "SAME");
                                    }

                                }
                            }

//                        }

                    }

                    if(isSingleFirstPosition)
                    {
                        isSingleFirstPosition=false;
                    }

                    Log.e("","arSingleStartLatlong.size()= "+arSingleStartLatlong.size());


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        StartTimer();
        ///////////////

    }

    public Bitmap ResizeMarker()
    {
//        int width = 130;
//        int height = 60;
//        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.map_car);

        int width = 60;
        int height = 110;
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.new_car_small1);


        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        return smallMarker;
    }
    CountDownTimer cTimer = null;
    //start timer function
    void InitTimer() {
        cTimer = new CountDownTimer(miliSecond, timerCountdown) {
            public void onTick(long millisUntilFinished) {
//                Log.e("","remaining time= "+(millisUntilFinished / 1000));
                long sec = (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                Log.e("", "remaining time=: "+sec );

//                CheckOldAndNewData();
            }
            public void onFinish() {
                if(diffSingleTrack==0)
                {
                    GetLocationDriverData();
                }
                else
                {
                    GetSingleDriverData();
                }

            }
        };
    }

    public void StartTimer()
{
    if(cTimer!=null)
    cTimer.start();
}

    //cancel timer
    void cancelTimer() {
        if(cTimer!=null)
            cTimer.cancel();
    }
    private static final String TAG ="MyActivity" ;
    private static final long ANIMATION_TIME_PER_ROUTE = 3000;
    float   v;
    private void startBikeAnimation(final LatLng start, final LatLng end, final int pos, final String driverid) {

        Log.i(TAG, "startBikeAnimation called...");

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(ANIMATION_TIME_PER_ROUTE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                //LogMe.i(TAG, "Car Animation Started...");
                v = valueAnimator.getAnimatedFraction();
                double lng = v * end.longitude + (1 - v)
                        * start.longitude;
                double lat = v * end.latitude + (1 - v)
                        * start.latitude;

                LatLng newPos = new LatLng(lat, lng);

//                Marker  carMarker = mMapSession.addMarker(new MarkerOptions().position(newPos).
//                                flat(true)/*.icon(BitmapDescriptorFactory.fromResource(R.mipmap.new_car_small))*/
////                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
//                                .icon(BitmapDescriptorFactory.fromBitmap(ResizeMarker()))
//                                .title("")
//                );


                arAllMarker.get(pos).setPosition(newPos);
                arAllMarker.get(pos).setAnchor(0.5f, 0.5f);
                arAllMarker.get(pos).setRotation(getBearing(start, end));

                LocDat bean=new LocDat();
                bean.setDriverId(driverid);
                bean.setLat(lat);
                bean.setLng(lng);
                arStartLatlong.set(pos,bean);
                count++;
            }

        });
        valueAnimator.start();
    }


    private void SingleBikeAnimation(final LatLng start, final LatLng end, final int pos, final String driverid) {

        Log.i(TAG, "startBikeAnimation called...");

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(ANIMATION_TIME_PER_ROUTE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                //LogMe.i(TAG, "Car Animation Started...");
                v = valueAnimator.getAnimatedFraction();
                double lng = v * end.longitude + (1 - v)
                        * start.longitude;
                double lat = v * end.latitude + (1 - v)
                        * start.latitude;

                LatLng newPos = new LatLng(lat, lng);

//                Marker  carMarker = mMapSession.addMarker(new MarkerOptions().position(newPos).
//                                flat(true)/*.icon(BitmapDescriptorFactory.fromResource(R.mipmap.new_car_small))*/
////                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
//                                .icon(BitmapDescriptorFactory.fromBitmap(ResizeMarker()))
//                                .title("")
//                );

                SinglecarMarker.setPosition(newPos);
                SinglecarMarker.setAnchor(0.5f, 0.5f);
                SinglecarMarker.setRotation(getBearing(start, end));

                LocDat bean=new LocDat();
                bean.setDriverId(driverid);
                bean.setLat(lat);
                bean.setLng(lng);
                arSingleStartLatlong.set(pos,bean);
                count++;
            }

        });
        valueAnimator.start();
    }

    int locPosModel=0;
    String trackDriverId="";
    public  void ShowAlertCarModelDlg()
    {
        android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(this);
        mBuilder.setTitle("Select Driver Id");
        mBuilder.setSingleChoiceItems(arrayDriver, locPosModel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tvVehicleInfo.setText(arrayDriver[i]);
                locPosModel=i;
                trackDriverId=arrayDriver[i];
                dialogInterface.dismiss();
                mMapSession.clear();
                diffSingleTrack=1;
                isSingleFirstPosition=true;
                arSingleStartLatlong.clear();
                GetSingleDriverData();
                cancelTimer();
                StartTimer();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }
//
//    private void startBikeAnimation1(final LatLng start, final LatLng end) {
//
//        Log.i(TAG, "startBikeAnimation called...");
//
//        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
//        valueAnimator.setDuration(ANIMATION_TIME_PER_ROUTE);
//        valueAnimator.setInterpolator(new LinearInterpolator());
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//
//                //LogMe.i(TAG, "Car Animation Started...");
//                v = valueAnimator.getAnimatedFraction();
//                double lng = v * end.longitude + (1 - v)
//                        * start.longitude;
//                double lat = v * end.latitude + (1 - v)
//                        * start.latitude;
//
//                LatLng newPos = new LatLng(lat, lng);
//
////                Marker  carMarker = mMapSession.addMarker(new MarkerOptions().position(newPos).
////                                flat(true)/*.icon(BitmapDescriptorFactory.fromResource(R.mipmap.new_car_small))*/
//////                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
////                                .icon(BitmapDescriptorFactory.fromBitmap(ResizeMarker()))
////                                .title("")
////                );
//
//                carMarker.setPosition(newPos);
//                carMarker.setAnchor(0.5f, 0.5f);
//                carMarker.setRotation(getBearing(start, end));
//
//
////                arEndLatlong.get(pos).latitude,arEndLatlong.get(i).longitude
//
////                arEndLatlong.set(pos,newPos);
//
////                LocDat bean=new LocDat();
////                bean.setDriverId(driverid);
////                bean.setLat(lat);
////                bean.setLng(lng);
////                arEndLatlong.add(bean);
////                arEndLatlong.set(pos,bean);
//
////                if(arEndLatlong.size()<count) {
////                    count++;
////                }
////                else
////                {
////                    count=0;
////                }
//
//                // todo : Shihab > i can delay here   Marker carMarker = mMapSession
////                mMapSession.moveCamera(CameraUpdateFactory
////                        .newCameraPosition
////                                (new CameraPosition.Builder()
////                                        .target(newPos)
////                                        .zoom(15.5f)
////                                        .build()));
////                LatLng startPosition = carMarker.getPosition();
////                arStartMarker.add(carMarker);
//
//            }
//
//        });
//        valueAnimator.start();
//    }

}

package com.diss.cabadvertisement.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.diss.cabadvertisement.R;
import com.diss.cabadvertisement.ui.presenter.SubscriptionPlanPresenter;
import com.diss.cabadvertisement.ui.util.AppData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardActivity extends FragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener ,OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    AppData appdata;
    DrawerLayout drawer;
    double lati = 0;//22.71246
    double longi = 0;//75.86491
    SupportMapFragment mapFragment;
    protected GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    private GoogleMap mMapSession;
    Marker mCurrLocationMarker;
    CircleImageView ivUsrPic;
    TextView tvUsrNm,tvUsrPhoneNo;
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        appdata=new AppData(DashboardActivity.this);
         drawer = findViewById(R.id.drawer_layout);
//        mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        NavigationView navi = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navi.getHeaderView(0);
        tvUsrNm = hView.findViewById(R.id.navname);
        tvUsrPhoneNo = hView.findViewById(R.id.navnumber);
        ivUsrPic=hView.findViewById(R.id.iv_user_Pic);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
         mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        buildGoogleApiClient();
        GetRegistration();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setProrileData();
    }
    public void setProrileData()
    {
        if(!appdata.getProfilePic().equals("NA")) {
            Glide.with(DashboardActivity.this)
                    .load(appdata.getProfilePic())
                    .placeholder(R.drawable.ic_user)
                    .into(ivUsrPic);
        }
        tvUsrNm.setText(appdata.getUsername());
        tvUsrPhoneNo.setText(appdata.getMobile());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_profile) {
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

        } else if (id == R.id.nav_notification) {

            startActivity(new Intent(getApplicationContext(), NotificationActivity.class));


        } else if (id == R.id.nav_campaigns) {
            startActivity(new Intent(getApplicationContext(), MyCampaignsActivity.class));


        } else if (id == R.id.nav_weekly) {

            startActivity(new Intent(getApplicationContext(), ActWeeklyAnalysis.class));
//            startActivity(new Intent(getApplicationContext(), WeeklyAnalysisActivity.class));


        } else if (id == R.id.nav_liveactivity) {

            startActivity(new Intent(getApplicationContext(), LiveActivityMonitor.class));

        } else if (id == R.id.nav_help) {

            startActivity(new Intent(getApplicationContext(), HelpActivity.class));

        } else if (id == R.id.nav_share) {

            startActivity(new Intent(getApplicationContext(), ShareActivity.class));

        } else if (id == R.id.nav_rate) {

            startActivity(new Intent(getApplicationContext(), RateActivity.class));

        } else if (id == R.id.nav_renew) {

//            startActivity(new Intent(getApplicationContext(), SubscriptionPlanActivity.class));
            startActivity(new Intent(getApplicationContext(), REviewActivity.class));

        } else if (id == R.id.nav_logout) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            appdata.setUserID("NA");
                            appdata.setUserStatus("NA");
                            appdata.clear();
                            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                            finish();
//                                finishAffinity();
                            Animatoo.animateSlideDown(DashboardActivity.this);
                        }
                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

//        if(lati!=0.0||logiti!=0.0)
//        {
//            add= supportUtils.getCompleteAddressString(ActBookSession.this,lati,logiti);
//            Log.e("","0000add= "+add);
////            mLocationAddress.setText(add);
////            ShowLine();
//        }
//        Toast.makeText(this, "currnet loat= "+lati+" logiti= "+logiti, Toast.LENGTH_SHORT).show();

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
        mCurrLocationMarker = mMapSession.addMarker(markerOptions);

        //move map camera
        mMapSession.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
//        mMapSession.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,16f));
//        mMapSession.getUiSettings().setZoomControlsEnabled(true);
    }
    //for location

    public void GetRegistration()
    {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

// Get the Instance ID token//
                        String token = task.getResult().getToken();
//                        String msg = getString(R.string.fcm_token, token);
                        Log.d("", "shyam fcm token= "+token);

                    }
                });

    }
}
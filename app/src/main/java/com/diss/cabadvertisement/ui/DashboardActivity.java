package com.diss.cabadvertisement.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.diss.cabadvertisement.R;
import com.diss.cabadvertisement.ui.util.AppData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DashboardActivity extends FragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener ,OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    AppData appdata;
    DrawerLayout drawer;
    public GoogleMap Map;
    LocationManager locationManager;
    String latitude ;
    String longitude ;
    MapView mapView;
    double lati = 22.72000155214488;;
    double longi = 75.87855927462988;
    SupportMapFragment mapFragment;
    protected GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
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
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        buildGoogleApiClient();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
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

            startActivity(new Intent(getApplicationContext(), WeeklyAnalysisActivity.class));


        } else if (id == R.id.nav_liveactivity) {


        } else if (id == R.id.nav_help) {

            startActivity(new Intent(getApplicationContext(), HelpActivity.class));

        } else if (id == R.id.nav_share) {

            startActivity(new Intent(getApplicationContext(), ShareActivity.class));

        } else if (id == R.id.nav_rate) {

            startActivity(new Intent(getApplicationContext(), REviewActivity.class));

        } else if (id == R.id.nav_renew) {

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

    public void getMapVeew ()
    {

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                Map=mMap;

//                StringRequest request = new StringRequest(URL1, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObj = new JSONObject(response);
//                            JSONArray contacts = jsonObj.getJSONArray("REAL_ESTATE_APP");
//                            for (int i = 0; i < contacts.length(); i++){
//                                JSONObject c = contacts.getJSONObject(i);
//                                String type_id = c.getString("type_id");
//                                String property_map_latitude = c.getString("property_map_latitude");
//                                String property_map_longitude = c.getString("property_map_longitude");
//                                double lat = Double.parseDouble(property_map_latitude);
//                                double lng = Double.parseDouble(property_map_longitude);
//                                LatLng latLng = new LatLng(lat, lng);
//                                String property_name = c.getString("property_name");
//                                String property_address = c.getString("property_address");
//                                String imageurl=c.getString("flag");
//                                Bitmap bitmap=getMarkerBitmapFromView(imageurl);
//                                Map.addMarker(new MarkerOptions()
//                                        .position(latLng)
//                                        .title(property_name)
//                                        .snippet(property_address)
//                                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
//                                CameraPosition Liberty= CameraPosition.builder().target(latLng).zoom(9).bearing(0).tilt(45).build();
//                                Map.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                RequestQueue queue = Volley.newRequestQueue(mview.getContext());
//                queue.add(request);

                LatLng latLng = new LatLng(lati, longi);
                Map.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title("Current location")
                                        .snippet(""));
                                        /*.icon(BitmapDescriptorFactory.fromBitmap(R.drawable.ic_address)));//R.drawable*/
                                CameraPosition Liberty= CameraPosition.builder().target(latLng).zoom(9).bearing(0).tilt(45).build();
                                Map.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));
                Map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//                CameraPosition Liberty= CameraPosition.builder().target(new LatLng(lati,longi)).zoom(16).bearing(0).tilt(45).build();
//                Map.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));
                Map.getUiSettings().setZoomControlsEnabled(true);
            }
        });
    }
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

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
    private void buildGoogleApiClient() {
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();

    }
    //for location
}
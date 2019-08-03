package com.diss.cabadvertisement.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.diss.cabadvertisement.R;
import com.diss.cabadvertisement.ui.util.AppData;

public class SplashActivity extends AppCompatActivity {
    AppData appData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        appData = new AppData(this);
        checkPermissions();
    }


    private static final int REQUEST_CODE_PERMISSION = 2;
    private void checkPermissions() {
        if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION);

        } else if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);

        } else if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);

        } else if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_PERMISSION);
        }
        else if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSION);
        }

        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


//                            Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
//                            startActivity(intent);
//                            finish();
//                            Animatoo.animateSlideUp(SplashActivity.this);


                    String userID = appData.getUserID();
                    String userStatus = appData.getUserStatus();//0 = not verify otp &  1 = not approve by admin &  2 = approved by admin
            if (userID.equals("NA")||userID=="NA"|| TextUtils.isEmpty(userID)||userStatus.equals("NA")||userStatus=="NA"|| TextUtils.isEmpty(userStatus)||userStatus.equals("0")||userStatus=="0") {
                        Intent intent = new Intent(SplashActivity.this, GetStartActivity.class);
                        startActivity(intent);
                        finish();
                        Animatoo.animateFade(SplashActivity.this);
                    }
                    else {
                Intent intent=null;
                        switch (userStatus)
                        {

                            case"1"://1 = otp verified
                            case "2":// approved by admin but not subsription done
                                intent = new Intent(SplashActivity.this, LoginActivity.class);
                                break;
//                            case "2":// approved by admin but not subsription done
//                                intent = new Intent(SplashActivity.this, SubscriptionPlanActivity.class);
//                                break;
                            case "3":// not subscription done by user
                                intent = new Intent(SplashActivity.this, SubscriptionPlanActivity.class);
                                break;
                            case "4"://subscription done by user
                                intent = new Intent(SplashActivity.this, DashboardActivity.class);
                                break;
                        }
                           startActivity(intent);
                            finish();
                            Animatoo.animateSlideUp(SplashActivity.this);

                    }


                }
            }, 3000);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        checkPermissions();
    }

}


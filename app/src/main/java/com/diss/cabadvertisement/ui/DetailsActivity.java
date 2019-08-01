package com.diss.cabadvertisement.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.diss.cabadvertisement.R;

public class DetailsActivity extends AppCompatActivity {
    Button loginbutton;
    TextView addclick ,minusclick;
    int count=0;
    int countBACK=0;
    ImageView firstcamera;
    private final int PICK_IMAGE_REQUEST = 71;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private Uri avatarPath;
    String camera_staus="none";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advanced_details_activity);


        firstcamera=(ImageView)findViewById(R.id.firstcamera_id);
        loginbutton=(Button)findViewById(R.id.login_button_id);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DetailsActivity.this, OrderActivity.class);
                startActivity(intent);
            }
        });



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)

            ActivityCompat.requestPermissions(DetailsActivity.this, new String[]{Manifest.permission.CAMERA}, PICK_IMAGE_REQUEST);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED)

            ActivityCompat.requestPermissions(DetailsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_IMAGE_REQUEST);



        addclick=(TextView)findViewById(R.id.addclickbutton_id);
        minusclick=(TextView)findViewById(R.id.minusclickbutton_id);
        addclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countBACK=1;
                if (count==0){
                    addclick.setVisibility(View.VISIBLE);
                    minusclick.setVisibility(View.VISIBLE);
                    count=1;
                }else{
                    addclick.setVisibility(View.VISIBLE);
                    minusclick.setVisibility(View.GONE);
                    count=0;
                }
            }
        });

        firstcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera_staus="first";
                chooseImage();
            }
        });


    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            avatarPath = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), avatarPath);

                if(camera_staus.equals("first")){
                    firstcamera.setImageBitmap(bitmap);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

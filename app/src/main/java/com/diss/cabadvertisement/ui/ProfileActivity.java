package com.diss.cabadvertisement.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.diss.cabadvertisement.R;
import com.diss.cabadvertisement.ui.model.ImageBean;
import com.diss.cabadvertisement.ui.model.SignupBean;
import com.diss.cabadvertisement.ui.model.UpdateProfileBean;
import com.diss.cabadvertisement.ui.presenter.SignupPresenter;
import com.diss.cabadvertisement.ui.presenter.UpdateProfilePresenter;
import com.diss.cabadvertisement.ui.util.AppData;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, UpdateProfilePresenter.updateProfile{

    ImageView imageViewback;
    AppData appdata;
    LinearLayout lyPhoto,lyBottom;
    CircleImageView ivPic;
    boolean editFlag=false;
    EditText etCompanyNm,etOfficAdd,etOffiContact,etAreaBusiness,etPersonNm,etPersonContact;
    TextView tvEditProfile;
    Button btUpdateProfile;
    private UpdateProfilePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile_activity);
        appdata=new AppData(ProfileActivity.this);
        presenter = new UpdateProfilePresenter(ProfileActivity.this, ProfileActivity.this);
        InitCompo();
        Listener();
        SetPreDataAtView();
    }
    private void Listener() {

        imageViewback.setOnClickListener(this);
        lyPhoto.setOnClickListener(this);
        tvEditProfile.setOnClickListener(this);
        btUpdateProfile.setOnClickListener(this);
    }
//    TextView tvCompanyNm,tvOfficAdd,tvOffiContact,tvAreaBusiness,tvPersonNm,tvPersonContact;

    public void InitCompo()
    {
        etCompanyNm=findViewById(R.id.et_company_nm);
        etOfficAdd=findViewById(R.id.et_office_add);
        etOffiContact=findViewById(R.id.et_office_contact);
        etAreaBusiness=findViewById(R.id.et_area_of_business);
        etPersonNm=findViewById(R.id.et_person_nm);
        etPersonContact=findViewById(R.id.et_person_phone);

        imageViewback=findViewById(R.id.imageback);
        lyPhoto=findViewById(R.id.ly_photo);
        lyBottom=findViewById(R.id.ly_bottom);
        tvEditProfile=findViewById(R.id.tv_edit_profile);

        ivPic=findViewById(R.id.profileimages_id);
        btUpdateProfile=findViewById(R.id.bt_update_profile);
    }
public void SetPreDataAtView()
{
    etCompanyNm.setText(appdata.getCompanyNm());
    etOfficAdd.setText(appdata.getCompanyOfficeAddress());
    etOffiContact.setText(appdata.getCompanyContactNo());
    etAreaBusiness.setText(appdata.getCompanyAreaOfBusiness());
    etPersonNm.setText(appdata.getUsername());
    etPersonContact.setText(appdata.getMobile());
if(!appdata.getProfilePic().equals("NA")) {
    Glide.with(ProfileActivity.this)
            .load(appdata.getProfilePic())
            .placeholder(R.drawable.ic_user)
            .into(ivPic);
}
    SetEditTag(editFlag);
}
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.bt_update_profile:
                addValidationToViews();
                break;
            case R.id.imageback:
                onBackPressed();
                break;

            case R.id.ly_photo:
                if(editFlag) {
                    selectImage1();
                }
                else
                {
                    uploadBase64="";//for empty data
                }
                break;
           case R.id.tv_edit_profile:
               if(!editFlag) {
                   editFlag=true;
               }
               else
               {
                   editFlag=false;
               }

               SetEditTag(editFlag);
                break;

        }
    }
    public void SetEditTag(boolean checkFlag)
    {
        if(checkFlag)
        {
            etCompanyNm.setEnabled(true);
            etOfficAdd.setEnabled(true);
            etOffiContact.setEnabled(true);
            etAreaBusiness.setEnabled(true);
            etPersonNm.setEnabled(true);
            etPersonContact.setEnabled(false);
            etCompanyNm.setFocusable(true);
            etCompanyNm.setSelection(etCompanyNm.getText().toString().length());
            lyBottom.setVisibility(View.VISIBLE);
        }
        else
        {
            etCompanyNm.setEnabled(false);
            etOfficAdd.setEnabled(false);
            etOffiContact.setEnabled(false);
            etAreaBusiness.setEnabled(false);
            etPersonNm.setEnabled(false);
            etPersonContact.setEnabled(false);
            lyBottom.setVisibility(View.GONE);
        }

    }

    static  int CAMERA_REQUEST=3,Result_Load_Image=4;
    int MY_CAMERA_PERMISSION_CODE=11;
    private void selectImage1() {//,"Choose pdf"
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Select Profile");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_CAMERA_PERMISSION_CODE);
                    } else {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent();
                    intent.setType("image/*");//only for image
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select File"), Result_Load_Image);

                }
                else if (items[item].equals("Choose pdf")) {
                  /*  new MaterialFilePicker()
                            .withActivity(DetailsActivity.this)
                            .withRequestCode(1)
                            .withHiddenFiles(true)
                            .withTitle("Sample title")
                            .start();*/

                }

                else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    String uploadBase64="";
    String filePath="",fileNm="";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Result_Load_Image && data != null && data.getData() != null) {
                Uri filePath2 = data.getData();

                try {
                    Bitmap bitmap= MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), filePath2);
                    Bitmap bit=Bitmap.createScaledBitmap(bitmap,150,150,false);

                    Uri selectedImage = getImageUri(getApplicationContext(), bit);

                    filePath = getPath(selectedImage);
                    File f = new File(filePath);
                    fileNm = f.getName();
//                    tvFileNm.setText(fileNm);
                    uploadBase64=getFileToBase64_1(f);
//                    ivPic.setImageBitmap(bit);
                    Glide.with(this)
                            .load(new File(filePath))
//                            .placeholder(R.mipmap.ic_launcher)
                            .into(ivPic);
                    Log.e("","shyam photo size11= "+f.length()+" uploadBase64= "+uploadBase64);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Bitmap bit=Bitmap.createScaledBitmap(photo ,150,150,false);
                Uri selectedImage = getImageUri(getApplicationContext(), bit);
                filePath = getPath(selectedImage);
                File f = new File(filePath);
                fileNm = f.getName();
//                tvFileNm.setText(fileNm);
//                uploadBase64=getFileToBase64(filePath);
                uploadBase64=getFileToBase64_1(f);
//                firstcamera.setImageResource(bit);
//                ivPic.setImageBitmap(bit);

                Glide.with(this)
                        .load(new File(filePath))
//                        .placeholder(R.mipmap.ic_launcher)
                        .into(ivPic);
                Log.e("","shyam file size11= "+f.length()+" uploadBase64= "+uploadBase64);


            }
            else if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

                File f  = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                filePath = f.getAbsolutePath();

                File f1 = new File(filePath);
                fileNm = f1.getName();
//                tvFileNm.setText(fileNm);
//                uploadBase64=getFileToBase64(filePath);

//                firstcamera.setImageBitmap(bit);
                Log.e("","shyam file size22= "+f.length()+" uploadBase64= "+uploadBase64);
            }
            else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
    public String getFileToBase64_1(File f) {
        InputStream inputStream = null;
        String encodedFile= "", lastVal;
        try {
            inputStream = new FileInputStream(f.getAbsolutePath());

            byte[] buffer = new byte[10240];//specify the size to allow
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }
            output64.close();
            encodedFile =  output.toString();
        }
        catch (FileNotFoundException e1 ) {
            e1.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        lastVal = encodedFile;
        return lastVal;
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
//    EditText etOffiContact,etAreaBusiness,etPersonNm,etPersonContact;
    String sCompanyNm,sOfficAdd,sOfficContact,sOfficArea,sPersonNm;
    public void addValidationToViews(){
        sCompanyNm=etCompanyNm.getText().toString().trim();
        sOfficAdd=etOfficAdd.getText().toString().trim();
        sOfficContact=etOffiContact.getText().toString().trim();
        sOfficArea=etAreaBusiness.getText().toString().trim();
        sPersonNm=etPersonNm.getText().toString().trim();
//        sMobi=etPersonContact.getText().toString().trim();

//        if(TextUtils.isEmpty(uploadBase64)){
//            Toast.makeText(this, "Please Select Profile Picture", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        else
            if(TextUtils.isEmpty(sCompanyNm)){
            etCompanyNm.setError("Enter Company Name");
            etCompanyNm.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(sOfficAdd)) {
            etOfficAdd.setError("Please Enter Office Address");
            etOfficAdd.requestFocus();
            return;
        }
        else if(TextUtils.isEmpty(sOfficContact)){
            etOffiContact.setError("Enter Office Contact");
            etOffiContact.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(sOfficArea)) {
            etAreaBusiness.setError("Please Enter Office Area");
            etAreaBusiness.requestFocus();
            return;
        }

        else if (TextUtils.isEmpty(sPersonNm)) {
            etPersonNm.setError("Please Enter Person Name");
            etPersonNm.requestFocus();
            return;
        }
//        else if (TextUtils.isEmpty(sMobi)) {
//            etPersonContact.setError("Please Enter your Mobile Number");
//            etPersonContact.requestFocus();
//            return;
//        }
//        else  if(sMobi.length()<10){//!appdata.isValidPhoneNumber(sMobi)
//            etPersonContact.setError("Enter a valid Mobile Number");
//            etPersonContact.requestFocus();
//            return;
//        }
        else {
            if(appdata.isNetworkConnected(this)){

                 if(TextUtils.isEmpty(uploadBase64))
                 {
                     fileNm="";
                 }
                UpdateProfileBean bean=new UpdateProfileBean();
                bean.setCompany_name(sCompanyNm);
                bean.setOffice_address(sOfficAdd);
                bean.setCompany_contact_no(sOfficContact);
                bean.setCompany_area_of_business(sOfficArea);
                bean.setFull_name(sPersonNm);
                bean.setProfile_pic(uploadBase64);
                presenter.updateProfileData(bean,fileNm);
            }else {
                appdata.ShowNewAlert(this,"Please connect to internet");
            }
        }
    }
//update profile
    @Override
    public void success(String response, String status) {
        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
        finish();
        Animatoo.animateSlideDown(ProfileActivity.this);
    }

    @Override
    public void error(String response) {
        appdata.ShowNewAlert(this,response);
    }

    @Override
    public void fail(String response) {
        appdata.ShowNewAlert(this,response);
    }
}

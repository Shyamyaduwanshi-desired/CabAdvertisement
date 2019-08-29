package com.diss.cabadvertisement.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.diss.cabadvertisement.R;
import com.diss.cabadvertisement.ui.adapter.CampaigDetailsAdapter;
import com.diss.cabadvertisement.ui.adapter.SelectedImageAdapter;
import com.diss.cabadvertisement.ui.adapter.SubPlanAdapter;
import com.diss.cabadvertisement.ui.model.AddCompaignDetailBean;
import com.diss.cabadvertisement.ui.model.AddCompaignLocBean;
import com.diss.cabadvertisement.ui.model.ImageBean;
import com.diss.cabadvertisement.ui.model.SubPlanBean;
import com.diss.cabadvertisement.ui.presenter.AdvancedDetailsPresenter;
import com.diss.cabadvertisement.ui.presenter.LoginPresenter;
import com.diss.cabadvertisement.ui.util.AppData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity implements CampaigDetailsAdapter.LocDetailClick,SelectedImageAdapter.ImageClick,View.OnClickListener, AdvancedDetailsPresenter.AdvanceCompaignDetail{
    Button loginbutton;

    ImageView firstcamera;
    TextView tvLoadImage;
    RecyclerView rvLocationNm;
    RecyclerView rvImage;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter mImgAdapter;
    int totalLength=0;
    private AdvancedDetailsPresenter presenter;
    AppData appdata;
  String PlanId="";
  String ObjectData="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advanced_details_activity);
        appdata=new AppData(DetailsActivity.this);
        presenter = new AdvancedDetailsPresenter(DetailsActivity.this, DetailsActivity.this);
        GetIntentData();
        InitCompo();
        Listener();
        AddItem();
    }

    private void GetIntentData() {
            ObjectData = getIntent().getStringExtra("object_data");
        Gson gson = new Gson();
        SubPlanBean subPlanBean = gson.fromJson(ObjectData, SubPlanBean.class);
        PlanId =subPlanBean.getPlanId();
        if(TextUtils.isEmpty(subPlanBean.getNumber_of_cabs()))
        {
            Toast.makeText(this, "Not correct number of cab data", Toast.LENGTH_SHORT).show();
        }
        else
        {
            totalLength = Integer.parseInt(subPlanBean.getNumber_of_cabs());
        }
        Log.e("","ObjectData= "+ObjectData.toString()+"PlanId= "+PlanId);
    }

    private void Listener() {
        loginbutton.setOnClickListener(this);
        firstcamera.setOnClickListener(this);
        tvLoadImage.setOnClickListener(this);
    }
EditText etCompaignNm/*,etAreaNm*/,etCompaignObj,etCompaignDetails;
    public void InitCompo()
    {
        tvLoadImage=findViewById(R.id.tv_load_image);
        etCompaignNm=findViewById(R.id.et_compaign);
//        etAreaNm=findViewById(R.id.et_loction_name);
        etCompaignObj=findViewById(R.id.et_object_compaign);
        etCompaignDetails=findViewById(R.id.et_detail_compaign);

        firstcamera=(ImageView)findViewById(R.id.firstcamera_id);
        loginbutton=(Button)findViewById(R.id.login_button_id);

        rvLocationNm = findViewById(R.id.rv_location_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvLocationNm.setLayoutManager(mLayoutManager);
        rvLocationNm.setItemAnimator(new DefaultItemAnimator());

        rvImage = findViewById(R.id.rv_image_list);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvImage.setLayoutManager(mLayoutManager1);
        rvImage.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.login_button_id:
                ValidationToViews();
                break;
            case R.id.tv_load_image:
            case R.id.firstcamera_id:
                selectImage1();
                break;
        }
    }
String sCompaignNm,sAreaNm,sCompaignObj,sCompaignDetails,allImagNm="",alllocationNm="";;
    public void ValidationToViews(){
        sCompaignNm=etCompaignNm.getText().toString().trim();
//        sAreaNm=etAreaNm.getText().toString().trim();
        sCompaignObj=etCompaignObj.getText().toString().trim();
        sCompaignDetails=etCompaignDetails.getText().toString().trim();
         boolean checkBlank=false;
         int identPos=0;
         for(int i=0;i<arLocDetail.size();i++)
         {
             if(TextUtils.isEmpty(arLocDetail.get(i).getsLocNm()))
             {
                 checkBlank=true;
                 identPos=i;
                 break;
             }
         }

        if (TextUtils.isEmpty(sCompaignNm)) {
            etCompaignNm.setError("Please Enter Compaign Name");
            etCompaignNm.requestFocus();
            return;
        }

       /* else if (TextUtils.isEmpty(sAreaNm)) {
            etAreaNm.setError("Please Enter Compaign Area Name");
            etAreaNm.requestFocus();
            return;
        }*/
      //  totalLength
        else if (arLocDetail.size()<totalLength) {
            Toast.makeText(this, "Please select all location", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (checkBlank) {
            Toast.makeText(this, "Please select location name", Toast.LENGTH_SHORT).show();
            return;
        }

        else if (TextUtils.isEmpty(sCompaignObj)) {
            etCompaignObj.setError("Please Enter Compaign Objective");
            etCompaignObj.requestFocus();
            return;
        } else if (TextUtils.isEmpty(sCompaignDetails)) {
            etCompaignDetails.setError("Please Enter Compaign Description");
            etCompaignDetails.requestFocus();
            return;
        }
        else if (arImageNm.size()<=0) {
            Toast.makeText(this, "Please select Banner Images", Toast.LENGTH_SHORT).show();
            return;
        }
        else {

//            Intent intent = new Intent(DetailsActivity.this, OrderActivity.class);
//            startActivity(intent);
//            Animatoo.animateFade(DetailsActivity.this);

//            Toast.makeText(this, "next screen", Toast.LENGTH_SHORT).show();

            if(appdata.isNetworkConnected(this)){
                AddCompaignDetailBean  compaignBean=new AddCompaignDetailBean();
                compaignBean.setsCompaignNm(sCompaignNm);
                compaignBean.setsCompaignObj(sCompaignObj);
                compaignBean.setsCompaignDetails(sCompaignDetails);
                allImagNm="";
                alllocationNm="";
                JSONArray jsonArray = new JSONArray();
                for(int i=0;i<arLocDetail.size();i++) {
                    JSONObject locObj = new JSONObject();
                    try {
                        locObj.put("c_l_name", arLocDetail.get(i).getsLocNm());
                        locObj.put("c_l_lat", arLocDetail.get(i).getsLocLat());
                        locObj.put("c_l_lng", arLocDetail.get(i).getsLocLong());

                        jsonArray.put(locObj);

                        if(TextUtils.isEmpty(alllocationNm))
                        {
                            alllocationNm=arLocDetail.get(i).getsLocNm();
                        }
                        else
                        {
                            alllocationNm=alllocationNm+","+arLocDetail.get(i).getsLocNm();
                        }

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }


                JSONArray jsonArrayImage = new JSONArray();
                for(int i=0;i<arImageNm.size();i++) {
                    JSONObject locObj = new JSONObject();
                    try {
                        locObj.put("data", arImageNm.get(i).getData());
                        locObj.put("name", arImageNm.get(i).getName());

                        jsonArrayImage.put(locObj);

                        if(TextUtils.isEmpty(allImagNm))
                        {
                            allImagNm=arImageNm.get(i).getName();
                        }
                        else
                        {
                            allImagNm=allImagNm+","+arImageNm.get(i).getName();
                        }

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }



                NextScreenData();
//                presenter.SaveAdvanceCompaignDetail(compaignBean,PlanId,jsonArrayImage,jsonArray);
            }else {
                appdata.ShowNewAlert(this,"Please connect to internet");
            }
        }
    }

static  int CAMERA_REQUEST=3,Result_Load_Image=4;
    int MY_CAMERA_PERMISSION_CODE=11;
    private void selectImage1() {//,"Choose pdf"
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
        builder.setTitle("Add File");
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

                   /* new MaterialFilePicker()
                            .withActivity(DetailsActivity.this)
                            .withRequestCode(1)
                            .withHiddenFiles(true)
                            .withTitle("Sample title")
                            .start();*/
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
//    ArrayList<String>arImage=new ArrayList<>();
    ArrayList<ImageBean>arImageNm=new ArrayList<>();
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Result_Load_Image && data != null && data.getData() != null) {
                Uri filePath2 = data.getData();

                try {
                    Bitmap   bitmap= MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), filePath2);
                    Bitmap bit=Bitmap.createScaledBitmap(bitmap,150,150,false);

                    Uri selectedImage = getImageUri(getApplicationContext(), bit);

                    filePath = getPath(selectedImage);
                    File f = new File(filePath);
                    fileNm = f.getName();
//                    tvFileNm.setText(fileNm);
                    uploadBase64=getFileToBase64_1(f);
                    firstcamera.setImageBitmap(bit);
                    Log.e("","shyam photo size11= "+f.length()+" uploadBase64= "+uploadBase64);
                   if(arImageNm.size()<5) {
//                       arImage.add(filePath);

                       ImageBean bean=new ImageBean();
                       bean.setData(uploadBase64);
                       bean.setName(fileNm);
                       bean.setImagePath(filePath);
                       arImageNm.add(bean);

                       SetImageAdapter();
                   }
                   else {
                       Toast.makeText(this, "Max file only 5", Toast.LENGTH_SHORT).show();
                   }
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
                firstcamera.setImageBitmap(bit);
                Log.e("","shyam file size11= "+f.length()+" uploadBase64= "+uploadBase64);
                if(arImageNm.size()<5) {
//                    arImage.add(filePath);

                    ImageBean bean=new ImageBean();
                    bean.setData(uploadBase64);
                    bean.setName(fileNm);
                    bean.setImagePath(filePath);
                    arImageNm.add(bean);

                    SetImageAdapter();
                }
                else {
                    Toast.makeText(this, "Max file only 5", Toast.LENGTH_SHORT).show();
                }

            }
            else if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

                File f  = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                filePath = f.getAbsolutePath();

                File f1 = new File(filePath);
                fileNm = f1.getName();
//                tvFileNm.setText(fileNm);
//                uploadBase64=getFileToBase64(filePath);
                uploadBase64=getFileToBase64_1(f);
                if(arImageNm.size()<5) {
//                    arImage.add(filePath);

                    ImageBean bean=new ImageBean();
                    bean.setData(uploadBase64);
                    bean.setName(fileNm);
                    bean.setImagePath(filePath);
                    arImageNm.add(bean);
                    SetImageAdapter();
                }
                else {
                    Toast.makeText(this, "Max file only 5", Toast.LENGTH_SHORT).show();
                }
//                firstcamera.setImageBitmap(bit);
                Log.e("","shyam file size22= "+f.length()+" uploadBase64= "+uploadBase64);
            }

            //for pick location name with latitude longitude
            else if (requestCode == 120 && resultCode == Activity.RESULT_OK) {

//                AddCompaignLocBean bean = new AddCompaignLocBean();
//                bean.setsLocNm("");//Compaign Location name
//                bean.setsLocLat("22.71246");
//                bean.setsLocLong("75.86491");
//                arLocDetail.add(bean);
               String loc_name=data.getStringExtra("loc_name");
                String loc_lati=data.getStringExtra("loc_lati");
                String loc_longi=data.getStringExtra("loc_longi");

                arLocDetail.get(adpterPosi).setsLocNm(loc_name);
                arLocDetail.get(adpterPosi).setsLocLat(loc_lati);
                arLocDetail.get(adpterPosi).setsLocLong(loc_longi);
                mAdapter.notifyDataSetChanged();
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

    /*public  String getFileToBase64(String filePath){
        Bitmap bmp = null;
        ByteArrayOutputStream bos = null;
        byte[] bt = null;
        String encodeString = null;
        try{
            bmp = BitmapFactory.decodeFile(filePath);
            bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bt = bos.toByteArray();
            encodeString = Base64.encodeToString(bt, Base64.DEFAULT);
        }catch (Exception e){
            e.printStackTrace();
        }
        return encodeString;
    }*/



  /*  private String EncodeImageIntoBase64(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }
    }

ArrayList<AddCompaignLocBean> arLocDetail=new ArrayList<>();
    public void AddItem()
    {
        if(arLocDetail.size()<totalLength) {
            AddCompaignLocBean bean = new AddCompaignLocBean();
            bean.setsLocNm("");//Compaign Location name
            bean.setsLocLat("22.71246");
            bean.setsLocLong("75.86491");
            arLocDetail.add(bean);
        }
        else
        {
            Toast.makeText(this, "Your max number of cab filled", Toast.LENGTH_SHORT).show();
        }

        SetAdapter();
    }
    public void SetAdapter()
    {
        int size=arLocDetail.size();
        Log.e("","size= "+size);
        mAdapter = new CampaigDetailsAdapter(DetailsActivity.this,arLocDetail,DetailsActivity.this);
        rvLocationNm.setAdapter(mAdapter);
    }
    int adpterPosi=0;
    //adapter click
    @Override
    public void onClick(int position,int diff) {
        adpterPosi=position;
//        Intent intent = new Intent(DetailsActivity.this, DetailsActivity.class);
//        startActivity(intent);
       switch (diff)
        {
            case 1://add location view
                AddItem();
                break;
            case 2://set location data like name and latitude longitude
//                AddItem();//select location
                Intent intent = new Intent(DetailsActivity.this, PickLocation.class);
                startActivityForResult(intent, 120);
                break;
        }

    }
//arImage.add(path);
    public void SetImageAdapter()
    {
        int size=arImageNm.size();
        Log.e("","size= "+size);
        mImgAdapter = new SelectedImageAdapter(DetailsActivity.this,arImageNm,DetailsActivity.this);
        rvImage.setAdapter(mImgAdapter);

    }

    @Override
    public void PhotonClick(int position, int diff) {

    }
 //add advanced details
    @Override
    public void success(String response, String status) {
        NextScreenData();
    }

    public void NextScreenData()
    {
       Gson gson = new Gson();
        String imageData = gson.toJson(
                arImageNm,
                new TypeToken<ArrayList<ImageBean>>() {}.getType());
        String locationData = gson.toJson(
                arLocDetail,
                new TypeToken<ArrayList<AddCompaignLocBean>>() {}.getType());

        appdata.setAdvanceData(ObjectData,sCompaignNm,sCompaignObj,sCompaignDetails,imageData,locationData,allImagNm,alllocationNm);

//        compaignBean
        Intent intent = new Intent(DetailsActivity.this, OrderActivity.class);
//        intent.putExtra("object_data",ObjectData);
//        intent.putExtra("all_image_data",allImagNm);
//        intent.putExtra("all_location_data",alllocationNm);
////        intent.putExtra("all_image_data",imageData);
////        intent.putExtra("all_location_data",locationData);
//        intent.putExtra("compaign_nm",sCompaignNm);
//        intent.putExtra("compaign_obj",sCompaignObj);
//        intent.putExtra("compaign_dsc",sCompaignDetails);
        startActivity(intent);

        Animatoo.animateSlideDown(DetailsActivity.this);
//        String planData,String CamNm,String Camobject,String CamDsc,String allimageData,String allLocData

    }

    @Override
    public void error(String response) {
        appdata.ShowNewAlert(this,response);
    }

    @Override
    public void fail(String response) {
        appdata.ShowNewAlert(this,response);
    }
//    @Override
//    public void ImageClick(int position,int diff) {
//
////        switch (diff)
////        {
////            case 1:
////                AddItem();
////                break;
////            case 2:
//////                AddItem();//select location
////                break;
////        }
//
//    }
}

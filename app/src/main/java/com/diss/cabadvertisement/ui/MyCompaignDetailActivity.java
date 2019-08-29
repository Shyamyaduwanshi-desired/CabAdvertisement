package com.diss.cabadvertisement.ui;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.diss.cabadvertisement.R;
import com.diss.cabadvertisement.ui.adapter.SelectedImageAdapter;
import com.diss.cabadvertisement.ui.adapter.TimeLineAdapter;
import com.diss.cabadvertisement.ui.adapter.UploadImageAdapter;
import com.diss.cabadvertisement.ui.model.CampaignBean;
import com.diss.cabadvertisement.ui.model.ImageBean;
import com.diss.cabadvertisement.ui.model.TimeLineModel;
import com.diss.cabadvertisement.ui.presenter.CampaignPresenter;
import com.diss.cabadvertisement.ui.presenter.VerifyCampaignDataPresenter;
import com.diss.cabadvertisement.ui.util.AppData;


import com.google.gson.Gson;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MyCompaignDetailActivity extends AppCompatActivity implements TimeLineAdapter.CompaignDetailClick,View.OnClickListener, CampaignPresenter.CampaingListData , SelectedImageAdapter.ImageClick, UploadImageAdapter.UploadImageClick , VerifyCampaignDataPresenter.CampaignData {
    RecyclerView recyclerView;
    String[] name = {"Package Amount Received", "Stickers are received by the driver", "Stickers are uploaded on driver vehicle","Campaign Start","Campaign Completed","Feedback"};
    String[] status = {"inactive", "inactive", "inactive","inactive","inactive","inactive"};

    List<TimeLineModel> timeLineModelList;
    TimeLineModel[] timeLineModel;
    Context context;
    LinearLayoutManager linearLayoutManager;
    int countItem=0;
    LinearLayout categorylinear,timelinelinear,lyMain;
    TimeLineAdapter adapter;
    private VerifyCampaignDataPresenter VerifyCamDatapresenter;
    private CampaignPresenter SingleCampaignpresenter;
    AppData appdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_compaigns_details_activity);
        appdata=new AppData(MyCompaignDetailActivity.this);
        VerifyCamDatapresenter = new VerifyCampaignDataPresenter(MyCompaignDetailActivity.this, MyCompaignDetailActivity.this);
        SingleCampaignpresenter = new CampaignPresenter(MyCompaignDetailActivity.this, MyCompaignDetailActivity.this);
        InitCompo();
        Listener();
        GetIntentData();
    }
    String PlanId="",campaign_id="";
        ArrayList<CampaignBean> arDriverCampaign=new ArrayList<>();
    private void GetIntentData() {
     String   ObjectDataInit = getIntent().getStringExtra("object_data");
        Gson gson = new Gson();
        CampaignBean companyCampaignbean = gson.fromJson(ObjectDataInit, CampaignBean.class);
        PlanId =companyCampaignbean.getPlan_id();
        campaign_id =companyCampaignbean.getID();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetSingleCampaingData();
    }

    private void GetSingleCampaingData() {

        if(appdata.isNetworkConnected(this)){
            SingleCampaignpresenter.GetSingleCampaignData(campaign_id);
        }else {
            appdata.ShowNewAlert(this,"Please connect to internet");
        }
    }

    private void Listener() {
        ivBack.setOnClickListener(this);

    }
ImageView ivBack;
    TextView tvCampaignNm,tvCampaignAmt,tvValidityDate,tvNoData;
    public void InitCompo()
    {
        tvCampaignNm=findViewById(R.id.tv_compaign_name);
        tvCampaignAmt=findViewById(R.id.tv_compaign_amount);
        tvValidityDate=findViewById(R.id.tv_validity);

        tvNoData=findViewById(R.id.tv_no_data);

        ivBack=findViewById(R.id.imageback);
        categorylinear=(LinearLayout)findViewById(R.id.category_linear_id);
        timelinelinear=(LinearLayout)findViewById(R.id.timeline_linear_id);
        lyMain=findViewById(R.id.ly_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_recommennd);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        categorylinear.requestFocus();

    }
    public void setAdapter()
    {
        timeLineModelList = new ArrayList<>();
        int size = name.length;
        timeLineModel = new TimeLineModel[size];
        context = MyCompaignDetailActivity.this;
        linearLayoutManager = new LinearLayoutManager(this);

        for (int i = 0; i < size; i++) {
            timeLineModel[i] = new TimeLineModel();
            timeLineModel[i].setName(name[i]);
            timeLineModel[i].setStatus(status[i]);
            timeLineModelList.add(timeLineModel[i]);
        }

         adapter= new TimeLineAdapter(context, timeLineModelList, MyCompaignDetailActivity.this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imageback:
                finish();
                Animatoo.animateSlideDown(MyCompaignDetailActivity.this);
                break;
        }
    }

int RecyclePosti=0;
    @Override
    public void CompaignOnClick(int position, int diff) {
        RecyclePosti=position;
         if(position==(countItem))
         {
             switch (position) {
               case 1://Stickers are received by the driver
                     Intent intent = new Intent(MyCompaignDetailActivity.this, ACampaignsDriverActivity.class);
                     intent.putExtra("object_data",ObjectData);
                     intent.putExtra("diff_",0+"");//0
                     Animatoo.animateSlideUp(MyCompaignDetailActivity.this);
                     startActivityForResult(intent, 100);
                     break;
                 case 2://Stickers are uploaded on driver vehicle
                     Intent intent1 = new Intent(MyCompaignDetailActivity.this, ACampaignsDriverActivity.class);
                     intent1.putExtra("object_data",ObjectData);
                     intent1.putExtra("diff_",1+"");//1
                     Animatoo.animateSlideUp(MyCompaignDetailActivity.this);
                     startActivityForResult(intent1, 100);
                     break;

                case 3://Campaign Start
//                     showRecievLocDlg(position, "Campaign Start", "");//change  function according to mustafa

                    Intent intent5 = new Intent(MyCompaignDetailActivity.this, ACampaignsDriverActivity.class);
                    intent5.putExtra("object_data",ObjectData);
                    intent5.putExtra("diff_",2+"");//
                    Animatoo.animateSlideUp(MyCompaignDetailActivity.this);
                    startActivityForResult(intent5, 100);
                     break;
                 case 4://Campaign Completed
                     Intent intent4 = new Intent(MyCompaignDetailActivity.this, ACampaignsDriverActivity.class);
                     intent4.putExtra("object_data",ObjectData);
                     intent4.putExtra("diff_",3+"");//(countItem-1)
                     Animatoo.animateSlideUp(MyCompaignDetailActivity.this);
                     startActivityForResult(intent4, 100);
                     break;
                case 5://Feedback
//                    Intent intent5 = new Intent(MyCompaignDetailActivity.this, ACampaignsDriverActivity.class);
//                    intent5.putExtra("object_data",ObjectData);
//                    intent5.putExtra("diff_",4+"");//
//                    Animatoo.animateSlideUp(MyCompaignDetailActivity.this);
//                    startActivityForResult(intent5, 100);

                    CompanyFeedbackDlg();
                     break;
             }

         }

    }
     Dialog VerifyCamDatadialog;
    public void showRecievLocDlg(final int position,String title,String loc){
         VerifyCamDatadialog = new Dialog(this);
        VerifyCamDatadialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        VerifyCamDatadialog.setCancelable(false);
        VerifyCamDatadialog.setContentView(R.layout.dialog_recieve_loc);
        TextView TvLocNmTitle=VerifyCamDatadialog.findViewById(R.id.tv_loc_txt);
        TextView TvLocNm=VerifyCamDatadialog.findViewById(R.id.tv_loc);
        TextView tvYes=VerifyCamDatadialog.findViewById(R.id.tv_yes);
        TextView tvNo=VerifyCamDatadialog.findViewById(R.id.tv_no);
        TvLocNmTitle.setText(title);
        if(TextUtils.isEmpty(loc))
        {
            TvLocNm.setVisibility(View.GONE);
//            TvLocNm.setText("Location: "+loc);
        }
        else
        {
            TvLocNm.setVisibility(View.VISIBLE);
            TvLocNm.setText(loc);
        }

        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(appdata.isNetworkConnected(MyCompaignDetailActivity.this)){

             VerifyCamDatapresenter.StartCampaign(arDriverCampaign.get(0).getID());

                }else {
                    appdata.ShowNewAlert(MyCompaignDetailActivity.this,"Please connect to internet");
                }
            }
        });
        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerifyCamDatadialog.dismiss();
            }
        });
        VerifyCamDatadialog.show();
    }

//sticker adapter
    @Override
    public void PhotonClick(int position, int diff) {
        Toast.makeText(context, ""+position, Toast.LENGTH_SHORT).show();
    }

    RatingBar rtBar;
    EditText etFeedbackDsc;
    Dialog feedBackDlg;
    public void CompanyFeedbackDlg(){
        feedBackDlg = new Dialog(this);
        feedBackDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        feedBackDlg.setCancelable(false);
        feedBackDlg.setContentView(R.layout.dailog_company_feedback);
        RelativeLayout rlClose=feedBackDlg.findViewById(R.id.rl_close);
         rtBar=feedBackDlg.findViewById(R.id.ratingBar);
         etFeedbackDsc=feedBackDlg.findViewById(R.id.et_feedback_dsc);
        Button btSubmit=feedBackDlg.findViewById(R.id.bt_submit_feedbac);

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(ValidFeedback()) {

                     if(appdata.isNetworkConnected(MyCompaignDetailActivity.this)){
                         VerifyCamDatapresenter.CompanyFeedback1(arDriverCampaign.get(0).getID(),String.valueOf(feebackRatting),sfeedbackDsc);

                     }else {
                         appdata.ShowNewAlert(MyCompaignDetailActivity.this,"Please connect to internet");
                     }
                 }
            }
        });
        rlClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                feedBackDlg.dismiss();
            }
        });
        feedBackDlg.show();
    }
 String sfeedbackDsc="";
    float feebackRatting=0;
    private boolean ValidFeedback() {
        boolean feedbackFlag=true;
        sfeedbackDsc=etFeedbackDsc.getText().toString().trim();
        feebackRatting = rtBar.getRating();
        if(feebackRatting==0||feebackRatting==0.0)
        {
            Toast.makeText(context, "Please Rate this App", Toast.LENGTH_SHORT).show();
            feedbackFlag=false;
        }
        else if(TextUtils.isEmpty(sfeedbackDsc))
        {
            etFeedbackDsc.setError("Please Enter Feedback Description");
            etFeedbackDsc.requestFocus();
            feedbackFlag=false;
        }
        return feedbackFlag;
    }

//upload adapter click
    @Override
    public void UploadPhotoClick(int position, int diff) {
        Toast.makeText(context, ""+position, Toast.LENGTH_SHORT).show();
    }


    static  int CAMERA_REQUEST=3,Result_Load_Image=4;
    int MY_CAMERA_PERMISSION_CODE=11;
    private void selectImage1() {//,"Choose pdf"
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(MyCompaignDetailActivity.this);
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
                    uploadBase64=getFileToBase64_1(f);
                    Log.e("","shyam photo size11= "+f.length()+" uploadBase64= "+uploadBase64);
//                    if(arUploadImage.size()<5) {
//
//                        ImageBean bean=new ImageBean();
//                        bean.setData(uploadBase64);
//                        bean.setName(fileNm);
//                        bean.setImagePath(filePath);
//                        arUploadImage.add(bean);
//                        mUploadImgAdapter.notifyDataSetChanged();
//                    }
//                    else {
//                        Toast.makeText(this, "Max file only 5", Toast.LENGTH_SHORT).show();
//                    }
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
                uploadBase64=getFileToBase64_1(f);
                Log.e("","shyam file size11= "+f.length()+" uploadBase64= "+uploadBase64);
//                if(arUploadImage.size()<5) {
//                    ImageBean bean=new ImageBean();
//                    bean.setData(uploadBase64);
//                    bean.setName(fileNm);
//                    bean.setImagePath(filePath);
//                    arUploadImage.add(bean);
//
////                    SetUploadImageAdapter();
//                    mUploadImgAdapter.notifyDataSetChanged();
//                }
//                else {
//                    Toast.makeText(this, "Max file only 5", Toast.LENGTH_SHORT).show();
//                }

            }
            else if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

                File f  = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                filePath = f.getAbsolutePath();

                File f1 = new File(filePath);
                fileNm = f1.getName();
                uploadBase64=getFileToBase64_1(f);
//                if(arUploadImage.size()<5) {
//                    ImageBean bean=new ImageBean();
//                    bean.setData(uploadBase64);
//                    bean.setName(fileNm);
//                    bean.setImagePath(filePath);
//                    arUploadImage.add(bean);
//                    mUploadImgAdapter.notifyDataSetChanged();
//                }
//                else {
//                    Toast.makeText(this, "Max file only 5", Toast.LENGTH_SHORT).show();
//                }
                Log.e("","shyam file size22= "+f.length()+" uploadBase64= "+uploadBase64);
            }
            else if (requestCode == 100 && resultCode == Activity.RESULT_OK) {//get result from driver screen

               String doneText= data.getStringExtra("result");

               if(doneText.equals("done")) {

//                   switch (countItem)
//                   {
//                       case 1:
//                           break;
//                   }

                   countItem = RecyclePosti+1;
//                   countItem = 2;
//                stickerDlg.dismiss();
                   timeLineModel[RecyclePosti].setStatus("active");
                   adapter.notifyDataSetChanged();

               }
               else
               {
                   Toast.makeText(this, "not verify",
                           Toast.LENGTH_LONG).show();
               }


            }
            /*else if (requestCode == 101 && resultCode == Activity.RESULT_OK) {//get result from driver screen

              *//* String doneText= data.getStringExtra("result");
               if(doneText.equals("done")) {
//                   countItem = 3;
                   countItem = RecyclePosti+1;

//                stickerDlg.dismiss();
                   timeLineModel[RecyclePosti].setStatus("active");
                   adapter.notifyDataSetChanged();
               }
               else
               {
                   Toast.makeText(this, "not verify",
                           Toast.LENGTH_LONG).show();
               }*//*
            }*/

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

    private void setViewData() {
            if(arDriverCampaign.size()>0)
            {
                tvCampaignNm.setText(arDriverCampaign.get(0).getC_name());//getC_l_name
                tvCampaignAmt.setText("Rs. "+arDriverCampaign.get(0).getPlan_amount());
                tvValidityDate.setText("( "+appdata.ConvertDate(arDriverCampaign.get(0).getAdded_on())+" - "+appdata.ConvertDate01(arDriverCampaign.get(0).getLastdate())+" )");//tvValidityDate

                lyMain.setVisibility(View.VISIBLE);
                tvNoData.setVisibility(View.GONE);
//                countItem=1;
                int setPosti=Integer.parseInt(arDriverCampaign.get(0).getVerifydataStatus());
                if(setPosti>=4)
                { //Stickers Uploaded Driver Vehicle approved by company and Ready to Go! and status =4 recycle positin=2
                    countItem = Integer.parseInt(arDriverCampaign.get(0).getVerifydataStatus()) - 2;//
                }
                else {
                    countItem = Integer.parseInt(arDriverCampaign.get(0).getVerifydataStatus()) - 1;//initial position 2
                }
                 Log.e("","countItem= "+countItem);
                for(int i=0;i<countItem;i++)
                {
                    status[i]="active";
                }
                setAdapter();

            }
            else
            {
                countItem=0;
                lyMain.setVisibility(View.GONE);
                tvNoData.setVisibility(View.VISIBLE);
            }

    }


    //for campaign data verification
    @Override
    public void success(String response, String status) {
        switch (status)
        {
            case "PhotoVechileWithSticker":
//            case "feedback":
//                if(feedBackDlg!=null)
//                {
//                    feedBackDlg.dismiss();
//                }

                break;
            case "feedback":
            case "StartCampaign":
            case "VerifyCampaign":
                if(VerifyCamDatadialog!=null)
                {
                    VerifyCamDatadialog.dismiss();
                }
                if(feedBackDlg!=null)
                {
                    feedBackDlg.dismiss();
                }
                UpdateViewData();
                break;
        }
    }

    private void UpdateViewData() {

        if(feedBackDlg!=null)
        {
            feedBackDlg.dismiss();
        }

        countItem=RecyclePosti+1;
        timeLineModel[RecyclePosti].setStatus("active");
        adapter.notifyDataSetChanged();
    }


    //for campaign data verification
    @Override
    public void error(String response,String status) {
        appdata.ShowNewAlert(this,response);
    }
    //for campaign data verification
    @Override
    public void fail(String response,String status) {
        appdata.ShowNewAlert(this,response);
    }
//single camapign data
String ObjectData="";
    @Override
    public void success(ArrayList<CampaignBean> response, String status) {
        arDriverCampaign.clear();
        ObjectData="";
        arDriverCampaign=response;
        Gson gson = new Gson();
        ObjectData = gson.toJson(arDriverCampaign.get(0));//because there is a single object
        Log.e("","jsonObj= "+ObjectData);
        setViewData();
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
package com.diss.cabadvertisement.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.diss.cabadvertisement.ui.adapter.DriverACampaignAdapter;
import com.diss.cabadvertisement.ui.adapter.StickerImageAdapter;
import com.diss.cabadvertisement.ui.model.CampaignBean;
import com.diss.cabadvertisement.ui.model.DriverCampaignBean;
import com.diss.cabadvertisement.ui.model.ImageBean;
import com.diss.cabadvertisement.ui.presenter.CampaignDetailsPresenter;
import com.diss.cabadvertisement.ui.presenter.VerifyCampaignDataPresenter;
import com.diss.cabadvertisement.ui.util.AppData;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ACampaignsDriverActivity extends AppCompatActivity implements View.OnClickListener,DriverACampaignAdapter.CompainDetailClick, CampaignDetailsPresenter.CampaingDriverListData,StickerImageAdapter.ImageClick, VerifyCampaignDataPresenter.CampaignData {

    ImageView imageViewback;

    TextView tvNoData;
    private RecyclerView rvCompaign;
    private RecyclerView.Adapter mAdapter;
    private CampaignDetailsPresenter presenter;
    private VerifyCampaignDataPresenter VerifyCamDatapresenter;
    AppData appdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campaing_driver_activity);
        appdata=new AppData(ACampaignsDriverActivity.this);
        presenter = new CampaignDetailsPresenter(ACampaignsDriverActivity.this, ACampaignsDriverActivity.this);
        VerifyCamDatapresenter = new VerifyCampaignDataPresenter(ACampaignsDriverActivity.this, ACampaignsDriverActivity.this);
        InitCompo();
        Listener();
        GetIntentData();
    }
    String ObjectData="",diff="";
    String campaignId="",campStatus="";
    CampaignBean companyCampaignbean;
    private void GetIntentData() {
        ObjectData = getIntent().getStringExtra("object_data");
        diff = getIntent().getStringExtra("diff_");
        Gson gson = new Gson();
         companyCampaignbean = gson.fromJson(ObjectData, CampaignBean.class);
        campaignId =companyCampaignbean.getID();
        campStatus =companyCampaignbean.getVerifydataStatus();

        Log.e("","ObjectData= "+ObjectData+"campaignId= "+campaignId);
        GetDriverAccordingToCampData(1);



    }

    private void Listener() {
        imageViewback.setOnClickListener(this);
        btSubmit.setOnClickListener(this);
    }
    Button btSubmit;
    public void InitCompo()
    {

        imageViewback=findViewById(R.id.imageback);
        tvNoData=findViewById(R.id.tv_no_data);
        rvCompaign = findViewById(R.id.rv_driver_list);
        rvCompaign.setLayoutManager(new GridLayoutManager(this, 2));
        rvCompaign.setItemAnimator(new DefaultItemAnimator());
        btSubmit=findViewById(R.id.bt_submit);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imageback:
                finish();
                Animatoo.animateSlideUp(this);
                break;
            case R.id.login_button_id:
//                addValidationToViews();
                break;
            case R.id.bt_submit:
//                Intent returnIntent = new Intent();
//                returnIntent.putExtra("result","done");
//                setResult(RESULT_OK,returnIntent);
//                finish();
//                Animatoo.animateSlideUp(this);

                GetDriverAccordingToCampData(4);


                break;
        }
    }


    //campaign driver list data
    @Override
    public void success(ArrayList<DriverCampaignBean> respons, String status, String locationsCovered,String total_kms,String total_hrs) {
        switch (status)
        {
            case "1":
                arAllDriverACampaing.clear();
                arAllDriverACampaing=respons;
                SetAdapter();
                DoStartCampaign();//for start campaign button
                break;
           /* case "2":

                break;*/
        }

    }

    @Override
    public void error(String response) {
        appdata.ShowNewAlert(this,response);
    }

    @Override
    public void fail(String response) {
        appdata.ShowNewAlert(this,response);
    }

    private void GetDriverAccordingToCampData(int diff_api) {
        if(appdata.isNetworkConnected(this)){
            switch (diff_api)
            {
                case 1:
                    presenter.GetAllDriverAccordingToCampaign(campaignId);
                    break;
                case 2://accepted driver sticker
                    VerifyCamDatapresenter.AcceptedDriverSticker(arAllDriverACampaing.get(driverPos).getDriver_id(),arAllDriverACampaing.get(driverPos).getCompaignId());
                    break;
               case 3://rejected driver sticker
                   VerifyCamDatapresenter.RejectedDriverSticker(arAllDriverACampaing.get(driverPos).getDriver_id(),arAllDriverACampaing.get(driverPos).getCompaignId());
                    break;
               case 4://start campaign

                   if(appdata.isNetworkConnected(this)){
                       VerifyCamDatapresenter.StartCampaign(campaignId);
                   }else {
                       appdata.ShowNewAlert(this,"Please connect to internet");
                   }
                    break;
            }

        }else {
            appdata.ShowNewAlert(this,"Please connect to internet");
        }
    }
    ArrayList<DriverCampaignBean> arAllDriverACampaing=new ArrayList<>();
    public void SetAdapter()
    {
        int size=arAllDriverACampaing.size();
        Log.e("","size= "+size);
        mAdapter = new DriverACampaignAdapter(this,arAllDriverACampaing,this);
        rvCompaign.setAdapter(mAdapter);

        if(arAllDriverACampaing.size()>0)
        {
            tvNoData.setVisibility(View.GONE);
        }
        else
        {
            tvNoData.setVisibility(View.VISIBLE);
        }

    }

    public void DoStartCampaign()
    {
//        boolean checkflag=false;
        int contLength=0;
        for(int i=0;i<arAllDriverACampaing.size();i++)
        {
            int status=Integer.parseInt(arAllDriverACampaing.get(i).getPackageAmountStatus());
            if(status>=9)
            {
                contLength=contLength+1;
            }
        }
        if((campStatus.equals("5")||campStatus=="5")&&(contLength==arAllDriverACampaing.size()))
        {
            btSubmit.setVisibility(View.VISIBLE);
        }
        else
        {
            btSubmit.setVisibility(View.GONE);
        }
    }

    int driverPos=0;
//driver adpter click
    @Override
    public void onClick(int position, int diff_) {
//        Toast.makeText(this, ""+diff, Toast.LENGTH_SHORT).show();

        driverPos=position;
        int showStatus=Integer.parseInt(arAllDriverACampaing.get(driverPos).getPackageAmountStatus());
        switch (diff)
        {
            case "0"://Stickers are received by the driver
                showStatusdialog();
                break;
            case "1"://Stickers are uploaded on driver vehicle

                if(showStatus>=7&&showStatus<=12)//approved driver photo sticker
                {
                    Toast.makeText(this, "You have already approved photo sticker for this driver", Toast.LENGTH_SHORT).show();
                }
                else if(showStatus==13)//reject driver photo sticker
                {
                    Toast.makeText(this, "You have already disapproved photo sticker for this driver", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    ShowStickerReceived();
                }
//                ShowStickerReceived();
                break;
            case "2"://Campaign Start
                showStatusdialog();
                break;
            case "3"://Campaign Completed
                showStatusdialog();
                break;
            case "4"://Feedback

                if(showStatus==12)
                {
                    Toast.makeText(this, "You have submit feedback for this driver", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    CompanyFeedbackDlg();
                }
//               CompanyFeedbackDlg();
                break;
        }
    }
    RecyclerView rvImage;
    Dialog driverPhotoVehicleWithStickerDlg;
    private void ShowStickerReceived() {
        if(driverPhotoVehicleWithStickerDlg!=null)
        {
            driverPhotoVehicleWithStickerDlg=null;
        }
        driverPhotoVehicleWithStickerDlg = new Dialog(ACampaignsDriverActivity.this);
        driverPhotoVehicleWithStickerDlg.setContentView(R.layout.dailog_stiker_approve_by_company);

        TextView Title = (TextView) driverPhotoVehicleWithStickerDlg.findViewById(R.id.tv_title);
        TextView driverId = (TextView) driverPhotoVehicleWithStickerDlg.findViewById(R.id.tv_driver_id);
        TextView tvApproved = (TextView) driverPhotoVehicleWithStickerDlg.findViewById(R.id.tv_approved);
        TextView disApproved = (TextView) driverPhotoVehicleWithStickerDlg.findViewById(R.id.tv_dis_approved);
        driverId.setText("# "+arAllDriverACampaing.get(driverPos).getDriver_id());
        arImageNm.clear();
        if(TextUtils.isEmpty(arAllDriverACampaing.get(driverPos).getImage()))
        {
//                ImageBean bean=new ImageBean();
//                bean.setData("");
//                bean.setName("");
//                bean.setImagePath("http://cabadvert.webdesigninguk.co/assets/images/default.png");
//                arImageNm.add(bean);
        }
        else
        {
            try {
                JSONArray jsArray = new JSONArray(arAllDriverACampaing.get(driverPos).getImage());
                Log.e("",""+jsArray.length());
                for (int i = 0; i < jsArray.length(); i++) {
                    Log.e("","image url="+jsArray.get(i).toString());
                    ImageBean bean = new ImageBean();
                    bean.setData("");
                    bean.setName("");
                    bean.setImagePath(jsArray.get(i).toString());
                    arImageNm.add(bean);
                }
            } catch (JSONException e) {
                Log.e("",""+e);
                e.printStackTrace();
            }
        }

        switch (diff)
        {
            case "1"://Stickers are uploaded on driver vehicle
                if(arImageNm.size()>0) {
                    Title.setText("Sticker Uploaded");
                }
                else
                {
                    Title.setText("Sticker Not Uploaded");
                    tvApproved.setVisibility(View.INVISIBLE);
                    disApproved.setVisibility(View.INVISIBLE);
                }
                break;
        }

        rvImage = driverPhotoVehicleWithStickerDlg.findViewById(R.id.rv_image_list);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvImage.setLayoutManager(mLayoutManager1);
        rvImage.setItemAnimator(new DefaultItemAnimator());
        SetImageAdapter();

        tvApproved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GetDriverAccordingToCampData(2);

            }
        });
        disApproved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetDriverAccordingToCampData(3);
            }
        });
        driverPhotoVehicleWithStickerDlg.show();
    }


    ArrayList<ImageBean>arImageNm=new ArrayList<>();
    private RecyclerView.Adapter mImgAdapter;
    public void SetImageAdapter()
    {
        int size=arImageNm.size();
        Log.e("","size= "+size);
        mImgAdapter = new StickerImageAdapter(this,arImageNm,this);
        rvImage.setAdapter(mImgAdapter);

    }
//image adapter click
    @Override
    public void PhotonClick(int position, int diff) {

    }

     private void showStatusdialog() {
        final Dialog dialog = new Dialog(ACampaignsDriverActivity.this);
        dialog.setContentView(R.layout.campaigns_status_activity);

        TextView text1 = (TextView) dialog.findViewById(R.id.idtext_id);
        TextView tvDriverId = (TextView) dialog.findViewById(R.id.idnumber_id);
        TextView text3 = (TextView) dialog.findViewById(R.id.status_id);
        TextView tvStatus = (TextView) dialog.findViewById(R.id.recived_id);
        final LinearLayout recivedlinear=(LinearLayout)dialog.findViewById(R.id.recived_linear_id);

         Button DoneButton = (Button) dialog.findViewById(R.id.okk_button_id);
         tvDriverId.setText("# "+arAllDriverACampaing.get(driverPos).getDriver_id());

         int showStatus=Integer.parseInt(arAllDriverACampaing.get(driverPos).getPackageAmountStatus());
         switch (diff)
         {
             case "0"://Stickers are received by the driver

                 if(showStatus>=4)
                 {
                     tvStatus.setTextColor(ContextCompat.getColor(ACampaignsDriverActivity.this, R.color.recieve_package_amount));
                     tvStatus.setText("Recived");
                 }
                 else
                 {
                     tvStatus.setTextColor(ContextCompat.getColor(ACampaignsDriverActivity.this, R.color.not_recieve_package_amount));
                     tvStatus.setText("Not Recived");
                 }

                 break;
             case "2"://Campaign Start

                 if(showStatus>=9)
                 {
                     tvStatus.setTextColor(ContextCompat.getColor(ACampaignsDriverActivity.this, R.color.recieve_package_amount));
                     tvStatus.setText("Ready to Go");
                 }
                 else
                 {
                     tvStatus.setTextColor(ContextCompat.getColor(ACampaignsDriverActivity.this, R.color.not_recieve_package_amount));
                     tvStatus.setText("Not Ready to Go");
                 }

                 break;

             case "3"://Campaign Completed

                 if(showStatus>=12)
                 {
                     tvStatus.setTextColor(ContextCompat.getColor(ACampaignsDriverActivity.this, R.color.recieve_package_amount));
                     tvStatus.setText("Completed");
                 }
                 else
                 {
                     tvStatus.setTextColor(ContextCompat.getColor(ACampaignsDriverActivity.this, R.color.not_recieve_package_amount));
                     tvStatus.setText("Not completed");
                 }
                 break;
            case "4"://
                 tvStatus.setText("");
                 break;
         }
        DoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


         dialog.show();
    }
//accept/reject/ driver uploaded vehicle with sticker,feedback manage,start campaign

    @Override
    public void success(String response, String status) {
        if(driverPhotoVehicleWithStickerDlg!=null)
        {
            driverPhotoVehicleWithStickerDlg.dismiss();
        }
        if(feedBackDlg!=null)
        {
            feedBackDlg.dismiss();
        }

        switch (status)
        {
            case "feedback":
                arAllDriverACampaing.get(driverPos).setPackageAmountStatus("12");
                if(mAdapter!=null)
                {
                    mAdapter.notifyDataSetChanged();
                }

                break;
            case "accepted_driver_sticker"://accept photo vehicle with sticker

                arAllDriverACampaing.get(driverPos).setPackageAmountStatus("7");
                if(mAdapter!=null)
                {
                    mAdapter.notifyDataSetChanged();
                }

                break;
            case "rejected_driver_sticker"://reject photo vehicle with sticker
                arAllDriverACampaing.get(driverPos).setPackageAmountStatus("13");
                if(mAdapter!=null)
                {
                    mAdapter.notifyDataSetChanged();
                }

                break;
           case "StartCampaign"://start campaign

               onBackPressed();
                break;

           default:

                  appdata.ShowNewAlert(this,response);
                break;
        }


    }

    @Override
    public void error(String response, String status) {
        appdata.ShowNewAlert(this,response);
    }

    @Override
    public void fail(String response, String status) {
        appdata.ShowNewAlert(this,response);
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
        Button btSubmitDlt=feedBackDlg.findViewById(R.id.bt_submit_feedbac);

        btSubmitDlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ValidFeedback()) {

                    if(appdata.isNetworkConnected(ACampaignsDriverActivity.this)){
//                        VerifyCamDatapresenter.CompanyFeedback(arAllDriverACampaing.get(driverPos).getDriver_id(),arAllDriverACampaing.get(driverPos).getCompaignId(),String.valueOf(feebackRatting),sfeedbackDsc);

                    }else {
                        appdata.ShowNewAlert(ACampaignsDriverActivity.this,"Please connect to internet");
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
            Toast.makeText(this, "Please Rate this App", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Animatoo.animateSlideDown(this);
    }
}

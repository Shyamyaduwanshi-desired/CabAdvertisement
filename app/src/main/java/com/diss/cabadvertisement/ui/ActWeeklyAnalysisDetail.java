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

public class ActWeeklyAnalysisDetail extends AppCompatActivity implements View.OnClickListener,DriverACampaignAdapter.CompainDetailClick, CampaignDetailsPresenter.CampaingDriverListData {

    ImageView imageViewback;

    TextView tvNoData;
    private RecyclerView rvCompaign;
    private RecyclerView.Adapter mAdapter;
    private CampaignDetailsPresenter presenter;
//    private VerifyCampaignDataPresenter VerifyCamDatapresenter;
    AppData appdata;
    int diffDlg=0;
    TextView tvMonth,tvWeek,tvYesterday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_weekly_analysis_detail);
        appdata=new AppData(ActWeeklyAnalysisDetail.this);
        presenter = new CampaignDetailsPresenter(ActWeeklyAnalysisDetail.this, ActWeeklyAnalysisDetail.this);
//        VerifyCamDatapresenter = new VerifyCampaignDataPresenter(ActWeeklyAnalysisDetail.this, ActWeeklyAnalysisDetail.this);
        InitCompo();
        Listener();
        GetIntentData();
    }
//    String ObjectData=""/*,diff=""*/;
    String campaignId="",reportType="";
//    CampaignBean companyCampaignbean;
    private void GetIntentData() {
        campaignId = getIntent().getStringExtra("s_id_");
        reportType="yesterday";
        Log.e("","campaignId= "+campaignId);
        GetDriverAccordingToCampData(1);



    }

    private void Listener() {
        imageViewback.setOnClickListener(this);
        btCampaignReport.setOnClickListener(this);
        tvMonth.setOnClickListener(this);
        tvWeek.setOnClickListener(this);
        tvYesterday.setOnClickListener(this);
    }
    Button btCampaignReport;
    public void InitCompo()
    {

        imageViewback=findViewById(R.id.imageback);
        tvNoData=findViewById(R.id.tv_no_data);
        rvCompaign = findViewById(R.id.rv_driver_list);
        rvCompaign.setLayoutManager(new GridLayoutManager(this, 2));
        rvCompaign.setItemAnimator(new DefaultItemAnimator());
        btCampaignReport=findViewById(R.id.bt_campaign_report);

        tvMonth=findViewById(R.id.tv_month);
        tvWeek=findViewById(R.id.tv_week);
        tvYesterday=findViewById(R.id.tv_yesterday);
        SetView(R.id.tv_yesterday);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imageback:
                finish();
                Animatoo.animateSlideUp(this);
                break;

            case R.id.tv_month:
                reportType="month";
               SetView(R.id.tv_month);
                GetDriverAccordingToCampData(1);
                break;
            case R.id.tv_week:
                reportType="week";
                SetView(R.id.tv_week);
                GetDriverAccordingToCampData(1);
                break;
           case R.id.tv_yesterday:
               reportType="yesterday";
               SetView(R.id.tv_yesterday);
               GetDriverAccordingToCampData(1);
                break;
            case R.id.bt_campaign_report:
                diffDlg=2;

                ShowDriverAnalysisDlg();
//                GetDriverAccordingToCampData(4);
                break;
        }
    }

    private void SetView(int tv_month) {
        tvMonth.setBackgroundResource(R.drawable.normal_bg);
        tvWeek.setBackgroundResource(R.drawable.normal_bg);
        tvYesterday.setBackgroundResource(R.drawable.normal_bg);
        switch (tv_month)
        {
            case R.id.tv_month:
                tvMonth.setBackgroundResource(R.drawable.selected_bg);
                break;
            case R.id.tv_week:
                tvWeek.setBackgroundResource(R.drawable.selected_bg);
                break;
            case R.id.tv_yesterday:
                tvYesterday.setBackgroundResource(R.drawable.selected_bg);
                break;
        }



    }

 String campaignLocationsCovered, campaignTotal_kms, campaignTotal_hrs;
    //all driver detail according to campaignId
    @Override
    public void success(ArrayList<DriverCampaignBean> respons, String status, String locationsCovered,String total_kms,String total_hrs) {
        switch (status)
        {
           /* case "1":
                break;*/
            case "2":
                campaignLocationsCovered=locationsCovered;
                campaignTotal_kms=total_kms;
                campaignTotal_hrs=total_hrs;
                arAllDriverACampaing.clear();
                arAllDriverACampaing=respons;
                SetAdapter();
                break;
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
                    presenter.GetAllDriverAllDataAcrdToCampaignId(campaignId,reportType);
                    break;
                case 2:
                    break;
               case 3:
                    break;
               case 4:
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



    int driverPos=0;
//driver adpter click
    @Override
    public void onClick(int position, int diff_) {
        driverPos=position;
        diffDlg=1;
        ShowDriverAnalysisDlg();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Animatoo.animateSlideDown(this);
    }
    private void ShowDriverAnalysisDlg() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dailog_analysis_info);
        TextView tvDriverId = (TextView) dialog.findViewById(R.id.tv_driver_id);
        TextView tvLocation = (TextView) dialog.findViewById(R.id.tv_covered_loc);
        TextView tvTotalKm = (TextView) dialog.findViewById(R.id.tv_total_km);
        TextView tvTotalHr = (TextView) dialog.findViewById(R.id.tv_total_hr);
        dialog.show();

        switch (diffDlg)
        {
            case 1:
                tvDriverId.setText("#"+arAllDriverACampaing.get(driverPos).getDriver_id());
                tvLocation.setText(arAllDriverACampaing.get(driverPos).getC_l_name());
                tvTotalKm.setText(arAllDriverACampaing.get(driverPos).getTotal_km() + " Km.");
                tvTotalHr.setText(arAllDriverACampaing.get(driverPos).getTotal_hours() + " Hours");
                break;
            case 2:
                tvDriverId.setText("Campaign Report");
                tvLocation.setText(campaignLocationsCovered);
                tvTotalKm.setText(campaignTotal_kms+" Km");
                tvTotalHr.setText(campaignTotal_hrs+" Hours");
                break;
        }



        Button DoneButton = (Button) dialog.findViewById(R.id.ok_button_id);
        DoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
}

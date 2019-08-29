package com.diss.cabadvertisement.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.diss.cabadvertisement.R;
import com.diss.cabadvertisement.ui.adapter.MyCampaigListAdapter;
import com.diss.cabadvertisement.ui.adapter.SubPlanAdapter;
import com.diss.cabadvertisement.ui.model.CampaignBean;
import com.diss.cabadvertisement.ui.model.SubPlanBean;
import com.diss.cabadvertisement.ui.presenter.CampaignPresenter;
import com.diss.cabadvertisement.ui.presenter.SubscriptionPlanPresenter;
import com.diss.cabadvertisement.ui.util.AppData;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MyCampaignsActivity extends AppCompatActivity implements View.OnClickListener,MyCampaigListAdapter.CompainDetailClick, CampaignPresenter.CampaingListData{

    AppData appdata;
    ImageView imageViewback;
    TextView tvNoData;
    private RecyclerView rvCompaign;
    private RecyclerView.Adapter mAdapter;
    private CampaignPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_compaigns_activity);
        appdata=new AppData(MyCampaignsActivity.this);
        presenter = new CampaignPresenter(MyCampaignsActivity.this, MyCampaignsActivity.this);
        InitCompo();
        Listener();
        GetCompaignData();
    }

    private void GetCompaignData() {
        if(appdata.isNetworkConnected(this)){
            presenter.GetCampaingList();
        }else {
            appdata.ShowNewAlert(this,"Please connect to internet");
        }
    }

    private void Listener() {
//        mycampaigns.setOnClickListener(this);
        imageViewback.setOnClickListener(this);
    }

    public void InitCompo()
    {
//        mycampaigns=findViewById(R.id.mycampaigns_linear_id);
        imageViewback=findViewById(R.id.imageback);
        tvNoData=findViewById(R.id.tv_no_data);

        rvCompaign = findViewById(R.id.rv_my_compaign);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvCompaign.setLayoutManager(mLayoutManager);
        rvCompaign.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
//            case R.id.mycampaigns_linear_id:
//                Intent intent = new Intent(MyCampaignsActivity.this, ACampaignsDriverActivity.class);
//                startActivity(intent);
//                Animatoo.animateSlideUp(MyCampaignsActivity.this);
//                break;
            case R.id.imageback:
                    finish();
                Animatoo.animateSlideUp(MyCampaignsActivity.this);
                break;
            case R.id.login_button_id:
//                addValidationToViews();
                break;
        }
    }
    public void SetAdapter()
    {
        int size=arCompaign.size();
        Log.e("","size= "+size);
        mAdapter = new MyCampaigListAdapter(this,arCompaign,this);
        rvCompaign.setAdapter(mAdapter);

        if(arCompaign.size()>0)
        {
            tvNoData.setVisibility(View.GONE);
        }
        else
        {
            tvNoData.setVisibility(View.VISIBLE);
        }
    }
//adapter click
    @Override
    public void onClick(int position, int diff) {

        Gson gson = new Gson();
        String jsonObj = gson.toJson(arCompaign.get(position));
        Log.e("","jsonObj= "+jsonObj);
        Intent intent = new Intent(MyCampaignsActivity.this, MyCompaignDetailActivity.class);
//        Intent intent = new Intent(MyCampaignsActivity.this, MyCompaignDetailActivity.class);
        intent.putExtra("object_data",jsonObj);
        startActivity(intent);
        Animatoo.animateSlideUp(MyCampaignsActivity.this);
    }
    ArrayList<CampaignBean> arCompaign=new ArrayList<>();
//campaign api
    @Override
    public void success(ArrayList<CampaignBean> response, String status) {
        arCompaign.clear();
        arCompaign=response;
        SetAdapter();
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

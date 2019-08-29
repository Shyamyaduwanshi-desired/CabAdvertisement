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
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.diss.cabadvertisement.R;
import com.diss.cabadvertisement.ui.adapter.MyCampaigListAdapter;
import com.diss.cabadvertisement.ui.model.CampaignBean;
import com.diss.cabadvertisement.ui.presenter.CampaignPresenter;
import com.diss.cabadvertisement.ui.util.AppData;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ActWeeklyAnalysis extends AppCompatActivity implements View.OnClickListener,MyCampaigListAdapter.CompainDetailClick, CampaignPresenter.CampaingListData{
    AppData appdata;
    ImageView imageViewback;
    TextView tvNoData;
    private RecyclerView rvCompaign;
    private RecyclerView.Adapter mAdapter;
    private CampaignPresenter presenter;
    TextView tvMainTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_compaigns_activity);
        appdata=new AppData(ActWeeklyAnalysis.this);
        presenter = new CampaignPresenter(ActWeeklyAnalysis.this, ActWeeklyAnalysis.this);
        InitCompo();
        Listener();
        GetCompaignData();
    }

    private void GetCompaignData() {
        if(appdata.isNetworkConnected(this)){
            presenter.GetAllCampaingList();
        }else {
            appdata.ShowNewAlert(this,"Please connect to internet");
        }
    }

    private void Listener() {
        imageViewback.setOnClickListener(this);
    }

    public void InitCompo()
    {
        imageViewback=findViewById(R.id.imageback);
        tvNoData=findViewById(R.id.tv_no_data);
        tvMainTitle=findViewById(R.id.logo);

        rvCompaign = findViewById(R.id.rv_my_compaign);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvCompaign.setLayoutManager(mLayoutManager);
        rvCompaign.setItemAnimator(new DefaultItemAnimator());

        tvMainTitle.setText("Weekly Analysis Report");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imageback:
                    finish();
                Animatoo.animateSlideUp(ActWeeklyAnalysis.this);
                break;
            case R.id.login_button_id:
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
        String s_id=arCompaign.get(position).getID();
        Log.e("","s_id= "+s_id);
        Intent intent = new Intent(ActWeeklyAnalysis.this, ActWeeklyAnalysisDetail.class);
        intent.putExtra("s_id_",s_id);
        startActivity(intent);
        Animatoo.animateSlideUp(ActWeeklyAnalysis.this);

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

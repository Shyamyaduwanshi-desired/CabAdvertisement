package com.diss.cabadvertisement.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.diss.cabadvertisement.R;
import com.diss.cabadvertisement.ui.adapter.ReviewAdapter;
import com.diss.cabadvertisement.ui.model.ReviewBean;
import com.diss.cabadvertisement.ui.presenter.ReviewPresenter;
import com.diss.cabadvertisement.ui.util.AppData;


import java.util.ArrayList;

public class RateActivity extends AppCompatActivity implements View.OnClickListener,ReviewAdapter.ReviewClick, ReviewPresenter.ReviewDetails{

    ImageView imageViewback;
    AppData appdata;
    private RecyclerView.Adapter mAdapter;
    RecyclerView rvReview;
    Context context;
    ReviewPresenter reviewPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviews_activity);
        appdata=new AppData(this);
        context=this;
        reviewPresenter = new ReviewPresenter(this, this);
        InitCompo();
        Listener();
//        DummyData();
        GetReview();
    }

    private void GetReview() {
        if(appdata.isNetworkConnected(this)){
            reviewPresenter.GetReview();
        }else {
            appdata.ShowNewAlert(this,"Please connect to internet");
        }
    }

    private void Listener() {
        imageViewback.setOnClickListener(this);

    }
    TextView tvNoData;
    public void InitCompo()
    {
        imageViewback=findViewById(R.id.imageback);
        tvNoData=findViewById(R.id.tv_no_data);

        rvReview = findViewById(R.id.rv_review);
        rvReview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvReview.setHasFixedSize(true);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imageback:
                onBackPressed();
                break;
        }
    }
    ArrayList<ReviewBean> arReview=new ArrayList<>();
  /*  public void DummyData()
    {
        ReviewBean bean;
        for(int i=0;i<3;i++)
        {
            bean=new ReviewBean();
            bean.setReviewId((i+1)+"");
            switch (i)
            {
                case 0://ReportDate,TotalKm,totalTime,locNm
                    bean.setReviewName("Chandrapal Sigh Pawar");
                    bean.setReviewDsc("Lorem Ipsum Doller sit amet Lorem Ipsum Doller sit amet  ");
                    bean.setReviewDate("2019-06-15 11:17:55");
                    bean.setReviewPicURL("");

                    break;
                case 1:
                    bean.setReviewName("Test demo");
                    bean.setReviewDsc("Lorem Ipsum Doller sit amet Lorem Ipsum Doller sit amet  ");
                    bean.setReviewDate("2019-06-25 11:17:55");
                    bean.setReviewPicURL("");

                    break;
                case 2:
                    bean.setReviewName("Harpreet Singh Panjabi");
                    bean.setReviewDsc("Lorem Ipsum Doller sit amet Lorem Ipsum Doller sit amet  ");
                    bean.setReviewDate("2019-06-30 11:17:55");
                    bean.setReviewPicURL("");

                    break;
            }
            arReview.add(bean) ;
        }
        SetAdapter();
    }*/

    public void SetAdapter()
    {
        mAdapter= new ReviewAdapter(context, arReview, this);
        rvReview.setAdapter(mAdapter);
        if(arReview.size()>0)
        {
            tvNoData.setVisibility(View.GONE);
        }
        else {
            tvNoData.setVisibility(View.VISIBLE);
        }
    }
    //adapter click
    @Override
    public void onClick(int position, int diff) {

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
        Animatoo.animateFade(this);
    }

    //for review rate api

    @Override
    public void success(ArrayList<ReviewBean> response, String status) {
        arReview.clear();
        arReview=response;
        SetAdapter();
    }

    @Override
    public void error(String response) {
        appdata.ShowNewAlert(this,response);
        tvNoData.setVisibility(View.VISIBLE);

    }

    @Override
    public void fail(String response) {
        appdata.ShowNewAlert(this,response);
        tvNoData.setVisibility(View.VISIBLE);

    }
}

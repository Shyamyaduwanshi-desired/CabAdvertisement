package com.diss.cabadvertisement.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.diss.cabadvertisement.R;
import com.diss.cabadvertisement.ui.adapter.SubPlanAdapter;
import com.diss.cabadvertisement.ui.model.SubPlanBean;
import com.diss.cabadvertisement.ui.presenter.LoginPresenter;
import com.diss.cabadvertisement.ui.presenter.SubscriptionPlanPresenter;
import com.diss.cabadvertisement.ui.util.AppData;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionPlanActivity extends AppCompatActivity implements View.OnClickListener, SubscriptionPlanPresenter.SubscriptionPlan, SubPlanAdapter.PlanClick{
    private SubscriptionPlanPresenter presenter;
    AppData appdata;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscription_plan_activity);
        appdata=new AppData(SubscriptionPlanActivity.this);
        presenter = new SubscriptionPlanPresenter(SubscriptionPlanActivity.this, SubscriptionPlanActivity.this);
        InitCompo();
        Listener();
        GetPlanData();
    }

    private void GetPlanData() {
        if(appdata.isNetworkConnected(this)){
            presenter.GeSubPlan();
        }else {
            appdata.ShowNewAlert(this,"Please connect to internet");
        }
    }

    private void Listener() {
    }

    public void InitCompo()
    {
        recyclerView = findViewById(R.id.rv_plan_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.signin_text_id:
//                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
//                startActivity(intent);
//                Animatoo.animateSlideUp(LoginActivity.this);
                break;
            case R.id.forgot_text_id:
//                Intent intent1 = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
//                startActivity(intent1);
//                Animatoo.animateFade(LoginActivity.this);
                break;
            case R.id.login_button_id:
//                addValidationToViews();
                break;
        }
    }
    ArrayList<SubPlanBean> arSubPlan=new ArrayList<>();
    @Override
    public void success(ArrayList<SubPlanBean> response, String status_) {
        arSubPlan.clear();
        arSubPlan=response;
        SetAdapter();
    }

    @Override
    public void error(String response) {

    }

    @Override
    public void fail(String response) {

    }

    public void SetAdapter()
    {
        int size=arSubPlan.size();
        Log.e("","size= "+size);
        mAdapter = new SubPlanAdapter(this,arSubPlan,this);
        recyclerView.setAdapter(mAdapter);
    }
//adapter click
    @Override
    public void onClick(int position) {

        Intent intent = new Intent(SubscriptionPlanActivity.this, DetailsActivity.class);
        startActivity(intent);

    }
}

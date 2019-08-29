package com.diss.cabadvertisement.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.diss.cabadvertisement.R;
import com.diss.cabadvertisement.ui.model.CouponBean;
import com.diss.cabadvertisement.ui.presenter.ApplyCouponPresenter;
import com.diss.cabadvertisement.ui.presenter.SubscriptionPlanPresenter;
import com.diss.cabadvertisement.ui.util.AppData;

import java.util.ArrayList;

public class CouponsApplyActivity extends AppCompatActivity implements View.OnClickListener,ApplyCouponPresenter.CouponDetails {

    private ApplyCouponPresenter presenter;
    AppData appdata;
    TextView tvCouponCoce,tvTitle,tvDsc,tvCouponApply;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_apply_activity);
        appdata=new AppData(CouponsApplyActivity.this);
        presenter = new ApplyCouponPresenter(CouponsApplyActivity.this, CouponsApplyActivity.this);
        InitCompo();
        Listener();
        GetCouponsApply();
    }

    private void Listener() {
        tvCouponApply.setOnClickListener(this);
    }

    private void InitCompo() {
        tvCouponCoce=findViewById(R.id.tv_coupon_code);
        tvTitle=findViewById(R.id.tv_title);
        tvDsc=findViewById(R.id.tv_dsc);
        tvCouponApply=findViewById(R.id.tv_coupon_apply);
    }

    private void GetCouponsApply() {
        if(appdata.isNetworkConnected(this)){
            presenter.GetCoupon("CAB1234");
        }else {
            appdata.ShowNewAlert(this,"Please connect to internet");
        }
    }
    ArrayList<CouponBean> arCoupon = new ArrayList<>();
//for apply coupon
    @Override
    public void success(ArrayList<CouponBean> response, String status) {
        arCoupon=response;
        setDataOnView();
    }

    private void setDataOnView() {
        int size=arCoupon.size();
        Log.e("",""+size);
        if(arCoupon.size()>0) {
            tvCouponCoce.setText(arCoupon.get(0).getCoupon_code());
            tvTitle.setText(arCoupon.get(0).getCoupon_title());
            tvDsc.setText(arCoupon.get(0).getCoupon_desc());
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

    @Override
    public void onClick(View v) {
          switch (v.getId())
          {
              case R.id.tv_coupon_apply:
                  Intent returnIntent = new Intent();
                  returnIntent.putExtra("result",arCoupon.get(0).getAmount());//result
                  returnIntent.putExtra("coupon_id",arCoupon.get(0).getID());//result
                  setResult(RESULT_OK,returnIntent);
                  finish();
                  break;
          }
    }
}

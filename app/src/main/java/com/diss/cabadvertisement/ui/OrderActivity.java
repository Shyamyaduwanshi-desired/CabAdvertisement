package com.diss.cabadvertisement.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.diss.cabadvertisement.R;
import com.diss.cabadvertisement.ui.model.ImageBean;
import com.diss.cabadvertisement.ui.model.SubPlanBean;
import com.diss.cabadvertisement.ui.util.AppData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity implements View.OnClickListener{

    TextView applytext;
    Button proceedbutton;
    ImageView imageViewback;
    String ObjectData="";
    SubPlanBean subPlanBean;
    String compaignNm,compaignObj,compaignDsc,sApplyAmount="0",sCouponId="0";
    AppData appdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_summary_activity);
        appdata=new AppData(OrderActivity.this);
        GetIntentData();
        InitCompo();
        Listener();
        PreSetData();
    }
    String allImageNm,allLocationNm;
    private void GetIntentData() {
        ObjectData=appdata.PlanData();
        compaignNm=appdata.CampaingNm();
        compaignObj=appdata.CampaingObj();
        compaignDsc=appdata.CampaingDsc();
        allImageNm=appdata.AllImagesNm();
        allLocationNm=appdata.AllLocationNm();

//        ObjectData = getIntent().getStringExtra("object_data");
//         imagedata = getIntent().getStringExtra("all_image_data");
//         locationdata = getIntent().getStringExtra("all_location_data");
//        compaignNm = getIntent().getStringExtra("compaign_nm");
//        compaignObj = getIntent().getStringExtra("compaign_obj");
//        compaignDsc = getIntent().getStringExtra("compaign_dsc");


//        Gson gson1 = new Gson();
//        ArrayList<ImageBean> arImageList= (ArrayList<ImageBean>)gson1.fromJson(imagedata,
//                new TypeToken<ArrayList<ImageBean>>() {
//                }.getType());
//        Log.e("","image size= "+arImageList.size());

        Gson gson = new Gson();
        subPlanBean = gson.fromJson(ObjectData, SubPlanBean.class);
        Log.e("","ObjectData= "+ObjectData.toString());
    }
    private void PreSetData() {
        tvPlanNm.setText(subPlanBean.getPlan_name());
        tvPlanTime.setText(subPlanBean.getPlan_days());
        tvNoOfCab.setText(subPlanBean.getNumber_of_cabs());
        tvPlanAmount.setText(subPlanBean.getPlan_amount());

        tvCompaignName.setText(compaignNm);
        tvCompaignObject.setText(compaignObj);
        tvCompaignDsc.setText(compaignDsc);
        tvLocationName.setText(allLocationNm);
        tvUploadeFileNm.setText(allImageNm);
        tvTotalAmount.setText(subPlanBean.getPlan_amount());
    }

    private void Listener() {
        applytext.setOnClickListener(this);
        proceedbutton.setOnClickListener(this);

    }
    TextView tvPlanNm,tvPlanTime,tvNoOfCab,tvCompaignName,tvCompaignObject,tvCompaignDsc,tvLocationName,tvUploadeFileNm,tvPlanAmount,tvTotalAmount;
    public void InitCompo()
    {
        applytext=findViewById(R.id.applybutton_id);
        proceedbutton=findViewById(R.id.proceed_button_id);
        tvPlanNm=findViewById(R.id.tv_plan_name);
        tvPlanTime=findViewById(R.id.tv_plan_time);
        tvNoOfCab=findViewById(R.id.tv_no_cab);

        tvCompaignName=findViewById(R.id.tv_compaign_name);
        tvLocationName=findViewById(R.id.tv_location_name);
        tvCompaignObject=findViewById(R.id.tv_compaign_object);
        tvCompaignDsc=findViewById(R.id.tv_compaign_dsc);
        tvUploadeFileNm=findViewById(R.id.tv_file_name);
        tvPlanAmount=findViewById(R.id.tv_plan_amount);
        tvTotalAmount=findViewById(R.id.tv_total_amount);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.applybutton_id:
                Intent intent = new Intent(OrderActivity.this, CouponsApplyActivity.class);
                startActivityForResult(intent, 6);
                break;
            case R.id.proceed_button_id:
                appdata.saveDiscountAmount(sApplyAmount);
                appdata.saveCouponId(sCouponId);
                Intent intent1 = new Intent(OrderActivity.this, PaymentActivity.class);
                startActivity(intent1);
                Animatoo.animateSlideDown(OrderActivity.this);
                break;


        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK){
            sApplyAmount=data.getStringExtra("result");
            sCouponId=data.getStringExtra("coupon_id");
            SetTotal();
        }

    }
    public void SetTotal()
    {
        int planAmount=0,discountAmount=0,finalAmount=0;
        if(!TextUtils.isEmpty(subPlanBean.getPlan_amount()))
        {
            planAmount= Integer.parseInt(subPlanBean.getPlan_amount());
        }
        if(!TextUtils.isEmpty(sApplyAmount))
        {
            discountAmount= Integer.parseInt(sApplyAmount);
        }
        if(planAmount>=discountAmount)
        {
            finalAmount=planAmount-discountAmount;
        }

        tvTotalAmount.setText(String.valueOf(finalAmount));
    }

}

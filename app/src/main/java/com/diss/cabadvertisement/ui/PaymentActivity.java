package com.diss.cabadvertisement.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.diss.cabadvertisement.R;
import com.diss.cabadvertisement.ui.model.AddCompaignDetailBean;
import com.diss.cabadvertisement.ui.model.AddCompaignLocBean;
import com.diss.cabadvertisement.ui.model.ImageBean;
import com.diss.cabadvertisement.ui.model.SubPlanBean;
import com.diss.cabadvertisement.ui.presenter.AdvancedDetailsPresenter;
import com.diss.cabadvertisement.ui.util.AppData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener, AdvancedDetailsPresenter.AdvanceCompaignDetail{

    Button confirmbutton;
    TextView paymentmethodtext;
    LinearLayout methodes;
    int count=0;
    int countBACK=0;
    boolean checkFlag[]= {false, false, false};
    private AdvancedDetailsPresenter presenter;
    AppData appdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_option_activity);
        appdata=new AppData(PaymentActivity.this);
        presenter = new AdvancedDetailsPresenter(PaymentActivity.this, PaymentActivity.this);
        InitCompo();
        Listener();
    }
    String imagedata,locationdata;


//    private void ShowMessage() {
//        final Dialog dialog = new Dialog(PaymentActivity.this);
//        dialog.setContentView(R.layout.thanku_activity);
//        dialog.setTitle("Custom Dialog");
//        TextView text1 = (TextView) dialog.findViewById(R.id.thanku_text_id);
//        text1.setText("Thank you for the subscription.Our respresentive will communicate with you soon for further process.");
//        dialog.show();
//        Button DoneButton = (Button) dialog.findViewById(R.id.thanku_button_id);
//        DoneButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(PaymentActivity.this, DashboardActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
    private void Listener() {
        paymentmethodtext.setOnClickListener(this);
        confirmbutton.setOnClickListener(this);

        tvNetBanking.setOnClickListener(this);
        tvCreditDebit.setOnClickListener(this);
        tvCOD.setOnClickListener(this);

    }
    TextView tvNetBanking,tvCreditDebit,tvCOD;
    public void InitCompo()
    {
        tvNetBanking=findViewById(R.id.tv_internetbanking);
        tvCreditDebit=findViewById(R.id.tv_credit_debit);
        tvCOD=findViewById(R.id.tv_cod);

        paymentmethodtext=(TextView)findViewById(R.id.paymenemethode_id);
        methodes=(LinearLayout)findViewById(R.id.methodes_id);
        confirmbutton=(Button)findViewById(R.id.confirmbooking_button_id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_internetbanking:
                setDefault(2,0);
                break;
            case R.id.tv_credit_debit:
                setDefault(2,1);
                break;
            case R.id.tv_cod:
                setDefault(2,2);
                break;

            case R.id.paymenemethode_id:
                countBACK=1;

                if (count==0){
                    paymentmethodtext.setVisibility(View.VISIBLE);
                    methodes.setVisibility(View.VISIBLE);
                    count=1;
                }else{
                    paymentmethodtext.setVisibility(View.VISIBLE);
                    methodes.setVisibility(View.GONE);
                    count=0;
                    setDefault(1,0);
                }
                break;
            case R.id.confirmbooking_button_id:

                boolean checkflag=false;
                int pos=0;

                for(int i=0;i<checkFlag.length;i++)
                {
                    if(checkFlag[i])
                    {
                        pos=i;
                        checkflag=true;

                    }
                }

                if(checkflag)
                {
                    switch (pos)
                    {
                        case 0:
                            Toast.makeText(this, "Comming soon", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            Toast.makeText(this, "Comming soon", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                                CallConfirm();
                            break;
                    }
                }
                else
                {
                    Toast.makeText(this, "Please select payment method", Toast.LENGTH_SHORT).show();
                }


//                ShowMessage();
                break;


        }
    }
    public void setDefault(int diff,int position)
    {
        for(int i=0;i<checkFlag.length;i++)
        {
            if(diff==1) //for default
            {
                checkFlag[i] = false;
            }
            else {//set a single position true
                if(position==i)
                {
                    checkFlag[i] = true;

                }
                else
                {
                    checkFlag[i] = false;
                }

            }
        }
        SetTextColor();
    }

    public  void SetTextColor()
    {
        for(int i=0;i<checkFlag.length;i++)
        {
            if(checkFlag[i])
            {
                switch (i)//for show selected
                {
                    case 0:
                        tvNetBanking.setTextColor(ContextCompat.getColor(PaymentActivity.this, R.color.lightblue));
                        break;
                    case 1:
                        tvCreditDebit.setTextColor(ContextCompat.getColor(PaymentActivity.this, R.color.lightblue));
                        break;
                    case 2:
                       tvCOD.setTextColor(ContextCompat.getColor(PaymentActivity.this, R.color.lightblue));
                        break;

                }
            }
            else
            {
                switch (i)//for show not selected
                {
                    case 0:
                        tvNetBanking.setTextColor(ContextCompat.getColor(PaymentActivity.this, R.color.black));
                        break;
                    case 1:
                        tvCreditDebit.setTextColor(ContextCompat.getColor(PaymentActivity.this, R.color.black));
                        break;
                    case 2:
                        tvCOD.setTextColor(ContextCompat.getColor(PaymentActivity.this, R.color.black));
                        break;

                }
            }
        }

    }
    String ObjectData="";
    String compaignNm,compaignObj,compaignDsc,sApplyAmount="",sCouponId="0";
    ArrayList<ImageBean> arImageList=new ArrayList<>();
    ArrayList<AddCompaignLocBean> arLocDetail=new ArrayList<>();
    SubPlanBean subPlanBean;
    public void CallConfirm()
    {
        ObjectData=appdata.PlanData();
        compaignNm=appdata.CampaingNm();
        compaignObj=appdata.CampaingObj();
        compaignDsc=appdata.CampaingDsc();
        imagedata=appdata.AllImagesData();
        locationdata=appdata.AllLocationData();
        sApplyAmount=appdata.DiscountAmount();
        sCouponId=appdata.CouponId();
        Gson gson1 = new Gson();
       arImageList= (ArrayList<ImageBean>)gson1.fromJson(imagedata,
                new TypeToken<ArrayList<ImageBean>>() {
                }.getType());

        Gson gson = new Gson();
        arLocDetail= (ArrayList<AddCompaignLocBean>)gson.fromJson(locationdata,
                new TypeToken<ArrayList<AddCompaignLocBean>>() {
                }.getType());
        Log.e("","image size= "+arImageList.size()+" arLocDetail= "+arLocDetail.size());
        Gson gson2 = new Gson();
        subPlanBean = gson2.fromJson(ObjectData, SubPlanBean.class);

        int planAmount=0,discountAmount=0,finalAmount=0;

        if(!TextUtils.isEmpty(subPlanBean.getPlan_amount()))
        {
            planAmount= Integer.parseInt(subPlanBean.getPlan_amount());
        }
        if(TextUtils.isEmpty(sApplyAmount)||sApplyAmount.equals("NA")||sApplyAmount=="NA")
        {
            discountAmount= 0;
        }
        else
        {
            discountAmount= Integer.parseInt(sApplyAmount);
        }
        if(planAmount>=discountAmount)
        {
            finalAmount=planAmount-discountAmount;
        }


        if(appdata.isNetworkConnected(this)){

                    AddCompaignDetailBean compaignBean=new AddCompaignDetailBean();
                    compaignBean.setsCompaignNm(compaignNm);
                    compaignBean.setsCompaignObj(compaignObj);
                    compaignBean.setsCompaignDetails(compaignDsc);

                    JSONArray jsonArray = new JSONArray();
                    for(int i=0;i<arLocDetail.size();i++) {
                        JSONObject locObj = new JSONObject();
                        try {
                            locObj.put("c_l_name", arLocDetail.get(i).getsLocNm());
                            locObj.put("c_l_lat", arLocDetail.get(i).getsLocLat());
                            locObj.put("c_l_lng", arLocDetail.get(i).getsLocLong());

                            jsonArray.put(locObj);

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }


                    JSONArray jsonArrayImage = new JSONArray();
                    for(int i=0;i<arImageList.size();i++) {
                        JSONObject locObj = new JSONObject();
                        try {
                            locObj.put("data", arImageList.get(i).getData());
                            locObj.put("name", arImageList.get(i).getName());

                            jsonArrayImage.put(locObj);

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
//            Log.e("","getsCompaignNm= "+compaignNm+" jsonArray= "+jsonArray.toString()+" getsCompaignObj= "+compaignObj+" getsCompaignDetails= "+compaignDsc+" getUserID= "+appdata.getUserID()+" subPlanBean.getPlanId()= "+subPlanBean.getPlanId()+" arImage.get(0)= "+jsonArrayImage.toString()+" sApplyAmount= "+sApplyAmount+" finalAmount= "+finalAmount+" sCouponId="+sCouponId);

                presenter.SaveAdvanceCompaignDetail(compaignBean,subPlanBean.getPlanId(),jsonArrayImage,jsonArray,sCouponId);
        }else {
            appdata.ShowNewAlert(this,"Please connect to internet");
        }
    }
//advance detail submit
    @Override
    public void success(String response, String status) {
       ShowNewAlert(this,"Thank you for the subscription.Our respresentive will communicate with you soon for further process.");
    }

    @Override
    public void error(String response) {
        appdata.ShowNewAlert(this,response);
    }

    @Override
    public void fail(String response) {
        appdata.ShowNewAlert(this,response);
    }
    PrettyDialog prettyDialog=null;
    public  void ShowNewAlert(Context context, String message) {
        if(prettyDialog!=null)
        {
            prettyDialog.dismiss();
        }
        prettyDialog = new PrettyDialog(context);
        prettyDialog.setCanceledOnTouchOutside(false);
        TextView title = (TextView) prettyDialog.findViewById(libs.mjn.prettydialog.R.id.tv_title);
        TextView tvmessage = (TextView) prettyDialog.findViewById(libs.mjn.prettydialog.R.id.tv_message);
        title.setTextSize(15);
        tvmessage.setTextSize(15);
        prettyDialog.setIconTint(R.color.colorPrimary);
        prettyDialog.setIcon(R.drawable.pdlg_icon_info);
        prettyDialog.setTitle("");
        prettyDialog.setMessage(message);
        prettyDialog.setAnimationEnabled(false);
        prettyDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        prettyDialog.addButton("Ok", R.color.black, R.color.white, new PrettyDialogCallback() {
            @Override
            public void onClick() {
                prettyDialog.dismiss();
                appdata.setUserStatus("3");//for auto login at splash screen
                Intent intent = new Intent(PaymentActivity.this, DashboardActivity.class);
                startActivity(intent);
                finishAffinity();
                Animatoo.animateSlideDown(PaymentActivity.this);
            }
        }).show();

//        prettyDialog.addButton("Search again", R.color.black, R.color.white, new PrettyDialogCallback() {
//            @Override
//            public void onClick() {
//                prettyDialog.dismiss();
//
//            }
//        }).show();
    }
}

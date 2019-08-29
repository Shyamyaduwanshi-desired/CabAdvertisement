package com.diss.cabadvertisement.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.diss.cabadvertisement.R;
import com.diss.cabadvertisement.ui.adapter.NotificationAdapter;
import com.diss.cabadvertisement.ui.model.NotiBean;
import com.diss.cabadvertisement.ui.presenter.NotificationPresenter;
import com.diss.cabadvertisement.ui.util.AppData;


import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener, NotificationAdapter.NotificationClick, NotificationPresenter.NotificationHistory{

    ImageView imageViewback;
    private NotificationPresenter NotiPresenter;
    AppData appdata;
    private RecyclerView.Adapter mAdapter;
    RecyclerView rvNotification;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_activity);
        appdata=new AppData(this);
        context=this;
        NotiPresenter = new NotificationPresenter(context, NotificationActivity.this,"");
        InitCompo();
        Listener();
//        DummyData();
        GetNotiData(1);
    }
String notiId="";
    int post=0;
    private void GetNotiData(int diff) {
        if(appdata.isNetworkConnected(this)){
            switch (diff)
            {
                case 1://get all notification
                    NotiPresenter.GetNotiHistroy();

                    break;
                case 2://delete single notification
                    NotiPresenter.DeleteSingleNoti(notiId);
                    break;
            }

        }else {
            appdata.ShowNewAlert(this,"Please connect to internet");
        }
    }
    public void DeleteNotiDlg(String msg)
    {
        new AlertDialog.Builder(this)
                .setMessage(msg)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GetNotiData(2);
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    private void Listener() {
        imageViewback.setOnClickListener(this);

    }
    public void InitCompo()
    {
        imageViewback=findViewById(R.id.imageback);

        rvNotification = findViewById(R.id.rv_notification);
        rvNotification.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvNotification.setHasFixedSize(true);

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
    ArrayList<NotiBean> arNoti=new ArrayList<>();
//    public void DummyData()
//    {
//        NotiBean bean;
//        for(int i=0;i<3;i++)
//        {
//            bean=new NotiBean();
//            bean.setNotiId((i+1)+"");
//            switch (i)
//            {
//                case 0://ReportDate,TotalKm,totalTime,locNm
//                    bean.setNotiTitle("Notification 1");
//                    bean.setNotiDsc("Lorem Ipsum Doller sit amet Lorem Ipsum Doller sit amet  ");
//                    bean.setNotiTime("5 days");
//                    bean.setImageUrl("");
//
//                    break;
//                case 1:
//                    bean.setNotiTitle("Notification 2");
//                    bean.setNotiDsc("Lorem Ipsum Doller sit amet Lorem Ipsum Doller sit amet ");
//                    bean.setNotiTime("8 days");
//                    bean.setImageUrl("");
//
//                    break;
//                case 2:
//                    bean.setNotiTitle("Notification 3");
//                    bean.setNotiDsc("Lorem Ipsum Doller sit amet Lorem Ipsum Doller sit amet ");
//                    bean.setNotiTime("10 days");
//                    bean.setImageUrl("");
//
//                    break;
//            }
//            arNoti.add(bean) ;
//        }
//        SetAdapter();
//    }

    public void SetAdapter()
    {
        mAdapter= new NotificationAdapter(context, arNoti, NotificationActivity.this);
        rvNotification.setAdapter(mAdapter);
    }
//adapter click
    @Override
    public void onClick(int position, int diff) {
        post=position;
          switch (diff)
          {
              case 1://delete single notification
                  notiId=arNoti.get(position).getNotiId();

                  DeleteNotiDlg("Are you sure you want to delete?");
                  break;
             case 2://view single notification

                  break;
          }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
        Animatoo.animateFade(this);
    }
//all notification list api
    @Override
    public void success(ArrayList<NotiBean> response, String msg, String diff) {
        switch (diff)
        {
            case "1"://for noti list
                arNoti.clear();
                arNoti=response;
                SetAdapter();
                break;
            case "2"://for delete notification
                arNoti.remove(post);
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void error(String response, String diff) {
        appdata.ShowNewAlert(this,response);
    }

    @Override
    public void fail(String response, String diff) {
        appdata.ShowNewAlert(this,response);
    }
}

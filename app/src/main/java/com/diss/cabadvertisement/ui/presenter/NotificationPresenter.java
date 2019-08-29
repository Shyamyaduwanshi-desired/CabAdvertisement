package com.diss.cabadvertisement.ui.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.diss.cabadvertisement.ui.model.NotiBean;
import com.diss.cabadvertisement.ui.util.AppData;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class NotificationPresenter {
    private Context context;
    private NotificationHistory notiHistory;
    private AppData appData;

    public NotificationPresenter(Context context, NotificationHistory notiHistory, String diff) {
        this.context = context;
        this.notiHistory = notiHistory;
        appData = new AppData(context);
    }

    public interface NotificationHistory{
        void success(ArrayList<NotiBean> response, String mes, String diff);
        void error(String response, String diff);
        void fail(String response, String diff);
    }
    public void GetNotiHistroy() {
        final ArrayList<NotiBean> NotiList = new ArrayList<>();
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();
        AndroidNetworking.post(AppData.url)
                .addBodyParameter("action", "notification_list")
                .addBodyParameter("user_id", appData.getUserID())

                .addHeaders("Username","admin")
                .addHeaders("Password","admin123")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject reader) {
                        // do anything with response
                        if(progress!=null)
                            progress.dismiss();
                        NotiList.clear();
                        try {
                            String status = reader.getString("code");
                            String msg = reader.getString("message");
                            Log.e("","notification histroy output json= "+reader.toString());
                            if(status.equals("200")){
                                NotiBean bean;
                                try {
                                    JSONArray jsArrayModel = reader.getJSONArray("body");
                                    if (jsArrayModel != null&&jsArrayModel.length()>0) {
                                        for (int count = 0; count < jsArrayModel.length(); count++) {
                                            JSONObject object = jsArrayModel.getJSONObject(count);
                                            bean = new NotiBean();

                                            String id = object.getString("id");
                                            String status1 = object.getString("status");
                                            String message = object.getString("message");
                                            String type = object.getString("type");


                                            bean.setNotiId(id);
                                            bean.setNotiDsc(message);
                                            bean.setNotiTime("8 days");
                                            bean.setImageUrl("");

                                            bean.setNotiTitle("Notification 2");

                                            NotiList.add(bean);
                                        }
                                    }
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }

                                notiHistory.success(NotiList,"","1");

                            }
                            else {
                                notiHistory.error(msg,"1");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            notiHistory.fail("Something went wrong. Please try after some time.","1");
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        if(progress!=null)
                            progress.dismiss();
                        notiHistory.fail("Something went wrong. Please try after some time.","1");
                    }
                });
    }


    public void DeleteSingleNoti(final  String notiId) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();
        AndroidNetworking.post(AppData.url)
                .addBodyParameter("action", "delete_notification")
                .addBodyParameter("noti_id", notiId)
                .addHeaders("Username","admin")
                .addHeaders("Password","admin123")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject reader) {
                        // do anything with response
                        if(progress!=null)
                            progress.dismiss();
                        try {
                            String status = reader.getString("code");
                            String msg = reader.getString("message");
                            Log.e("","notification histroy output json= "+reader.toString());
                            if(status.equals("200")){
                                notiHistory.success(null,msg,"2");
                            }
                            else {
                                notiHistory.error(msg,"2");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            notiHistory.fail("Something went wrong. Please try after some time.","1");
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        if(progress!=null)
                            progress.dismiss();
                        notiHistory.fail("Something went wrong. Please try after some time.","1");
                    }
                });
    }

}

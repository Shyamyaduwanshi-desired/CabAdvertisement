package com.diss.cabadvertisement.ui.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.diss.cabadvertisement.ui.model.CampaignBean;
import com.diss.cabadvertisement.ui.util.AppData;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CampaignPresenter {
    private Context context;
    private CampaingListData campaingData;
    private AppData appData;

    public CampaignPresenter(Context context, CampaingListData campaingData) {
        this.context = context;
        this.campaingData = campaingData;
        appData = new AppData(context);
    }

    public interface CampaingListData{
        void success(ArrayList<CampaignBean> response, String status);
        void error(String response);
        void fail(String response);
    }

   public void GetCampaingList() {
       final ArrayList<CampaignBean> list = new ArrayList<>();
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();
        AndroidNetworking.post(AppData.url)
                .addBodyParameter("action", "get_campaigns_details")
                .addBodyParameter("user_id", appData.getUserID())
                .addHeaders("Username","admin")
                .addHeaders("Password","admin123")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject reader) {
                        // do anything with response
                        if(progress!=null) {
                            progress.dismiss();
                        }
                        list.clear();
                        try {
                            String status = reader.getString("code");
                            String msg = reader.getString("message");
                            Log.e("","plan output json= "+reader.toString());
                            if(status.equals("200")){

                                JSONArray jsArrayPlan = reader.getJSONArray("body");
                                CampaignBean bean;
                                for (int count = 0; count < jsArrayPlan.length(); count++) {
                                    JSONObject object = jsArrayPlan.getJSONObject(count);
                                    bean=new CampaignBean();
                                    String ID = object.getString("ID");

                                    String c_name = object.getString("c_name");
                                    String c_objective = object.getString("c_objective");
                                    String c_details = object.getString("c_details");
                                    String coupon_id = object.getString("coupon_id");
                                    String added_on = object.getString("added_on");

                                    String plan_id = object.getString("plan_id");
                                    String plan_name = object.getString("plan_name");
                                    String plan_days = object.getString("plan_month");
                                    String number_of_cabs = object.getString("number_of_cabs");
                                    String plan_amount = object.getString("plan_amount");
//                                    String pstatus = object.getString("pstatus");
                                    String lastdate = object.getString("lastdate");
                                    String verifystatus = object.getString("status");

                                    bean.setID(ID);
                                    bean.setC_name(c_name);
                                    bean.setC_objective(c_objective);
                                    bean.setC_details(c_details);
                                    bean.setCoupon_id(coupon_id);

                                    bean.setPlan_id(plan_id);
                                    bean.setPlan_name(plan_name);
                                    bean.setPlan_month(plan_days);
                                    bean.setNumber_of_cabs(number_of_cabs);
                                    bean.setPlan_amount(plan_amount);
                                    bean.setLastdate(lastdate);
                                    bean.setAdded_on(added_on);
                                    bean.setVerifydataStatus(verifystatus);

                                    list.add(bean);
                                }

                                campaingData.success(list,"");
                            }else {
                                campaingData.error(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            campaingData.fail("Something went wrong. Please try after some time.");
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        if(progress!=null)
                        progress.dismiss();
                        campaingData.fail("Something went wrong. Please try after some time.");
                    }
                });
    }//current campaign

    public void GetAllCampaingList() //all campaign old and current
    {
       final ArrayList<CampaignBean> list = new ArrayList<>();
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();
        AndroidNetworking.post(AppData.url)
                .addBodyParameter("action", "get_all_campaigns_of_company")
                .addBodyParameter("user_id", appData.getUserID())
                .addHeaders("Username","admin")
                .addHeaders("Password","admin123")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject reader) {
                        // do anything with response
                        if(progress!=null) {
                            progress.dismiss();
                        }
                        list.clear();
                        try {
                            String status = reader.getString("code");
                            String msg = reader.getString("message");
                            Log.e("","plan output json= "+reader.toString());
                            if(status.equals("200")){

                                JSONArray jsArrayPlan = reader.getJSONArray("body");
                                CampaignBean bean;
                                for (int count = 0; count < jsArrayPlan.length(); count++) {
                                    JSONObject object = jsArrayPlan.getJSONObject(count);
                                    bean=new CampaignBean();
                                    String ID = object.getString("s_id");

                                    String c_name = object.getString("c_name");
                                    String c_objective = object.getString("c_objective");
                                    String c_details = object.getString("c_details");
                                    String coupon_id = object.getString("coupon_id");
                                    String added_on = object.getString("added_on");

                                    String plan_id = object.getString("plan_id");
                                    String plan_name = object.getString("plan_name");
                                    String plan_days = object.getString("plan_month");
                                    String number_of_cabs = object.getString("number_of_cabs");
                                    String plan_amount = object.getString("plan_amount");
//                                    String pstatus = object.getString("pstatus");
                                    String lastdate = object.getString("lastdate");
                                    String verifystatus = object.getString("status");

                                    bean.setID(ID);
                                    bean.setC_name(c_name);
                                    bean.setC_objective(c_objective);
                                    bean.setC_details(c_details);
                                    bean.setCoupon_id(coupon_id);

                                    bean.setPlan_id(plan_id);
                                    bean.setPlan_name(plan_name);
                                    bean.setPlan_month(plan_days);
                                    bean.setNumber_of_cabs(number_of_cabs);
                                    bean.setPlan_amount(plan_amount);
                                    bean.setLastdate(lastdate);
                                    bean.setAdded_on(added_on);
                                    bean.setVerifydataStatus(verifystatus);

                                    list.add(bean);
                                }

                                campaingData.success(list,"");
                            }else {
                                campaingData.error(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            campaingData.fail("Something went wrong. Please try after some time.");
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        if(progress!=null)
                        progress.dismiss();
                        campaingData.fail("Something went wrong. Please try after some time.");
                    }
                });
    }

 public void GetSingleCampaignData(final String campaignId) {
        final ArrayList<CampaignBean> singleCampaign = new ArrayList<>();
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();
        AndroidNetworking.post(AppData.url)
                .addBodyParameter("action", "campaign_data")
                .addBodyParameter("user_id", appData.getUserID())
                .addBodyParameter("s_id", campaignId)
                .addHeaders("Username","admin")
                .addHeaders("Password","admin123")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject reader) {
                        if(progress!=null) {
                            progress.dismiss();
                        }
                        singleCampaign.clear();
                        try {
                            String status = reader.getString("code");
                            String msg = reader.getString("message");
                            Log.e("","single campaign output json= "+reader.toString());
                            if(status.equals("200")){

//                                JSONArray jsArrayPlan = reader.getJSONArray("body");
                                CampaignBean bean;
//                                for (int count = 0; count < jsArrayPlan.length(); count++)
                                {
                                    JSONObject object =  reader.getJSONObject("body");
                                    bean=new CampaignBean();
                                    String ID = object.getString("ID");
                                    String plan_id = object.getString("plan_id");
                                    String c_name = object.getString("c_name");
                                    String c_objective = object.getString("c_objective");
                                    String c_details = object.getString("c_details");
                                    String coupon_id = object.getString("coupon_id");
                                    String added_on = object.getString("added_on");


                                    String plan_name = object.getString("plan_name");
                                    String plan_days = object.getString("plan_month");
                                    String number_of_cabs = object.getString("number_of_cabs");
                                    String plan_amount = object.getString("plan_amount");
//                                    String pstatus = object.getString("pstatus");
                                    String lastdate = object.getString("lastdate");

                                    String verifystatus = object.getString("status");

                                    bean.setID(ID);
                                    bean.setC_name(c_name);
                                    bean.setC_objective(c_objective);
                                    bean.setC_details(c_details);
                                    bean.setCoupon_id(coupon_id);

                                    bean.setPlan_id(plan_id);
                                    bean.setPlan_name(plan_name);
                                    bean.setPlan_month(plan_days);
                                    bean.setNumber_of_cabs(number_of_cabs);
                                    bean.setPlan_amount(plan_amount);
                                    bean.setLastdate(lastdate);
                                    bean.setAdded_on(added_on);
                                    bean.setVerifydataStatus(verifystatus);
                                    singleCampaign.add(bean);
                                }

                                campaingData.success(singleCampaign,"");
                            }else {
                                campaingData.error(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            campaingData.fail("Something went wrong. Please try after some time.");
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        if(progress!=null)
                            progress.dismiss();
                        campaingData.fail("Something went wrong. Please try after some time.");
                    }
                });
    }
}

package com.diss.cabadvertisement.ui.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.diss.cabadvertisement.ui.model.DriverAccordingtoCampaingBean;
import com.diss.cabadvertisement.ui.model.DriverCampaignBean;
import com.diss.cabadvertisement.ui.util.AppData;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CampaignDetailsPresenter {
    private Context context;
    private CampaingDriverListData campaingData;
    private AppData appData;

    public CampaignDetailsPresenter(Context context, CampaingDriverListData campaingData) {
        this.context = context;
        this.campaingData = campaingData;
        appData = new AppData(context);
    }

    public interface CampaingDriverListData{
        void success(ArrayList<DriverCampaignBean> respons, String status, String locationsCovered,String total_kms,String total_hrs);
        void error(String response);
        void fail(String response);
    }

 public void GetAllDriverAccordingToCampaign(final  String campaignId) {
       final ArrayList<DriverCampaignBean> list = new ArrayList<>();
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();
        AndroidNetworking.post(AppData.url)
                .addBodyParameter("action", "get_campaigns_alldrives")//get_capaigns_alldrives
                .addBodyParameter("sub_id", campaignId)
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
                                DriverCampaignBean bean;
                                for (int count = 0; count < jsArrayPlan.length(); count++) {
                                    JSONObject object = jsArrayPlan.getJSONObject(count);
                                    bean=new DriverCampaignBean();
                                    String ID = object.getString("ID");

                                    String driver_id = object.getString("driver_id");
                                    String s_id = object.getString("s_id");
//                                    String user_email = object.getString("user_email");
                                    String c_name = object.getString("c_l_name");
                                    String c_l_lat = object.getString("c_l_lat");
                                    String c_l_lng = object.getString("c_l_lng");
                                    String status1 = object.getString("status");//for package amount

                                    JSONArray jsImageArray=null;
                                    int pos=Integer.parseInt(status1);
                                    if(pos>=6&&pos<=12)
                                    {
                                        jsImageArray = object.getJSONArray("stickers");
                                    }
                                    bean.setCompaignId(s_id);
                                    bean.setId(ID);
                                    bean.setDriver_id(driver_id);
                                    bean.setC_l_name(c_name);
                                    bean.setC_l_lat(c_l_lat);
                                    bean.setC_l_lng(c_l_lng);
                                    bean.setPackageAmountStatus(status1);
                                   if(pos>=6&&pos<=12)
                                    {
                                        bean.setImage(jsImageArray.toString());
                                    }
                                    else
                                    {
                                        bean.setImage("");
                                    }


                                    list.add(bean);
                                }

                                campaingData.success(list,"1","","","");
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

 public void GetAllDriverAllDataAcrdToCampaignId(final  String campaignId,final  String type) {
       final ArrayList<DriverCampaignBean> list = new ArrayList<>();
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();
        AndroidNetworking.post(AppData.url)
                .addBodyParameter("action", "analysis_report_company")//for show all information
                .addBodyParameter("s_id", campaignId)
                .addBodyParameter("user_id", appData.getUserID())
                .addBodyParameter("date", appData.GetCurrentDate())
                .addBodyParameter("type", type)
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
                            Log.e("","analysis json= "+reader.toString());
                            if(status.equals("200")){

                                JSONObject jsObjMain = reader.getJSONObject("body");

                                String locationsCovered = jsObjMain.getString("locationsCovered");
                                String total_kms = jsObjMain.getString("total_kms");
                                String total_hrs = jsObjMain.getString("total_hrs");

                                if(locationsCovered.equals(null)||locationsCovered.equals("null"))
                                {
                                    locationsCovered="";
                                }

                                if(total_kms.equals(null)||total_kms.equals("null"))
                                {
                                    total_kms="";
                                }

                                 if(total_hrs.equals(null)||total_hrs.equals("null"))
                                {
                                    total_hrs="";
                                }


                                JSONArray jsArrayPlan = jsObjMain.getJSONArray("campaignList");
                                DriverCampaignBean bean;
                                for (int count = 0; count < jsArrayPlan.length(); count++) {
                                    JSONObject object = jsArrayPlan.getJSONObject(count);
                                    bean=new DriverCampaignBean();
                                    String driver_id = object.getString("driver_id");
                                    String s_id = object.getString("s_id");
                                    String c_name = object.getString("c_l_name");
                                    String c_l_lat = object.getString("c_l_lat");
                                    String c_l_lng = object.getString("c_l_lng");

                                    String total_km = object.getString("total_km");
                                    String total_hours = object.getString("total_hours");

                                     if(TextUtils.isEmpty(total_km)||total_km.equals(null)||total_km.equals("null"))
                                     {
                                         total_km="0";
                                     }
                                    if(TextUtils.isEmpty(total_hours)||total_hours.equals(null)||total_hours.equals("null"))
                                    {
                                        total_hours="0";
                                    }
                                    bean.setCompaignId(s_id);
                                    bean.setDriver_id(driver_id);
                                    bean.setC_l_name(c_name);
                                    bean.setC_l_lat(c_l_lat);
                                    bean.setC_l_lng(c_l_lng);
                                    bean.setTotal_km(total_km);
                                    bean.setTotal_hours(total_hours);
                                    bean.setImage("");

                                    list.add(bean);
                                }

                                campaingData.success(list,"2",locationsCovered,total_kms,total_hrs);
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

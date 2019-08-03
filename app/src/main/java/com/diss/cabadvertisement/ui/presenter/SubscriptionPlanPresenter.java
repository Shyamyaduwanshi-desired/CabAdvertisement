package com.diss.cabadvertisement.ui.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.diss.cabadvertisement.ui.model.SignupBean;
import com.diss.cabadvertisement.ui.model.SubPlanBean;
import com.diss.cabadvertisement.ui.util.AppData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SubscriptionPlanPresenter {
    private Context context;
    private SubscriptionPlan subPlan;
    private AppData appData;

    public SubscriptionPlanPresenter(Context context, SubscriptionPlan subPlan) {
        this.context = context;
        this.subPlan = subPlan;
        appData = new AppData(context);
    }

    public interface SubscriptionPlan{
        void success(ArrayList<SubPlanBean> response, String status);
        void error(String response);
        void fail(String response);
    }

   public void GeSubPlan() {
       final ArrayList<SubPlanBean> list = new ArrayList<>();
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();
        AndroidNetworking.post(AppData.url)
                .addBodyParameter("action", "plan_list")
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
                                SubPlanBean bean;
                                for (int count = 0; count < jsArrayPlan.length(); count++) {
                                    JSONObject object = jsArrayPlan.getJSONObject(count);
                                    bean=new SubPlanBean();
                                    String plan_id = object.getString("plan_id");
                                    String plan_name = object.getString("plan_name");
                                    String plan_days = object.getString("plan_month");
                                    String number_of_cabs = object.getString("number_of_cabs");
                                    String plan_amount = object.getString("plan_amount");

                                    bean.setPlanId(plan_id);
                                    bean.setPlan_name(plan_name);
                                    bean.setPlan_days(plan_days);
                                    bean.setNumber_of_cabs(number_of_cabs);
                                    bean.setPlan_amount(plan_amount);
                                    list.add(bean);
                                }

                                subPlan.success(list,"");
                            }else {
                                subPlan.error(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            subPlan.fail("Something went wrong. Please try after some time.");
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        if(progress!=null)
                        progress.dismiss();
                        subPlan.fail("Something went wrong. Please try after some time.");
                    }
                });
    }
}

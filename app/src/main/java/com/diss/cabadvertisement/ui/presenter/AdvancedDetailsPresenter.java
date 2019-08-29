package com.diss.cabadvertisement.ui.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.diss.cabadvertisement.ui.model.AddCompaignDetailBean;
import com.diss.cabadvertisement.ui.model.AddCompaignLocBean;
import com.diss.cabadvertisement.ui.model.SignupBean;
import com.diss.cabadvertisement.ui.util.AppData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AdvancedDetailsPresenter {
    private Context context;
    private AdvanceCompaignDetail applyCoupon;
    private AppData appData;

    public AdvancedDetailsPresenter(Context context, AdvanceCompaignDetail applyCoupon) {
        this.context = context;
        this.applyCoupon = applyCoupon;
        appData = new AppData(context);
    }

    public interface AdvanceCompaignDetail{
        void success(String response, String status);
        void error(String response);
        void fail(String response);
    }

    public void SaveAdvanceCompaignDetail(AddCompaignDetailBean bean, String planId,JSONArray jsonArrayImage /*ArrayList<String> arImage*/, JSONArray jsonArray,String couponId) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();
        Log.e("","getsCompaignNm= "+bean.getsCompaignNm()+" jsonArray= "+String.valueOf(jsonArray)+" getsCompaignObj= "+bean.getsCompaignObj()+" getsCompaignDetails= "+bean.getsCompaignDetails()+" getUserID= "+appData.getUserID()+""+planId+" arImage.get(0)= "+String.valueOf(jsonArrayImage)/*arImage.get(0)*/);
       AndroidNetworking.post(AppData.url)
                .addBodyParameter("action", "advanced_details_save")
                .addBodyParameter("c_name", bean.getsCompaignNm())
                .addBodyParameter("locations", String.valueOf(jsonArray))// String.valueOf(jsonArray)bean.getsAreaNm()
                .addBodyParameter("c_objective", bean.getsCompaignObj())
                .addBodyParameter("c_details", bean.getsCompaignDetails())
                .addBodyParameter("user_id",appData.getUserID())
                .addBodyParameter("plan_id", planId)
                .addBodyParameter("payment_method", "COD")
                .addBodyParameter("medias", String.valueOf(jsonArrayImage))
//                .addBodyParameter("medias[]", arImage.get(0))
                .addBodyParameter("coupon_id", couponId)//default=0
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
                            Log.e("","add advanced output json= "+reader.toString());
                            if(status.equals("200")){

//                                appData.setForGotUserId(reader.getJSONObject("body").getString("ID"));

                                applyCoupon.success(msg,reader.getJSONObject("body").getString("s_id"));
                            }else {
                                applyCoupon.error(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            applyCoupon.fail("Something went wrong. Please try after some time33.");
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        if(progress!=null)
                        progress.dismiss();
                        applyCoupon.fail("Something went wrong. Please try after some time.");
                    }
                });
    }
}

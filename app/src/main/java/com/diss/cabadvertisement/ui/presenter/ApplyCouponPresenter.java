package com.diss.cabadvertisement.ui.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.diss.cabadvertisement.ui.model.CouponBean;
import com.diss.cabadvertisement.ui.model.SignupBean;
import com.diss.cabadvertisement.ui.model.SubPlanBean;
import com.diss.cabadvertisement.ui.util.AppData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ApplyCouponPresenter {
    private Context context;
    private CouponDetails applyCoupon;
    private AppData appData;

    public ApplyCouponPresenter(Context context, CouponDetails applyCoupon) {
        this.context = context;
        this.applyCoupon = applyCoupon;
        appData = new AppData(context);
    }

    public interface CouponDetails{
        void success(ArrayList<CouponBean> response, String status);
        void error(String response);
        void fail(String response);
    }

    public void GetCoupon(String couponcode) {
        final ArrayList<CouponBean> list = new ArrayList<>();
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();
        AndroidNetworking.post(AppData.url)
                .addBodyParameter("action", "apply_coupon")
                .addBodyParameter("coupon_code", "CAB1234")//"CAB1234"
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
                        list.clear();
                        try {
                            String status = reader.getString("code");
                            String msg = reader.getString("message");
                            Log.e("","coupon output json= "+reader.toString());
                            if(status.equals("200")){
//                                JSONArray jsArrayPlan = reader.getJSONArray("body");
                                CouponBean bean;
//                                for (int count = 0; count < jsArrayPlan.length(); count++)
//                                {
//                                    JSONObject object = jsArrayPlan.getJSONObject(count);


                                try {
                                    JSONObject object = reader.getJSONObject("body");
                                    if(object!=null) {
                                        bean = new CouponBean();
                                        String ID = object.getString("ID");
                                        String coupon_code = object.getString("coupon_code");
                                        String can_use = object.getString("can_use");
                                        String used_times = object.getString("used_times");
                                        String expire_on = object.getString("expire_on");
                                        String discount_type = object.getString("discount_type");
                                        String amount = object.getString("amount");
                                        String added_on = object.getString("added_on");
                                        String status1 = object.getString("status");

                                        String coupon_title = object.getString("coupon_title");
                                        String coupon_desc = object.getString("coupon_desc");

                                        bean.setID(ID);
                                        bean.setCoupon_code(coupon_code);
                                        bean.setCan_use(can_use);
                                        bean.setUsed_times(used_times);
                                        bean.setExpire_on(expire_on);
                                        bean.setDiscount_type(discount_type);
                                        bean.setAmount(amount);
                                        bean.setAdded_on(added_on);
                                        bean.setStatus(status1);
                                        bean.setCoupon_title(coupon_title);
                                        bean.setCoupon_desc(coupon_desc);

                                        list.add(bean);
                                    }
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }

                                applyCoupon.success(list,"");

                            }
                            else {
                                applyCoupon.error(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            applyCoupon.fail("Something went wrong. Please try after some time.");
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

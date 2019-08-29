package com.diss.cabadvertisement.ui.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.diss.cabadvertisement.ui.Approval_activity;
import com.diss.cabadvertisement.ui.DashboardActivity;
import com.diss.cabadvertisement.ui.LoginActivity;
import com.diss.cabadvertisement.ui.OtpActivity;
import com.diss.cabadvertisement.ui.SubscriptionPlanActivity;
import com.diss.cabadvertisement.ui.util.AppData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;




public class LoginPresenter {
    private Context context;
    private Login login;
    private AppData appData;

    public LoginPresenter(Context context, Login login) {
        this.context = context;
        this.login = login;
        appData = new AppData(context);
        AndroidNetworking.initialize(context);
    }

    public interface Login{
        void success(String response, String diff_, String otp);
        void error(String response);
        void fail(String response);
    }

   public void LoginUser(final String phone, final String password, final String deviceToken) {//, final String deviceToken
       final ProgressDialog progress = new ProgressDialog(context);
       progress.setMessage("Please Wait..");
       progress.setCancelable(false);
       progress.show();
       AndroidNetworking.post(AppData.url)
               .addBodyParameter("action", "login")
               .addBodyParameter("mobile_no", phone)
               .addBodyParameter("password", password)
               .addBodyParameter("device_token", deviceToken)
               .addBodyParameter("device_type", "android")
               .addHeaders("Username","admin")
               .addHeaders("Password","admin123")
               .setPriority(Priority.MEDIUM)
               .build()
               .getAsJSONObject(new JSONObjectRequestListener() {
                   @Override
                   public void onResponse(JSONObject reader) {
                       // do anything with response
                       progress.dismiss();
                       try {
                           String status = reader.getString("code");
                           String msg = reader.getString("message");
                           Log.e("","login output json= "+reader.toString());
                           if(status.equals("200")){
                               appData.setUserID(reader.getJSONObject("body").getString("ID"));
                               appData.setUsername(reader.getJSONObject("body").getString("display_name"));
                               appData.setEmail(reader.getJSONObject("body").getString("user_email"));
                               appData.setUserStatus(reader.getJSONObject("body").getString("user_status"));
                               appData.setMobile(reader.getJSONObject("body").getString("user_phone"));
                               appData.setProfilePic(reader.getJSONObject("body").getString("user_pic_full"));

                               appData.setCompanyNm(reader.getJSONObject("body").getString("company_name"));
                               appData.setCompanyOfficeAddress(reader.getJSONObject("body").getString("company_office_address"));
                               appData.setCompanyContactNo(reader.getJSONObject("body").getString("company_contact_no"));
                               appData.setCompanyAreaOfBusiness(reader.getJSONObject("body").getString("company_area_of_business"));

                               appData.setCurrentCampaignId("3");//dummy data

                               String userStatus=reader.getJSONObject("body").getString("user_status");
                                 String otp="";

                               switch (userStatus)
                               {
                                   case "0"://not verify otp
                                       appData.setForGotUserId(reader.getJSONObject("body").getString("ID"));
                                       otp=reader.getJSONObject("body").getString("otp");
                                       break;
                                   case "1"://verified otp but not approve by admin

                                       break;
                                   case "2":// approved by admin

                                       break;
                                   case "3":// not subscription done by user

                                       break;
                                   case "4"://subscription done by user

                                       break;
                               }
                               login.success(msg,userStatus,otp);
                           }else {
                               login.error(msg);
                           }
                       } catch (JSONException e) {
                           e.printStackTrace();
                           login.fail("Something went wrong. Please try after some time.");
                       }
                   }
                   @Override
                   public void onError(ANError error) {
                       // handle error
                       progress.dismiss();
                       login.fail("Something went wrong. Please try after some time.");
                   }
               });
   }

    public void ForgotPass(final String phone) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();
        AndroidNetworking.post(AppData.url)
                .addBodyParameter("action", "forgot_password")
                .addBodyParameter("mobile_no", phone)
                .addHeaders("Username","admin")
                .addHeaders("Password","admin123")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject reader) {
                        // do anything with response
                        if (progress!=null)
                        progress.dismiss();
                        try {
                            String status = reader.getString("code");
                            String msg = reader.getString("message");
                            Log.e("","Forgot output json= "+reader.toString());
                            if(status.equals("200")){
                                appData.setForGotUserId(reader.getJSONObject("body").getString("ID"));
                                login.success(msg,reader.getJSONObject("body").getString("ID"),reader.getJSONObject("body").getString("otp"));
                            }else {
                                login.error(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            login.fail("Something went wrong. Please try after some time.");
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                       if (progress!=null)
                        progress.dismiss();
                        login.fail("Something went wrong. Please try after some time.");
                    }
                });
    }

    public void ResetPass(final String newPass,final String confirmPass) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();
        AndroidNetworking.post(AppData.url)
                .addBodyParameter("action", "reset_password")
                .addBodyParameter("new_pass", newPass)
                .addBodyParameter("confirm_new_pass", confirmPass)
                .addBodyParameter("user_id", appData.getForGotUserId())
                .addHeaders("Username","admin")
                .addHeaders("Password","admin123")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject reader) {
                        // do anything with response
                        if (progress!=null)
                        progress.dismiss();
                        try {
                            String status = reader.getString("code");
                            String msg = reader.getString("message");
                            Log.e("","Reset output json= "+reader.toString());
                            if(status.equals("200")){
                                appData.setForGotUserId("NA");
                                login.success(msg,"","");
                            }else {
                                login.error(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            login.fail("Something went wrong. Please try after some time.");
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                       if (progress!=null)
                        progress.dismiss();
                        login.fail("Something went wrong. Please try after some time.");
                    }
                });
    }

    public void ResendOTP(final String phone) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();
        AndroidNetworking.post(AppData.url)
                .addBodyParameter("action", "resend_otp")
                .addBodyParameter("mobile_no", phone)
                .addHeaders("Username","admin")
                .addHeaders("Password","admin123")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject reader) {
                        // do anything with response
                        if (progress!=null)
                            progress.dismiss();
                        try {
                            String status = reader.getString("code");
                            String msg = reader.getString("message");
                            Log.e("","resend OTP output json= "+reader.toString());
                            if(status.equals("200")){
                                login.success(msg,"",reader.getJSONObject("body").getString("otp"));
                            }else {
                                login.error(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            login.fail("Something went wrong. Please try after some time.");
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        if (progress!=null)
                            progress.dismiss();
                        login.fail("Something went wrong. Please try after some time.");
                    }
                });
    }

    public void VerifyOTP(final String userid, final String deviceToken) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();
        AndroidNetworking.post(AppData.url)
                .addBodyParameter("action", "otp_verify")
                .addBodyParameter("user_id", userid)

                .addBodyParameter("device_token", deviceToken)
                .addBodyParameter("device_type", "android")

                .addHeaders("Username","admin")
                .addHeaders("Password","admin123")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject reader) {
                        // do anything with response
                        if (progress!=null)
                            progress.dismiss();
                        try {
                            String status = reader.getString("code");
                            String msg = reader.getString("message");
                            Log.e("","vefify OTP output json= "+reader.toString());
                            if(status.equals("200")){
                                appData.setUserStatus(reader.getJSONObject("body").getString("user_status"));
                                login.success(msg,"1","");//verified
                            }else {
                                login.error(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            login.fail("Something went wrong. Please try after some time.");
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        if (progress!=null)
                            progress.dismiss();
                        login.fail("Something went wrong. Please try after some time.");
                    }
                });
    }



/*public void LoginVendor(final String phone, final String password) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Login Please Wait..");
        progress.setCancelable(false);
        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    String status = reader.getString("code");
                    String msg = reader.getString("message");
                    Log.e("","login output json= "+reader.toString());
                    if(status.equals("200")){
                        appData.setUserID(reader.getJSONObject("body").getString("ID"));
                        appData.setUsername(reader.getJSONObject("body").getString("display_name"));
                        appData.setEmail(reader.getJSONObject("body").getString("user_email"));

                        login.success(msg,"");
                    }else {
                       login.error(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    login.fail("Something went wrong. Please try after some time.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                login.fail("Server Error.\n Please try after some time.");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "login");
                params.put("mobile_no", "+919893160596");
                params.put("password", "admin123");
//                params.put("username", phone);
//                params.put("password", password);
                Log.e("","Input param= "+params.toString());
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Username","admin");
                params.put("Password","admin123");

                Log.e("","header param= "+params.toString());
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }*/

}

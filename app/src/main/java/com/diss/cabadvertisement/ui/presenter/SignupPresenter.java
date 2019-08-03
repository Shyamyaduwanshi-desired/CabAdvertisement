package com.diss.cabadvertisement.ui.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

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
import com.diss.cabadvertisement.ui.model.SignupBean;
import com.diss.cabadvertisement.ui.util.AppData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;




public class SignupPresenter {
    private Context context;
    private SignUp signup;
    private AppData appData;

    public SignupPresenter(Context context, SignUp signup) {
        this.context = context;
        this.signup = signup;
        appData = new AppData(context);
    }

    public interface SignUp{
        void success(String response,String otp);
        void error(String response);
        void fail(String response);
    }

    public void SignUpCompany(SignupBean bean) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();
        AndroidNetworking.post(AppData.url)
                .addBodyParameter("action", "register_company")
                .addBodyParameter("company_name", bean.getCompany_name())
                .addBodyParameter("company_office_address", bean.getCompany_ofice_address())
                .addBodyParameter("company_contact_no", bean.getCompany_contact_no())
                .addBodyParameter("company_area_of_business", bean.getCompany_area_of_business())
                .addBodyParameter("email", bean.getEmail())
                .addBodyParameter("password", bean.getPassword())
                .addBodyParameter("confirm_password", bean.getConfirm_password())
                .addBodyParameter("full_name", bean.getFull_name())
                .addBodyParameter("mobile_no", bean.getMobile_no())
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
                            Log.e("","sign output json= "+reader.toString());
                            if(status.equals("200")){
                                appData.setForGotUserId(reader.getJSONObject("body").getString("ID"));
                                signup.success(msg,reader.getJSONObject("body").getString("otp"));
                            }else {
                                signup.error(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            signup.fail("Something went wrong. Please try after some time.");
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        if(progress!=null)
                        progress.dismiss();
                        signup.fail("Something went wrong. Please try after some time.");
                    }
                });
    }
}

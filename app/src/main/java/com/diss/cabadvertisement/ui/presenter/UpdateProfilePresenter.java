package com.diss.cabadvertisement.ui.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import com.diss.cabadvertisement.ui.model.UpdateProfileBean;
import com.diss.cabadvertisement.ui.util.AppData;

import org.json.JSONException;
import org.json.JSONObject;


public class UpdateProfilePresenter {
    private Context context;
    private updateProfile updateProfile;
    private AppData appData;

    public UpdateProfilePresenter(Context context, updateProfile updateProfile) {
        this.context = context;
        this.updateProfile = updateProfile;
        appData = new AppData(context);
    }

    public interface updateProfile{
        void success(String response, String status);
        void error(String response);
        void fail(String response);
    }

    public void updateProfileData(UpdateProfileBean bean,String picname) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();
        AndroidNetworking.post(AppData.url)
                .addBodyParameter("action", "update_company_profile")//update_company_profile
                .addBodyParameter("company_name", bean.getCompany_name())
                .addBodyParameter("company_office_address", bean.getOffice_address())
                .addBodyParameter("company_contact_no", bean.getCompany_contact_no())
                .addBodyParameter("company_area_of_business", bean.getCompany_area_of_business())
                .addBodyParameter("full_name", bean.getFull_name())
//                .addBodyParameter("mobile_no", bean.getMobile_no())
                .addBodyParameter("user_id", appData.getUserID())
                .addBodyParameter("profile_pic", bean.getProfile_pic())
                .addBodyParameter("profile_pic_name", picname)

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
                            Log.e("","update profile output json= "+reader.toString());
                            if(status.equals("200")){

                                appData.setProfilePic(reader.getJSONObject("body").getString("user_pic_full"));
                                appData.setUsername(reader.getJSONObject("body").getString("display_name"));
                                appData.setCompanyNm(reader.getJSONObject("body").getString("company_name"));

                                appData.setCompanyOfficeAddress(reader.getJSONObject("body").getString("company_office_address"));
                                appData.setCompanyContactNo(reader.getJSONObject("body").getString("company_contact_no"));
                                appData.setCompanyAreaOfBusiness(reader.getJSONObject("body").getString("company_area_of_business"));
                                updateProfile.success(msg,"");
                            }else {
                                updateProfile.error(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            updateProfile.fail("Something went wrong. Please try after some time.");
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        if(progress!=null)
                        progress.dismiss();
                        updateProfile.fail("Something went wrong. Please try after some time.");
                    }
                });
    }
}

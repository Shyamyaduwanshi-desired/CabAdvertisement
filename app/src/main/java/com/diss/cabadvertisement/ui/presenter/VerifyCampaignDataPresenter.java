package com.diss.cabadvertisement.ui.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.diss.cabadvertisement.ui.util.AppData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class VerifyCampaignDataPresenter {
    private Context context;
    private CampaignData campaignData;
    private AppData appData;

    public VerifyCampaignDataPresenter(Context context, CampaignData campaignData) {
        this.context = context;
        this.campaignData = campaignData;
        appData = new AppData(context);
        AndroidNetworking.initialize(context);
    }

    public interface CampaignData{
        void success(String response, String status);
        void error(String response, String status);
        void fail(String response, String status);
    }

   public void DriverRecieveCampaingData(final String companyId, final String campaignId, final String position) {
       final ProgressDialog progress = new ProgressDialog(context);
       progress.setMessage("Please Wait..");
       progress.setCancelable(false);
       progress.show();
       AndroidNetworking.post(AppData.url)
               .addBodyParameter("action", "recieve_location_bydriver")
               .addBodyParameter("driver_id", appData.getUserID())
               .addBodyParameter("user_id", companyId)
               .addBodyParameter("s_id", campaignId)
               .addBodyParameter("status", position)
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
                           Log.e("","campaign Data verify output json= "+reader.toString());
                           if(status.equals("200")){
                               campaignData.success(msg,"VerifyCampaign");
                           }else {
                               campaignData.error(msg,"VerifyCampaign");
                           }
                       } catch (JSONException e) {
                           e.printStackTrace();
                           campaignData.fail("Something went wrong. Please try after some time.","VerifyCampaign");
                       }
                   }
                   @Override
                   public void onError(ANError error) {
                       // handle error
                       progress.dismiss();
                       campaignData.fail("Something went wrong. Please try after some time.","VerifyCampaign");
                   }
               });
   }


    public void UploadPhotoVechileWithSticker(final String campaignId, JSONArray jsonArrayImage) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();
        Log.e("","campaignId= "+campaignId+" getUserID= "+appData.getUserID()+" arImage.get(0)= "+String.valueOf(jsonArrayImage)/*arImage.get(0)*/);
        AndroidNetworking.post(AppData.url)
                .addBodyParameter("action", "upload_vehicle_with_stickers")
                .addBodyParameter("driver_id", appData.getUserID())
                .addBodyParameter("s_id", campaignId)
                .addBodyParameter("medias", String.valueOf(jsonArrayImage))
//                .addBodyParameter("medias", jsonArrayImage.toString())
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
                            Log.e("","UploadPhotoVechileWithSticker output json= "+reader.toString());
                            if(status.equals("200")){
                                campaignData.success(msg,"PhotoVechileWithSticker");
                            }else {
                                campaignData.error(msg,"PhotoVechileWithSticker");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            campaignData.fail("Something went wrong. Please try after some time33.","PhotoVechileWithSticker");
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        if(progress!=null)
                            progress.dismiss();
                        campaignData.fail("Something went wrong. Please try after some time.","PhotoVechileWithSticker");
                    }
                });
    }

    public void CompanyFeedback1(final String campaignId,final  String ratting,final String feedback) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();
        Log.e("","campaignId= "+campaignId+" getUserID= "+appData.getUserID()+" ratting= "+ratting+" feedback= "+feedback);
        AndroidNetworking.post(AppData.url)
                .addBodyParameter("action", "give_company_feedback")//feedback for each driver via company
                .addBodyParameter("user_id", appData.getUserID())
                .addBodyParameter("s_id", campaignId)
                .addBodyParameter("feedback", feedback)
                .addBodyParameter("rating", ratting)

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
                            Log.e("","feedback for each driver via company output json= "+reader.toString());

                            if(status.equals("200")){
                                campaignData.success(msg,"feedback");
                            }else {
//                                campaignData.error(msg,"PhotoVechileWithSticker");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            campaignData.fail("Something went wrong. Please try after some time.","feedback");
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        if(progress!=null)
                            progress.dismiss();
                        campaignData.fail("Something went wrong. Please try after some time.","feedback");
                    }
                });
    }

   /* public void CompanyFeedback(final String driverId, final String campaignId,final  String ratting,final String feedback) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();
        Log.e("","campaignId= "+campaignId+" getUserID= "+appData.getUserID()+" ratting= "+ratting+" feedback= "+feedback);
        AndroidNetworking.post(AppData.url)
                .addBodyParameter("action", "give_driver_feedback")//feedback for each driver via company
                .addBodyParameter("driver_id", driverId)
                .addBodyParameter("user_id", appData.getUserID())
                .addBodyParameter("s_id", campaignId)
                .addBodyParameter("feedback", feedback)
                .addBodyParameter("rating", ratting)

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
                            Log.e("","feedback for each driver via company output json= "+reader.toString());

                            if(status.equals("200")){
                                campaignData.success(msg,"feedback");
                            }else {
//                                campaignData.error(msg,"PhotoVechileWithSticker");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            campaignData.fail("Something went wrong. Please try after some time.","feedback");
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        if(progress!=null)
                            progress.dismiss();
                        campaignData.fail("Something went wrong. Please try after some time.","feedback");
                    }
                });
    }*/

    public void AcceptedDriverSticker(final String driverId, final String campaignId) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();
        AndroidNetworking.post(AppData.url)
                .addBodyParameter("action", "accept_driver_sticker")
                .addBodyParameter("driver_id", driverId)
                .addBodyParameter("s_id", campaignId)

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
                            Log.e("","accepted_sticker output json= "+reader.toString());
                            if(status.equals("200")){
                                campaignData.success(msg,"accepted_driver_sticker");
                            }else {
                                campaignData.error(msg,"accepted_driver_sticker");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            campaignData.fail("Something went wrong. Please try after some time.","VerifyCampaign");
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        progress.dismiss();
                        campaignData.fail("Something went wrong. Please try after some time.","VerifyCampaign");
                    }
                });
    }
    public void RejectedDriverSticker(final String driverId, final String campaignId) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();
        AndroidNetworking.post(AppData.url)
                .addBodyParameter("action", "reject_driver_sticker")
                .addBodyParameter("driver_id", driverId)
                .addBodyParameter("s_id", campaignId)

                .addHeaders("Username","admin")
                .addHeaders("Password","admin123")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject reader) {
                        // do anything with response
                        progress.dismiss();
                        try {//{"code":"200","title":"Success","message":"Photos rejected"}
                            String status = reader.getString("code");
                            String msg = reader.getString("message");
                            Log.e("","reject_sticker output json= "+reader.toString());
                            if(status.equals("200")){
                                campaignData.success(msg,"rejected_driver_sticker");
                            }else {
                                campaignData.error(msg,"rejected_driver_sticker");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            campaignData.fail("Something went wrong. Please try after some time.","VerifyCampaign");
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        progress.dismiss();
                        campaignData.fail("Something went wrong. Please try after some time.","VerifyCampaign");
                    }
                });
    }

    public void StartCampaign(final String campaignId) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();
        AndroidNetworking.post(AppData.url)
                .addBodyParameter("action", "start_the_campaign")
                .addBodyParameter("user_id", appData.getUserID())
                .addBodyParameter("s_id", campaignId)
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
                            Log.e("","start campaign output json= "+reader.toString());
                            if(status.equals("200")){
                                campaignData.success(msg,"StartCampaign");
                            }else {
                                campaignData.error(msg,"StartCampaign");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            campaignData.fail("Something went wrong. Please try after some time.","VerifyCampaign");
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        progress.dismiss();
                        campaignData.fail("Something went wrong. Please try after some time.","VerifyCampaign");
                    }
                });
    }

//    public void StartCampaign(final String campaignId) {
//        final ProgressDialog progress = new ProgressDialog(context);
//        progress.setMessage("Please Wait..");
//        progress.setCancelable(false);
//        progress.show();
//        AndroidNetworking.post(AppData.url)
//                .addBodyParameter("action", "start_the_campaign")
//                .addBodyParameter("user_id", appData.getUserID())
//                .addBodyParameter("s_id", campaignId)
//
//                .addHeaders("Username","admin")
//                .addHeaders("Password","admin123")
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject reader) {
//                        // do anything with response
//                        progress.dismiss();
//                        try {//{"code":"200","title":"Success","message":"Photos rejected"}
//                            String status = reader.getString("code");
//                            String msg = reader.getString("message");
//                            Log.e("","start campaign output json= "+reader.toString());
//                            if(status.equals("200")){
//                                campaignData.success(msg,"StartCampaign");
//                            }else {
//                                campaignData.error(msg,"StartCampaign");
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            campaignData.fail("Something went wrong. Please try after some time.","VerifyCampaign");
//                        }
//                    }
//                    @Override
//                    public void onError(ANError error) {
//                        // handle error
//                        progress.dismiss();
//                        campaignData.fail("Something went wrong. Please try after some time.","VerifyCampaign");
//                    }
//                });
//    }

}

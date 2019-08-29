package com.diss.cabadvertisement.ui.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.diss.cabadvertisement.ui.model.ReviewBean;
import com.diss.cabadvertisement.ui.util.AppData;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ReviewPresenter {
    private Context context;
    private ReviewDetails review;
    private AppData appData;

    public ReviewPresenter(Context context, ReviewDetails review) {
        this.context = context;
        this.review = review;
        appData = new AppData(context);
    }

    public interface ReviewDetails{
        void success(ArrayList<ReviewBean> response, String status);
        void error(String response);
        void fail(String response);
    }

    public void GetReview() {
        final ArrayList<ReviewBean> reviewList = new ArrayList<>();
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();
        AndroidNetworking.post(AppData.url)
                .addBodyParameter("action", "get_feedback_company")
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
                        reviewList.clear();
                        try {
                            String status = reader.getString("code");
                            String msg = reader.getString("message");
                            Log.e("","review output json= "+reader.toString());
                            if(status.equals("200")){
                                try {
                                    ReviewBean bean;

                                 JSONArray jsArrayPlan = reader.getJSONArray("body");
                             for (int count = 0; count < jsArrayPlan.length(); count++)
                                {
                                    JSONObject object = jsArrayPlan.getJSONObject(count);

//                                    JSONObject object = reader.getJSONObject("body");
                                    if (object != null) {
                                        bean = new ReviewBean();
                                        String display_name = object.getString("display_name");
                                        String feedback_text = object.getString("feedback_text");
                                        String feedback_on = object.getString("feedback_on");
                                        String rating = object.getString("feedback_rating");

//                                        bean.setReviewId(ID);
                                        bean.setReviewDsc(feedback_text);
                                        bean.setRateValue(rating);
                                        bean.setReviewName(display_name);
                                        bean.setReviewDate(feedback_on);
                                        reviewList.add(bean);
                                    }
                                }
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }

                                review.success(reviewList,"");

                            }
                            else {
                                review.error(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            review.fail("Something went wrong. Please try after some time.");
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        if(progress!=null)
                        progress.dismiss();
                        review.fail("Something went wrong. Please try after some time.");
                    }
                });
    }
}

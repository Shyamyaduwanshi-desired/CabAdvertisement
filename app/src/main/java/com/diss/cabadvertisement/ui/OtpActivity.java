package com.diss.cabadvertisement.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.diss.cabadvertisement.R;
import com.diss.cabadvertisement.ui.model.SignupBean;
import com.diss.cabadvertisement.ui.presenter.LoginPresenter;
import com.diss.cabadvertisement.ui.util.AppData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class OtpActivity extends AppCompatActivity implements View.OnClickListener, LoginPresenter.Login{

    TextView resendtext;
    Button verfiedbutton;
    private LoginPresenter presenter;
    AppData appdata;
    EditText code_one,code_two,code_three,code_four,code_five,code_six;
    String sOtp="",sUserid="",sDiff="",sPhoneNo="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_activity);
        appdata=new AppData(OtpActivity.this);
        presenter = new LoginPresenter(OtpActivity.this, OtpActivity.this);
        InitCompo();
        Listener();
        GetIntentData();


    }

    private void GetIntentData() {
        sOtp=getIntent().getStringExtra("otp");
        sDiff=getIntent().getStringExtra("diff");
        sPhoneNo=getIntent().getStringExtra("phone_no");
        sUserid=appdata.getForGotUserId();
        Log.e("","OtpActivity sOtp= "+sOtp+" sUserid= "+sUserid+" sDiff= "+sDiff);

       SetViewData();
    }

    private void SetViewData() {
        code_one.setText(sOtp.substring(0));
        code_two.setText(sOtp.substring(1));
        code_three.setText(sOtp.substring(2));
        code_four.setText(sOtp.substring(3));
        code_five.setText(sOtp.substring(4));
        code_six.setText(sOtp.substring(5));
    }


    private void InitCompo() {
        code_one = findViewById(R.id.code_one);
        code_two = findViewById(R.id.code_two);
        code_three = findViewById(R.id.code_three);
        code_four = findViewById(R.id.code_four);
        code_five = findViewById(R.id.code_five);
        code_six = findViewById(R.id.code_six);
        resendtext=findViewById(R.id.resend_text_id);
        verfiedbutton=findViewById(R.id.verfied_button_id);
    }
    private void Listener() {
        verfiedbutton.setOnClickListener(this);
        resendtext.setOnClickListener(this);

        code_one.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!code_one.getText().toString().equals("")) {
                    code_one.clearFocus();
                    code_two.requestFocus();
                    code_two.setCursorVisible(true);
                }

            }
        });

        code_two.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                code_two.requestFocus();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!code_two.getText().toString().equals("")) {
                    code_two.clearFocus();
                    code_three.requestFocus();
                    code_three.setCursorVisible(true);
                }

            }
        });

        code_three.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                code_three.requestFocus();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            //6594 otp
            @Override
            public void afterTextChanged(Editable s) {
                if (!code_three.getText().toString().equals("")) {
                    code_three.clearFocus();
                    code_four.requestFocus();
                    code_four.setCursorVisible(true);
                }

            }
        });

        code_four.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                code_four.requestFocus();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            //6594 otp
            @Override
            public void afterTextChanged(Editable s) {
                if (!code_four.getText().toString().equals("")) {
                    code_four.clearFocus();
                    code_five.requestFocus();
                    code_five.setCursorVisible(true);
                }

            }
        });

        code_five.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                code_five.requestFocus();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            //6594 otp
            @Override
            public void afterTextChanged(Editable s) {
                if (!code_five.getText().toString().equals("")) {
                    code_five.clearFocus();
                    code_six.requestFocus();
                    code_six.setCursorVisible(true);
                }

            }
        });

        code_six.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                code_six.requestFocus();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!code_six.getText().toString().equals("")) {
                    appdata.hideKeyboard(OtpActivity.this);
                    code_six.clearFocus();
                }

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.verfied_button_id:
                addValidationToViews();
                break;
           case R.id.resend_text_id:
               if(appdata.isNetworkConnected(this)){
                 if(TextUtils.isEmpty(sPhoneNo))
                 {
                     Toast.makeText(this, "Not Found Mobile number", Toast.LENGTH_SHORT).show();
                 }
                 else {
                     presenter.ResendOTP(sPhoneNo);
                 }
               }else {
                   appdata.ShowNewAlert(this,"Please connect to internet");
               }
                break;
        }

    }
    String otpcode="";
    public void addValidationToViews(){
         otpcode = code_one.getText().toString() +
                code_two.getText().toString() +
                code_three.getText().toString() +
                code_four.getText().toString() +
                code_five.getText().toString() +
                code_six.getText().toString();

       if(otpcode.length()<5){
           Toast.makeText(this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
            return;
        }
        else  if(!sOtp.equals(otpcode)){
           Toast.makeText(this, "Mismatch OTP", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
//             switch (sDiff)
//             {
//                 case "1"://for forgot
//                     Intent intent = new Intent(OtpActivity.this, ResetPasswordActivity.class);
//                     startActivity(intent);
//                     break;
//                 case "2"://for registration
//                     appdata.setForGotUserId("NA");
//                     Intent intent1 = new Intent(OtpActivity.this, Approval_activity.class);
//                     startActivity(intent1);
//                     finishAffinity();
//                     Animatoo.animateFade(OtpActivity.this);
//                     break;
//             }
           GetRegistration();

        }
    }
//for resend otp and verified otp
    @Override
    public void success(String response, String userType_, String otp) {
        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
        switch (userType_)
        {
            case ""://resend otp
                sOtp=otp;
                SetViewData();
                break;
            case "1"://verified otp
                switch (sDiff)
                {
                    case "1"://from forgot
                        Intent intent = new Intent(OtpActivity.this, ResetPasswordActivity.class);
                        startActivity(intent);
                        finishAffinity();
                        Animatoo.animateFade(OtpActivity.this);
                        break;
                    case "2"://from registration
                        appdata.setForGotUserId("NA");
                        Intent intent1 = new Intent(OtpActivity.this, Approval_activity.class);
                        startActivity(intent1);
                        finishAffinity();
                        Animatoo.animateFade(OtpActivity.this);
                        break;
                }
                break;
//            case "2":// approved by admin
//
//                break;
//            case "3":// not subscription done by user
//
//                break;
//            case "4"://subscription done by user
//
//                break;
        }

    }

    @Override
    public void error(String response) {
        appdata.ShowNewAlert(this,response);
    }

    @Override
    public void fail(String response) {
        appdata.ShowNewAlert(this,response);
    }

    public void GetRegistration()
    {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

// Get the Instance ID token//
                        String token = task.getResult().getToken();
//                        String msg = getString(R.string.fcm_token, token);
                        Log.d("", "shyam fcm token= "+token);

                        if(appdata.isNetworkConnected(OtpActivity.this)){
                            Log.e("","verify userid= "+sUserid);
                            presenter.VerifyOTP(sUserid,token);
                        }else {
                            appdata.ShowNewAlert(OtpActivity.this,"Please connect to internet");
                        }

                    }
                });

    }
}

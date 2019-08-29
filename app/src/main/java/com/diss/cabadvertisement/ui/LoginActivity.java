package com.diss.cabadvertisement.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.diss.cabadvertisement.R;
import com.diss.cabadvertisement.ui.presenter.LoginPresenter;
import com.diss.cabadvertisement.ui.util.AppData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.hbb20.CountryCodePicker;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoginPresenter.Login{
    TextView forgot;
    Button Loginbtn;
    TextView signuptext;
 EditText etMobileNo,etPassword;
    private LoginPresenter presenter;
    AppData appdata;
    private String sMobi,Password,countryCodeValue;
    CountryCodePicker countryCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        appdata=new AppData(LoginActivity.this);
        presenter = new LoginPresenter(LoginActivity.this, LoginActivity.this);
        InitCompo();
        Listener();
    }

    private void Listener() {
        signuptext.setOnClickListener(this);
        forgot.setOnClickListener(this);
        Loginbtn.setOnClickListener(this);
    }

    public void InitCompo()
    {
        etMobileNo = findViewById(R.id.et_mobile_no);
        etPassword = findViewById(R.id.et_password);
        forgot = findViewById(R.id.forgot_text_id);
        Loginbtn = findViewById(R.id.login_button_id);
        signuptext=findViewById(R.id.signin_text_id);
        countryCode=findViewById(R.id.spin_coun_code);
    }

    @Override
    public void onClick(View v) {
      switch (v.getId())
      {
          case R.id.signin_text_id:
              Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
              startActivity(intent);
              Animatoo.animateSlideUp(LoginActivity.this);
              break;
              case R.id.forgot_text_id:
                  Intent intent1 = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                  startActivity(intent1);
                  Animatoo.animateFade(LoginActivity.this);
              break;
           case R.id.login_button_id:
               addValidationToViews();
              break;
      }
    }
    public void addValidationToViews(){
        sMobi=etMobileNo.getText().toString().trim();
        Password=etPassword.getText().toString().trim();
        countryCodeValue=countryCode.getSelectedCountryCode();

        if (TextUtils.isEmpty(sMobi)) {
            etMobileNo.setError("Please Enter your Mobile Number");
            etMobileNo.requestFocus();
            return;
        }
        else  if(sMobi.length()<10){//!appdata.isValidPhoneNumber(sMobi)
            etMobileNo.setError("Enter a valid Mobile Number");
            etMobileNo.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(Password)) {
            etPassword.setError("Please Enter Password");
            etPassword.requestFocus();
            return;
        }
        else {
            GetRegistration();
//            if(appdata.isNetworkConnected(LoginActivity.this)){
//                Log.e("",""+("+"+countryCodeValue+sMobi)+""+Password);
//                presenter.LoginUser("+"+countryCodeValue+sMobi, Password);
//            }else {
//                appdata.ShowNewAlert(LoginActivity.this,"Please connect to internet");
//            }

        }
    }

    @Override
    public void success(String response, String diff_, String otp_) {
              Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
        Intent intent=null;
                switch (diff_)
                {
                    case "0"://not verify otp

                        intent = new Intent(LoginActivity.this, OtpActivity.class);
                        intent.putExtra("otp",otp_);
                        intent.putExtra("diff","2");
                        intent.putExtra("phone_no",("+"+countryCodeValue+sMobi));
                        break;
                    case "1"://not approve by admin
                     intent = new Intent(LoginActivity.this, Approval_activity.class);
                        break;
                    case "2":// approved by admin
                   intent = new Intent(LoginActivity.this, SubscriptionPlanActivity.class);
                        break;
//                    case "3":// not subscription done by user
//                        intent = new Intent(LoginActivity.this, SubscriptionPlanActivity.class);
//                        break;
                    case "3"://subscription done by user
                        intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        break;
                }

               if(diff_.equals("0")&&TextUtils.isEmpty(otp_)) {
                   Toast.makeText(this, "OTP not found", Toast.LENGTH_SHORT).show();
               }
               else
               {
                   startActivity(intent);
                   finishAffinity();
                   Animatoo.animateFade(LoginActivity.this);
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

    //for location

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
//
                        if(appdata.isNetworkConnected(LoginActivity.this)){
                            Log.e("",""+("+"+countryCodeValue+sMobi)+""+Password);
                            presenter.LoginUser("+"+countryCodeValue+sMobi, Password,token);
                        }else {
                            appdata.ShowNewAlert(LoginActivity.this,"Please connect to internet");
                        }
                    }
                });

    }
}

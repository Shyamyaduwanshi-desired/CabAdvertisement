package com.diss.cabadvertisement.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.diss.cabadvertisement.R;
import com.diss.cabadvertisement.ui.presenter.LoginPresenter;
import com.diss.cabadvertisement.ui.util.AppData;
import com.hbb20.CountryCodePicker;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener, LoginPresenter.Login {
    Button forgotbutton;
    private String sMobi,countryCodeValue;
    EditText etMobileNo;
    CountryCodePicker countryCode;
    private LoginPresenter presenter;
    AppData appdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_activity);
        appdata=new AppData(ForgotPasswordActivity.this);
        presenter = new LoginPresenter(ForgotPasswordActivity.this, ForgotPasswordActivity.this);
         InitCompo();
        Listener();
    }

    private void Listener() {
        forgotbutton.setOnClickListener(this);
    }

    private void InitCompo() {
        etMobileNo = findViewById(R.id.et_mobile_no);
        countryCode=findViewById(R.id.spin_coun_code);
        forgotbutton=findViewById(R.id.forgot_button_id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.forgot_button_id:
                addValidationToViews();
                break;
        }

    }
    public void addValidationToViews(){
        sMobi=etMobileNo.getText().toString().trim();
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
        else {
            if(appdata.isNetworkConnected(this)){
                Log.e("",""+("+"+countryCodeValue+sMobi));
                presenter.ForgotPass("+"+countryCodeValue+sMobi);
            }else {
                appdata.ShowNewAlert(this,"Please connect to internet");
            }
        }
    }
//for forgot
    @Override
    public void success(String response, String userType_, String otp_) {
        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ForgotPasswordActivity.this, OtpActivity.class);
        intent.putExtra("otp",otp_);
        intent.putExtra("diff","1");
        intent.putExtra("phone_no",("+"+countryCodeValue+sMobi));
//        intent.putExtra("user_id",userType_);
        startActivity(intent);
        Animatoo.animateSlideUp(ForgotPasswordActivity.this);
    }

    @Override
    public void error(String response) {
        appdata.ShowNewAlert(this,response);
    }

    @Override
    public void fail(String response) {
        appdata.ShowNewAlert(this,response);
    }
}

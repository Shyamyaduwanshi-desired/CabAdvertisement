package com.diss.cabadvertisement.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.diss.cabadvertisement.R;
import com.diss.cabadvertisement.ui.model.SignupBean;
import com.diss.cabadvertisement.ui.presenter.LoginPresenter;
import com.diss.cabadvertisement.ui.presenter.SignupPresenter;
import com.diss.cabadvertisement.ui.util.AppData;
import com.hbb20.CountryCodePicker;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener , SignupPresenter.SignUp{

    Button signup;
    TextView logintext;
    LinearLayout lyLogin;
    private SignupPresenter presenter;
    AppData appdata;
    EditText etCompanyNm,etOfficAdd,etOfficContact,etOfficArea,etPassword,etConfirmPassword,etPersonNm,etPersonContact,etEmail;
    CountryCodePicker countryCode;
    private String sCompanyNm,sOfficAdd,sOfficContact,sOfficArea,sMobi,sPassword,sConfirmPassword,sPersonNm,sEmail,countryCodeValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registraion_activity);
        appdata=new AppData(RegistrationActivity.this);
        presenter = new SignupPresenter(RegistrationActivity.this, RegistrationActivity.this);
        InitCompo();
        Listener();
    }
    private void Listener() {
        signup.setOnClickListener(this);
        logintext.setOnClickListener(this);
        lyLogin.setOnClickListener(this);
    }

    public void InitCompo()
    {
        signup=findViewById(R.id.signup_button_id);
        logintext=findViewById(R.id.login_text_id);
        lyLogin=findViewById(R.id.ly_login);

        etCompanyNm = findViewById(R.id.et_company_nm);
        etOfficAdd = findViewById(R.id.et_office_add);
        etOfficContact = findViewById(R.id.et_office_contact);
        etOfficArea = findViewById(R.id.et_area_of_business);
        etEmail = findViewById(R.id.et_email);

        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_pass);
        etPersonNm = findViewById(R.id.et_person_nm);
        etPersonContact = findViewById(R.id.et_person_phone);
        countryCode=findViewById(R.id.spin_coun_code);
    }


    public void addValidationToViews(){
        sCompanyNm=etCompanyNm.getText().toString().trim();
        sOfficAdd=etOfficAdd.getText().toString().trim();
        sOfficContact=etOfficContact.getText().toString().trim();
        sOfficArea=etOfficArea.getText().toString().trim();
        sEmail=etEmail.getText().toString().trim();

        sPassword=etPassword.getText().toString().trim();
        sConfirmPassword=etConfirmPassword.getText().toString().trim();
        sPersonNm=etPersonNm.getText().toString().trim();
        sMobi=etPersonContact.getText().toString().trim();
        countryCodeValue=countryCode.getSelectedCountryCode();

        if(TextUtils.isEmpty(sCompanyNm)){
            etCompanyNm.setError("Enter Company Name");
            etCompanyNm.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(sOfficAdd)) {
            etOfficAdd.setError("Please Enter Office Address");
            etOfficAdd.requestFocus();
            return;
        }
        else if(TextUtils.isEmpty(sOfficContact)){
            etOfficContact.setError("Enter Office Contact");
            etOfficContact.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(sOfficArea)) {
            etOfficArea.setError("Please Enter Office Area");
            etOfficArea.requestFocus();
            return;
        }
        else   if (TextUtils.isEmpty(sEmail)) {
            etEmail.setError("Please Enter your Email");
            etEmail.requestFocus();
            return;
        }
        else  if (!appdata.isEmailValid(sEmail)) {
            etEmail.setError("Enter a valid email");
            etEmail.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(sPassword)) {
            etPassword.setError("Please Enter Password");
            etPassword.requestFocus();
            return;
        }  else if (!sPassword.equals(sConfirmPassword)) {
            etConfirmPassword.setError("Password Not matching");
            etConfirmPassword.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(sPersonNm)) {
            etPersonNm.setError("Please Enter Person Name");
            etPersonNm.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(sMobi)) {
            etPersonContact.setError("Please Enter your Mobile Number");
            etPersonContact.requestFocus();
            return;
        }
        else  if(sMobi.length()<10){//!appdata.isValidPhoneNumber(sMobi)
            etPersonContact.setError("Enter a valid Mobile Number");
            etPersonContact.requestFocus();
            return;
        }
        else {
            if(appdata.isNetworkConnected(this)){
                SignupBean bean=new SignupBean();
                bean.setCompany_name(sCompanyNm);
                bean.setCompany_ofice_address(sOfficAdd);
                bean.setCompany_contact_no(sOfficContact);
                bean.setCompany_area_of_business(sOfficArea);
                bean.setEmail(sEmail);
                bean.setPassword(sPassword);
                bean.setConfirm_password(sConfirmPassword);
                bean.setFull_name(sPersonNm);
                bean.setMobile_no(("+"+countryCodeValue+sMobi));

                presenter.SignUpCompany(bean);
            }else {
                appdata.ShowNewAlert(this,"Please connect to internet");
            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
          /*  case R.id.signin_text_id:
                Intent intent = new Intent(RegistrationActivity.this, Approval_activity.class);
                startActivity(intent);
                Animatoo.animateFade(RegistrationActivity.this);
                break;*/
            case R.id.ly_login:
            case R.id.login_text_id:
                Intent intent1 = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent1);
                finish();
                Animatoo.animateFade(RegistrationActivity.this);
                break;
            case R.id.signup_button_id:
                addValidationToViews();
                break;
        }
    }
//for sign up
    @Override
    public void success(String response,String otp_) {
        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RegistrationActivity.this, OtpActivity.class);
        intent.putExtra("otp",otp_);
        intent.putExtra("diff","2");
        intent.putExtra("phone_no",("+"+countryCodeValue+sMobi));
        startActivity(intent);
        finish();
        Animatoo.animateSlideDown(RegistrationActivity.this);
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

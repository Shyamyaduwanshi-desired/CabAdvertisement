package com.diss.cabadvertisement.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.diss.cabadvertisement.R;
import com.diss.cabadvertisement.ui.presenter.LoginPresenter;
import com.diss.cabadvertisement.ui.util.AppData;

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener, LoginPresenter.Login {

    Button resetbutton;
    private String sPassword,sConfirmPassword;
    EditText etPassword,etConfirmPassword;
    private LoginPresenter presenter;
    AppData appdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_passowrd_activity);
        appdata=new AppData(ResetPasswordActivity.this);
        presenter = new LoginPresenter(ResetPasswordActivity.this, ResetPasswordActivity.this);
        InitCompo();
        Listener();

    }

    private void InitCompo() {
        resetbutton=(Button)findViewById(R.id.reset_button_id);
        etPassword = findViewById(R.id.et_new_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
    }
    private void Listener() {
        resetbutton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.reset_button_id:
                addValidationToViews();
                break;
        }

    }

    private void addValidationToViews() {
        sPassword=etPassword.getText().toString().trim();
        sConfirmPassword=etConfirmPassword.getText().toString().trim();
        if (TextUtils.isEmpty(sPassword)) {
            etPassword.setError("Please Enter New Password");
            etPassword.requestFocus();
        return;
        }
        else if (TextUtils.isEmpty(sConfirmPassword)) {
            etConfirmPassword.setError("Please Enter Confirm Password");
            etConfirmPassword.requestFocus();
            return;
        }
        else if (!sPassword.equals(sConfirmPassword)) {
        etConfirmPassword.setError("Password Not matching");
        etConfirmPassword.requestFocus();
        return;
    }
        else {
            if(appdata.isNetworkConnected(this)){
                presenter.ResetPass(sPassword,sConfirmPassword);
            }else {
                appdata.ShowNewAlert(this,"Please connect to internet");
            }
        }
    }

    @Override
    public void success(String response, String userType_, String otp) {
        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
        finishAffinity();
        Animatoo.animateSlideUp(ResetPasswordActivity.this);
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

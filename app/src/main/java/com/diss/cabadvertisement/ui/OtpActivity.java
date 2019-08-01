package com.diss.cabadvertisement.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.diss.cabadvertisement.R;

public class OtpActivity extends AppCompatActivity {

    TextView resendtext;
    Button verfiedbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_activity);
        resendtext=(TextView)findViewById(R.id.resend_text_id);


        verfiedbutton=(Button)findViewById(R.id.verfied_button_id);

        verfiedbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OtpActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });



        /*  resendtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OtpActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });


*/

    }
}

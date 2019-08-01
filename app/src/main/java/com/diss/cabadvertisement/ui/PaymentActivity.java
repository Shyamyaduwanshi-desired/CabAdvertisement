package com.diss.cabadvertisement.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diss.cabadvertisement.R;

public class PaymentActivity extends AppCompatActivity {

    Button confirmbutton;
    TextView paymentmethodtext;
    LinearLayout methodes;
    int count=0;
    int countBACK=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_option_activity);



        paymentmethodtext=(TextView)findViewById(R.id.paymenemethode_id);
        methodes=(LinearLayout)findViewById(R.id.methodes_id);

        paymentmethodtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countBACK=1;
                if (count==0){
                    paymentmethodtext.setVisibility(View.VISIBLE);
                    methodes.setVisibility(View.VISIBLE);
                    count=1;
                }else{
                    paymentmethodtext.setVisibility(View.VISIBLE);
                    methodes.setVisibility(View.GONE);
                    count=0;
                }
            }
        });

        confirmbutton=(Button)findViewById(R.id.confirmbooking_button_id);
        confirmbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMessage();
            }
        });

    }

    private void ShowMessage() {
        final Dialog dialog = new Dialog(PaymentActivity.this);
        dialog.setContentView(R.layout.thanku_activity);
        dialog.setTitle("Custom Dialog");
        TextView text1 = (TextView) dialog.findViewById(R.id.thanku_text_id);
        text1.setText("Thank you for the subscription.Our respresentive will communicate with you soon for further process.");
        dialog.show();
        Button DoneButton = (Button) dialog.findViewById(R.id.thanku_button_id);
        DoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });
    }
}

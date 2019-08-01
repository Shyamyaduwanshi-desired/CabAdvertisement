package com.diss.cabadvertisement.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.diss.cabadvertisement.R;

public class SubscriptionPlanActivity extends AppCompatActivity {

    RelativeLayout classic , professional ,premium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscription_plan_activity);

     classic=(RelativeLayout)findViewById(R.id.classic_relative_id);
     professional=(RelativeLayout)findViewById(R.id.professional_relative_id);
     premium=(RelativeLayout)findViewById(R.id.premium_relative_id);

        classic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubscriptionPlanActivity.this, DetailsActivity.class);
                startActivity(intent);
            }
        });


        professional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubscriptionPlanActivity.this, DetailsActivity.class);
                startActivity(intent);
            }
        });

        premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubscriptionPlanActivity.this, DetailsActivity.class);
                startActivity(intent);
            }
        });

    }
}

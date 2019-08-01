package com.diss.cabadvertisement.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.diss.cabadvertisement.R;

public class campaign_third_activity extends AppCompatActivity {

    LinearLayout campaignlinear;
    ImageView imageViewback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capmpaign_third_activity);

       campaignlinear =(LinearLayout)findViewById(R.id.campaign_linear_id);
        campaignlinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(campaign_third_activity.this, WeeklyAnalysisActivity.class);
                startActivity(intent);
            }
        });

        imageViewback=findViewById(R.id.imageback);
        imageViewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



    }
}

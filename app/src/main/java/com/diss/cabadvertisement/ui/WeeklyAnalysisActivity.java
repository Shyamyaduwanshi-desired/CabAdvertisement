package com.diss.cabadvertisement.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.diss.cabadvertisement.R;

public class WeeklyAnalysisActivity extends AppCompatActivity {

    LinearLayout weeklylinear;
    ImageView imageViewback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weeklyanalysis_activity);

        weeklylinear=(LinearLayout)findViewById(R.id.weekly_linear_id);
        weeklylinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeeklyAnalysisActivity.this, WeeklyAnalysisDetailsActivity.class);
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

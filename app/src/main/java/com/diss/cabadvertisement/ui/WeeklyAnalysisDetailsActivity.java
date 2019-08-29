package com.diss.cabadvertisement.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diss.cabadvertisement.R;

public class WeeklyAnalysisDetailsActivity extends AppCompatActivity {

    Button Campaignbutton;
    LinearLayout firstcircle;
    ImageView imageViewback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weekly_analyss_second_activity);

        imageViewback=findViewById(R.id.imageback);
        imageViewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Campaignbutton =(Button)findViewById(R.id.Campaign_button_id);
        Campaignbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeeklyAnalysisDetailsActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });

        firstcircle=(LinearLayout)findViewById(R.id.firstcircle_id);
        firstcircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

    }

    private void showDialog() {
        final Dialog dialog = new Dialog(WeeklyAnalysisDetailsActivity.this);
        dialog.setContentView(R.layout.id_activity);
        TextView text1 = (TextView) dialog.findViewById(R.id.id_id);
        TextView text2 = (TextView) dialog.findViewById(R.id.locationcovered_id);
        TextView text3 = (TextView) dialog.findViewById(R.id.location_id);
        TextView text4 = (TextView) dialog.findViewById(R.id.totalkm_id);
        TextView text5 = (TextView) dialog.findViewById(R.id.km_id);
        TextView text6 = (TextView) dialog.findViewById(R.id.totalhours_id);
        TextView text7 = (TextView) dialog.findViewById(R.id.hours_id);
        dialog.show();
        Button DoneButton = (Button) dialog.findViewById(R.id.ok_button_id);
        DoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeeklyAnalysisDetailsActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });

    }
}

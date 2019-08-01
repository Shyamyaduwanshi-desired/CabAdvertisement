package com.diss.cabadvertisement.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diss.cabadvertisement.R;

public class MyCampaignsDetailsActivity extends AppCompatActivity {
    LinearLayout firstcircle;
    int count=0;
    int countBACK=0;
    ImageView imageViewback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycampaigns_details_activity);

        firstcircle=(LinearLayout)findViewById(R.id.firstcircle_id);
        firstcircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               showdialog();
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

    private void showdialog() {
        final Dialog dialog = new Dialog(MyCampaignsDetailsActivity.this);
        dialog.setContentView(R.layout.campaigns_id_activity);

        TextView text1 = (TextView) dialog.findViewById(R.id.idtext_id);
        TextView text2 = (TextView) dialog.findViewById(R.id.idnumber_id);
        TextView text3 = (TextView) dialog.findViewById(R.id.status_id);
        TextView text4 = (TextView) dialog.findViewById(R.id.notrecived_id);
        final LinearLayout recivedlinear=(LinearLayout)dialog.findViewById(R.id.recived_linear_id);
        final LinearLayout notrecivedlinear=(LinearLayout)dialog.findViewById(R.id.notrecived_linear_id);
        dialog.show();
    /*    Button DoneButton = (Button) dialog.findViewById(R.id.okk_button_id);*/

        dialog.show();
        Button DoneButton = (Button) dialog.findViewById(R.id.okk_button_id);
        DoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                countBACK=1;
                if (count==0){
                    notrecivedlinear.setVisibility(View.GONE);
                    recivedlinear.setVisibility(View.VISIBLE);
                    count=1;
                   /* Intent intent = new Intent(MyCampaignsDetailsActivity.this, DashboardActivity.class);
                    startActivity(intent);
*/
                }else{
                    notrecivedlinear.setVisibility(View.VISIBLE);
                    recivedlinear.setVisibility(View.GONE);
                    count=0;
                    Intent intent = new Intent(MyCampaignsDetailsActivity.this, campaign_third_activity.class);
                    startActivity(intent);
                }


                /* Intent intent = new Intent(MyCampaignsDetailsActivity.this, DashboardActivity.class);
                startActivity(intent);*/
            }
        });

       /* DoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              *//*  Intent intent = new Intent(MyCampaignsDetailsActivity.this, DashboardActivity.class);
                startActivity(intent);
*//*        countBACK=1;
                if (count==0){
                    notrecivedlinear.setVisibility(View.GONE);
                    recivedlinear.setVisibility(View.VISIBLE);
                    count=1;
                    Intent intent = new Intent(MyCampaignsDetailsActivity.this, DashboardActivity.class);
                    startActivity(intent);

                }else{
                    notrecivedlinear.setVisibility(View.VISIBLE);
                    recivedlinear.setVisibility(View.GONE);
                    count=0;
                }

            }
        });*/

    }
}

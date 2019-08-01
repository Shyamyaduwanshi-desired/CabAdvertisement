package com.diss.cabadvertisement.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diss.cabadvertisement.R;

public class HelpActivity extends AppCompatActivity {

    TextView clicktext;
    LinearLayout clicllinear;
    int count=0;
    int countBACK=0;
    ImageView imageViewback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_activity);

        clicktext=(TextView)findViewById(R.id.click_id);
        clicllinear=(LinearLayout)findViewById(R.id.clickeventlinear);

        clicktext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countBACK=1;
                if (count==0){
                    clicllinear.setVisibility(View.GONE);
                    count=1;

                }else{
                    clicllinear.setVisibility(View.VISIBLE);
                    count=0;
                }
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


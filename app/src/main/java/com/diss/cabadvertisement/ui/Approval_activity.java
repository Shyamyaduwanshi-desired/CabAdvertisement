package com.diss.cabadvertisement.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.diss.cabadvertisement.R;

public class Approval_activity extends AppCompatActivity {

    TextView textapporwal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.approval_activity);

        textapporwal=(TextView)findViewById(R.id.textapporwal_id);
        textapporwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Approval_activity.this, SubscriptionPlanActivity.class);
//                startActivity(intent);

            }
        });

    }
}

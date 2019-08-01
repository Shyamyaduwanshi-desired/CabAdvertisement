package com.diss.cabadvertisement.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.diss.cabadvertisement.R;

public class GetStartActivity extends AppCompatActivity {
    Button getstarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_started_activity);

        getstarted=(Button)findViewById(R.id.getstarted_button_id);

        getstarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetStartActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });




    }
}

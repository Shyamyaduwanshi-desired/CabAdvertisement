package com.diss.cabadvertisement.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.diss.cabadvertisement.R;

public class ShareActivity extends AppCompatActivity {


    ImageView imageViewback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_app_activity);

        imageViewback=findViewById(R.id.imageback);
        imageViewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}


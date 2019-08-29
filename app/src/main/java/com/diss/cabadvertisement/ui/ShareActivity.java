package com.diss.cabadvertisement.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.diss.cabadvertisement.R;
import com.diss.cabadvertisement.ui.util.AppData;


import java.util.List;

public class ShareActivity extends AppCompatActivity implements View.OnClickListener{

//    private HelpAndRequestPresenter HelpAndReqPresenter;
    AppData appdata;
    ImageView imageViewback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_app_activity);
        appdata=new AppData(this);
//        HelpAndReqPresenter = new HelpAndRequestPresenter(this, this);
        InitCompo();
        Listener();
    }
    private void Listener() {
        imageViewback.setOnClickListener(this);
        lyShareWhatsApp.setOnClickListener(this);
        lyShareFacebook.setOnClickListener(this);
    }
    LinearLayout lyShareWhatsApp,lyShareFacebook;
    public void InitCompo()
    {
        imageViewback=findViewById(R.id.imageback);
        lyShareWhatsApp=findViewById(R.id.ly_share_whatapp);
        lyShareFacebook=findViewById(R.id.ly_share_facebook);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imageback:
                onBackPressed();
                break;
            case R.id.ly_share_whatapp:
                ShareWhatApp();
                break;
            case R.id.ly_share_facebook:
                ShareFaceBookApp();
                break;

        }
    }
    String Text="https://play.google.com/store/apps/details?id=com.starmakerinteractive.starmaker&hl=en_IN";
    public void ShareWhatApp()
    {
//        Uri imageUri = Uri.parse(pictureFile.getAbsolutePath());
//        http://cabadvert.webdesigninguk.co/assets/images/default.png
//        Uri imageUri = Uri.parse("http://cabadvert.webdesigninguk.co/assets/images/default.png");
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        //Target whatsapp:
        shareIntent.setPackage("com.whatsapp");
        //Add text and then Image URI
//        shareIntent.putExtra(Intent.EXTRA_TEXT, Text);
          /* shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/jpeg");*/

        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
        shareIntent.putExtra(Intent.EXTRA_TEXT, Text);

        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(shareIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
        }


//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_SEND);
//        intent.putExtra(Intent.EXTRA_TEXT, Text);
//        intent.setType("text/plain");
//        intent.putExtra(Intent.EXTRA_STREAM,imageUri);
//        intent.setType("image/jpeg");
//        intent.setPackage("com.whatsapp");
//        startActivity(intent);
    }

    public void ShareFaceBookApp()
    {
//        Uri imageUri = Uri.parse(pictureFile.getAbsolutePath());
//        http://cabadvert.webdesigninguk.co/assets/images/default.png
        Uri imageUri = Uri.parse("http://cabadvert.webdesigninguk.co/assets/images/default.png");
//        Intent shareIntent = new Intent();
//        shareIntent.setAction(Intent.ACTION_SEND);
//        //Target whatsapp:
////        shareIntent.setPackage("com.facebook");
//        shareIntent.setPackage("com.facebook.orca");
//        //Add text and then Image URI
//        shareIntent.putExtra(Intent.EXTRA_TEXT, Text);
////        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
////        shareIntent.setType("image/jpeg");
//        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//        try {
//            startActivity(shareIntent);
//        } catch (android.content.ActivityNotFoundException ex) {
//            Toast.makeText(this, "Facebook have not been installed.", Toast.LENGTH_SHORT).show();
//        }



        Intent shareIntent = new Intent(Intent.ACTION_SEND);

//        shareIntent.setType("image/*");
//
//        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, Text);//(String) v.getTag(R.string.app_name)
//
//        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri); // put your image URI


        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
        shareIntent.putExtra(Intent.EXTRA_TEXT, Text);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        PackageManager pm = this.getPackageManager();

        List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);

        for (final ResolveInfo app : activityList)
        {
            if ((app.activityInfo.name).contains("facebook"))
            {

                final ActivityInfo activity = app.activityInfo;

//                final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);

//                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);

//                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

//                shareIntent.setComponent(name);

               startActivity(shareIntent);

                break;
            }
        }


    }

    @Override
    public void onBackPressed() {
        finish();
        Animatoo.animateFade(this);
    }
}

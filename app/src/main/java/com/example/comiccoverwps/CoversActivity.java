package com.example.comiccoverwps;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CoversActivity extends AppCompatActivity {
    LinearLayout c1;
    LinearLayout c2;
    String last;
    int count = 0;ArrayList<String> data;
    int pages = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covers);




        Intent in = getIntent();
        final String name = in.getStringExtra("name");
        ((TextView) findViewById(R.id.c_header)).setText(name);

        data = in.getStringArrayListExtra("data");
            c1 = (LinearLayout) findViewById(R.id.cc1);
            c2 = (LinearLayout) findViewById(R.id.cc2);
            final Context c = this;

            pages++;

        for(int i = 42*(pages-1);i<=42*(pages);i++){
            final String cover;
            try {
                cover = (String) data.get(i);
                last = cover;
            }catch (Exception e){break;}
            addComic(cover,name);
            last = cover;
            count++;
        }

        if(last == data.get(data.size()-1)){
            findViewById(R.id.cnext_ll).setVisibility(View.GONE);
        }
        else{
            findViewById(R.id.cnext_ll).setVisibility(View.VISIBLE);
        }

        if(pages == 1){
            findViewById(R.id.cprev_ll).setVisibility(View.GONE);
        }
        else{
            findViewById(R.id.cprev_ll).setVisibility(View.VISIBLE);
        }

            findViewById(R.id.cprev_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pages--;
                    ((LinearLayout) findViewById(R.id.cc1)).removeAllViews();
                    ((LinearLayout) findViewById(R.id.cc2)).removeAllViews();

                    for(int i = 42*(pages-1)+1;i<=42*(pages);i++){
                        final String cover;
                        try {
                            cover = (String) data.get(i);
                            last = cover;
                        }catch (Exception e){break;}
                        addComic(cover,name);
                        last = cover;
                        count++;
                    }

                    if(last == data.get(data.size()-1)){
                        findViewById(R.id.cnext_ll).setVisibility(View.GONE);
                    }
                    else{
                        findViewById(R.id.cnext_ll).setVisibility(View.VISIBLE);
                    }

                    if(pages == 1){
                        findViewById(R.id.cprev_ll).setVisibility(View.GONE);
                    }
                    else{
                        findViewById(R.id.cprev_ll).setVisibility(View.VISIBLE);
                    }

                }
            });

            findViewById(R.id.cnext_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pages++;
                    ((LinearLayout) findViewById(R.id.cc1)).removeAllViews();
                    ((LinearLayout) findViewById(R.id.cc2)).removeAllViews();

                    for(int i = 42*(pages-1)+1;i<=42*(pages);i++){
                        final String cover;
                        try {
                            cover = (String) data.get(i);
                            last = cover;
                        }catch (Exception e){break;}
                        addComic(cover,name);
                        last = cover;
                        count++;
                    }

                    if(last == data.get(data.size()-1)){
                        findViewById(R.id.cnext_ll).setVisibility(View.GONE);
                    }
                    else{
                        findViewById(R.id.cnext_ll).setVisibility(View.VISIBLE);
                    }

                    if(pages == 1){
                        findViewById(R.id.cprev_ll).setVisibility(View.GONE);
                    }
                    else{
                        findViewById(R.id.cprev_ll).setVisibility(View.VISIBLE);
                    }

                }
            });
    }

    public void addComic(final String cover,final String name){
        RelativeLayout wll = new RelativeLayout(this);
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        wll.setLayoutParams(ll);
        final Context c = this;
        wll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(c,MainCoverActivity.class);
                i.putExtra("cover",cover);
                i.putExtra("name",name);
                startActivity(i);
            }
        });

        ImageView iv = new ImageView(c);
        iv.setLayoutParams(ll);
        wll.addView(iv);

        ImageButton ib = new ImageButton(c);
        RelativeLayout.LayoutParams rll = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        rll.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        rll.addRule(RelativeLayout.ALIGN_PARENT_END);
        ib.setLayoutParams(rll);
        ib.setImageDrawable(getDrawable(R.drawable.ic_file_download_black_20dp));
        ib.setBackgroundColor(Color.TRANSPARENT);
        wll.addView(ib);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Comixion").exists() == false){
                    new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Comixion").mkdir();
                }

                if(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Comixion/C_Downloads").exists() == false){
                    new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Comixion/C_Downloads").mkdir();
                }

                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                DownloadManager.Request dr = new DownloadManager.Request(Uri.parse(cover));

                String[] parts = cover.substring(cover.lastIndexOf("/"),cover.length()).replace("/","").replace(".jpg","").split("-");

                String volume = parts[0];
                String issue = parts[1];

                String fname = name+"_"+volume+"_"+issue;

                dr.setAllowedNetworkTypes(
                        DownloadManager.Request.NETWORK_WIFI
                                | DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false).setTitle(name+" Vol."+volume+" #"+issue)
                        .setDescription("Comixion")
                        .setDestinationInExternalFilesDir(getApplicationContext(),"/Comixion/C_Downloads", fname+".jpg");


                dm.enqueue(dr);
            }
        });

        Glide.with(c).load(cover).into(iv);
        if(count%2 == 0){
            c1.addView(wll);
        }
        else{
            c2.addView(wll);
        }
    }


    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public void setSP(String key,String value){
        SharedPreferences.Editor e = getSharedPreferences("cxion",MODE_PRIVATE).edit();
        e.putString(key,value);
        e.commit();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public String getSP(String key) {
        SharedPreferences s = getSharedPreferences("cxion", MODE_PRIVATE);
        return s.getString(key, "");
    }


}

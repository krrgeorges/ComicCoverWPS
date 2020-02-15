package com.example.comiccoverwps;

import android.Manifest;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.mbms.DownloadRequest;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.IOException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainCoverActivity extends AppCompatActivity {
    String p_wp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cover);



        Intent i = getIntent();
        final String cover = i.getStringExtra("cover");
        final String name = i.getStringExtra("name");


        Glide.with(this).load(cover).apply(new RequestOptions().override(Integer.parseInt(getSP("d_w")),Integer.parseInt(getSP("d_h")))).into((ImageView) findViewById(R.id.m_img));
        Glide.with(this).load
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                    if (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/temp").list().length > 0) {
                        String fname = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/temp").list()[0];
                        WallpaperManager wp = (WallpaperManager) getSystemService(WALLPAPER_SERVICE);
                        Bitmap b = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Comixion/temp/"+fname);


                        SharedPreferences s = getSharedPreferences("cxion",MODE_PRIVATE);
                        int width = Integer.parseInt(s.getString("d_w","0"));
                        int height = Integer.parseInt(s.getString("d_h","0"));

                        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                            return;
                        }
                        else{
                            b = Bitmap.createScaledBitmap(b,width,height,false);
                        }

                        try {
                            if(getSP("effect").equals("h")){
                                wp.setBitmap(b);
                            }
                            else if(getSP("effect").equals("l")){
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    wp.setBitmap(b,null,false,WallpaperManager.FLAG_LOCK);
                                }
                            }
                            else if(getSP("effect").equals("handl")){
                                wp.setBitmap(b);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    wp.setBitmap(b,null,false,WallpaperManager.FLAG_LOCK);
                                }
                            }
                            setSP("type","s");
                            Toast.makeText(getApplicationContext(),"Wallpaper set",Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            Toast.makeText(getApplicationContext(),"Wallpaper could not be set",Toast.LENGTH_LONG).show();
                        }
                    }
            }
        };


        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


        ((ImageButton) findViewById(R.id.c_download)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion").exists() == false) {
                        new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion").mkdir();
                    }

                    if (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/C_Downloads").exists() == false) {
                        new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/C_Downloads").mkdir();
                    }

                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    DownloadManager.Request dr = new DownloadManager.Request(Uri.parse(cover));

                    String[] parts = cover.substring(cover.lastIndexOf("/"), cover.length()).replace("/", "").replace(".jpg", "").split("-");

                    String volume = parts[0];
                    String issue = parts[1];

                    String fname = name + "_" + volume + "_" + issue;
                    Toast.makeText(getApplicationContext(),"Downloading "+name + " Vol." + volume + " #" + issue,Toast.LENGTH_LONG).show();
                    dr.setAllowedNetworkTypes(
                            DownloadManager.Request.NETWORK_WIFI
                                    | DownloadManager.Request.NETWORK_MOBILE)
                            .setAllowedOverRoaming(false).setTitle(name + " Vol." + volume + " #" + issue)
                            .setDescription("Comixion")
                            .setDestinationInExternalFilesDir(getApplicationContext(), "/Comixion/C_Downloads", fname + ".jpg");


                    dm.enqueue(dr);
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();

                }
            }
        });

        ((ImageButton) findViewById(R.id.c_wp)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion").exists() == false) {
                        new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion").mkdir();
                    }

                    if (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/temp").exists() == false) {
                        new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/temp").mkdir();
                    }

                    if (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/temp").list().length > 0) {
                        for (String f : new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/temp").list()) {
                            new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/temp/" + f).delete();
                        }
                    }

                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    DownloadManager.Request dr = new DownloadManager.Request(Uri.parse(cover));

                    setSP("p_wp",cover+" "+name);

                    String[] parts = cover.substring(cover.lastIndexOf("/"), cover.length()).replace("/", "").replace(".jpg", "").split("-");

                    String volume = parts[0];
                    String issue = parts[1];

                    String fname = name + "_" + volume + "_" + issue;

                    dr.setAllowedNetworkTypes(
                            DownloadManager.Request.NETWORK_WIFI
                                    | DownloadManager.Request.NETWORK_MOBILE)
                            .setAllowedOverRoaming(false).setTitle(name + " Vol." + volume + " #" + issue)
                            .setDescription("Comixion")
                            .setDestinationInExternalPublicDir("/Comixion/temp", fname + ".jpg");

                    dm.enqueue(dr);
                    Toast.makeText(getApplicationContext(),"Wallpaper is being set",Toast.LENGTH_LONG).show();
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
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

    public String getSP(String key){
        SharedPreferences s = getSharedPreferences("cxion",MODE_PRIVATE);
        return s.getString(key,"");
    }


    private static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }
}

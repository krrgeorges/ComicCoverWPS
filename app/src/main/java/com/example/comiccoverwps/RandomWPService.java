package com.example.comiccoverwps;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class RandomWPService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Toast.makeText(getApplicationContext(),Build.VERSION.SDK_INT+"",Toast.LENGTH_LONG).show();
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "Channelos";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Comixion Notification Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
            Intent intent = new Intent();
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID);
            builder.setContentTitle("Comixion WPS Actions");
            builder.setContentText("");

            builder.setSmallIcon(R.mipmap.ic_launcher);
            Bitmap largeIconBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_keyboard_arrow_right_black_35dp);
            builder.setLargeIcon(largeIconBitmap);
            Intent playIntent = new Intent(this, WPDownloader.class);
            playIntent.setAction("Download");
            PendingIntent pendingPlayIntent = PendingIntent.getService(this, 0, playIntent, 0);
            NotificationCompat.Action playAction = new NotificationCompat.Action(android.R.drawable.ic_media_play, "Download Wallpaper", pendingPlayIntent);
            builder.addAction(playAction);
            Intent pauseIntent = new Intent(this, WPChanger.class);
            pauseIntent.setAction("Change");
            PendingIntent pendingPrevIntent = PendingIntent.getService(this, 0, pauseIntent, 0);
            NotificationCompat.Action prevAction = new NotificationCompat.Action(android.R.drawable.ic_media_pause, "Change Wallpaper", pendingPrevIntent);
            builder.addAction(prevAction);
            Notification notification = builder.build();
            startForeground(1, notification);



        }

        Toast.makeText(getApplicationContext(),getSP("los"),Toast.LENGTH_LONG).show();




        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                ArrayList<String> mkeys = null;
                JSONObject jdata = null;
                try{
                BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open("data/cover_comics.json")));
                String temp = "";
                String data = "";
                while((temp = br.readLine())!=null){
                    data += temp;
                }

                jdata = new JSONObject(data);
                Iterator<String> keys = jdata.keys();
                    mkeys = new ArrayList<>();
                    while(keys.hasNext()) {
                        String cat = keys.next();
                        mkeys.add(cat);

                    }
                }catch (Exception e){}

                    Random r = new Random();

                Calendar for_bcks = Calendar.getInstance();
                if( (for_bcks.get(Calendar.HOUR_OF_DAY) == 3 && for_bcks.get(Calendar.MINUTE) == 0) ||
                        (for_bcks.get(Calendar.HOUR_OF_DAY) == 6 && for_bcks.get(Calendar.MINUTE) == 0) ||
                        (for_bcks.get(Calendar.HOUR_OF_DAY) == 9 && for_bcks.get(Calendar.MINUTE) == 0) ||
                        (for_bcks.get(Calendar.HOUR_OF_DAY) == 12 && for_bcks.get(Calendar.MINUTE) == 0) ||
                        (for_bcks.get(Calendar.HOUR_OF_DAY) == 15 && for_bcks.get(Calendar.MINUTE) == 0) ||
                        (for_bcks.get(Calendar.HOUR_OF_DAY) == 18 && for_bcks.get(Calendar.MINUTE) == 0) ||
                        (for_bcks.get(Calendar.HOUR_OF_DAY) == 21 && for_bcks.get(Calendar.MINUTE) == 0)) {
                    if(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Comixion").exists() == false){
                        new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Comixion").mkdir();
                    }

                    if(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Comixion/bcks").exists() == false){
                        new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Comixion/bcks").mkdir();
                    }

                    if(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Comixion/bcks").list().length > 0){
                        for(String f:new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Comixion/bcks").list()){
                            new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Comixion/bcks/"+f).delete();
                        }
                    }
                    for(int i=0;i<=10;i++){
                        String rkey = mkeys.get(r.nextInt(mkeys.size()));

                        JSONArray covers = null;
                        String cover = null;
                        try {
                            covers = jdata.getJSONArray(rkey);
                            cover = (String) covers.get(r.nextInt(covers.length()));
                        } catch (JSONException e) {
                            continue;
                        }

                        DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                        DownloadManager.Request dr = new DownloadManager.Request(Uri.parse(cover));

                        String[] parts = cover.substring(cover.lastIndexOf("/"),cover.length()).replace("/","").replace(".jpg","").split("-");

                        String volume = parts[0];
                        String issue = parts[1];

                        String fname = rkey+"_"+volume+"_"+issue;

                        dr.setAllowedNetworkTypes(
                                DownloadManager.Request.NETWORK_WIFI
                                        | DownloadManager.Request.NETWORK_MOBILE)
                                .setAllowedOverRoaming(false).setTitle(rkey+" Vol."+volume+" #"+issue)
                                .setDescription("Comixion")
                                .setDestinationInExternalPublicDir("/Comixion/bcks", fname.replace("/","")+".jpg");

                        dm.enqueue(dr);
                    }
                }




                SharedPreferences s = getSharedPreferences("cxion", Context.MODE_PRIVATE);
                String rtype =  s.getString("rtype","");
                String type = s.getString("type","");
                String active = s.getString("active","");
                if(active.equals("1") && type.equals("r")){
                    if(rtype.equals("t")){
                            boolean shouldDo = false;
                            if (getSP("los").equals("")) {
                                shouldDo = true;
                                Calendar set = Calendar.getInstance();
                                int day = set.get(Calendar.DAY_OF_MONTH);
                                int month = set.get(Calendar.MONTH);
                                int year = set.get(Calendar.YEAR);
                                int hour = set.get(Calendar.HOUR_OF_DAY);
                                int minute = set.get(Calendar.MINUTE);
                                int second = set.get(Calendar.SECOND);

                                setSP("los", day + "/" + month + "/" + year + " " + hour + ":" + minute + ":" + second);
                            } else {
                                String los = getSP("los");
                                String date = los.split(" ")[0];
                                String time = los.split(" ")[1];

                                Calendar present = Calendar.getInstance();
                                long present_time = present.getTimeInMillis();

                                Calendar prev = Calendar.getInstance();
                                prev.set(Calendar.DAY_OF_MONTH, Integer.valueOf(date.split("/")[0]));
                                prev.set(Calendar.MONTH, Integer.valueOf(date.split("/")[1]));
                                prev.set(Calendar.YEAR, Integer.valueOf(date.split("/")[2]));
                                prev.set(Calendar.HOUR_OF_DAY, Integer.valueOf(time.split(":")[0]));
                                prev.set(Calendar.MINUTE, Integer.valueOf(time.split(":")[1]));
                                prev.set(Calendar.SECOND, Integer.valueOf(time.split(":")[2]));

                                long prev_time = prev.getTimeInMillis();

                                String timey = getSP("timey");
                                String num = timey.split(" ")[0];
                                String ntype = timey.split(" ")[1];
                                long judge = 0;
                                if (ntype.equals("hours")) {
                                    present.add(Calendar.HOUR_OF_DAY, Integer.parseInt(num));
                                    long ttime = present.getTimeInMillis();
                                    judge = 60 * 1000 * 60 * Integer.parseInt(num);
                                } else if (ntype.equals("minutes")) {
                                    present.add(Calendar.MINUTE, Integer.parseInt(num));
                                    long ttime = present.getTimeInMillis();
                                    judge = 60 * 1000 * Integer.parseInt(num);;
                                } else if (ntype.equals("days")) {
                                    present.add(Calendar.DAY_OF_MONTH, Integer.parseInt(num));
                                    long ttime = present.getTimeInMillis();
                                    judge = 60 * 1000 *60 * 24 * Integer.parseInt(num);;
                                }

                                if ((present_time - prev_time) >= judge) {
                                    shouldDo = true;
                                    Calendar set = Calendar.getInstance();
                                    int day = set.get(Calendar.DAY_OF_MONTH);
                                    int month = set.get(Calendar.MONTH);
                                    int year = set.get(Calendar.YEAR);
                                    int hour = set.get(Calendar.HOUR_OF_DAY);
                                    int minute = set.get(Calendar.MINUTE);
                                    int second = set.get(Calendar.SECOND);
                                    setSP("los", day + "/" + month + "/" + year + " " + hour + ":" + minute + ":" + second);
                                }

                            }


                            if (shouldDo) {
                                try{
                                BroadcastReceiver onComplete = new BroadcastReceiver() {
                                    @RequiresApi(api = Build.VERSION_CODES.N)
                                    @Override
                                    public void onReceive(Context context, Intent intent) {
                                        try {
                                            if (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/temp").list().length > 0) {
                                                String fname = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/temp").list()[0];
                                                WallpaperManager wp = (WallpaperManager) context.getSystemService(Context.WALLPAPER_SERVICE);

                                                Bitmap b = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/temp/" + fname);

                                                SharedPreferences s = getSharedPreferences("cxion",MODE_PRIVATE);
                                                int width = Integer.parseInt(s.getString("d_w","0"));
                                                int height = Integer.parseInt(s.getString("d_h","0"));

                                                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){

                                                    final Timer it = new Timer();

                                                    TimerTask itt = new TimerTask() {
                                                        @Override
                                                        public void run() {
                                                            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                                                                if (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/temp").list().length > 0) {
                                                                    String fname = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/temp").list()[0];
                                                                    WallpaperManager wp = (WallpaperManager) getSystemService(Context.WALLPAPER_SERVICE);
                                                                    Bitmap b = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/temp/" + fname);

                                                                    SharedPreferences sp = getSharedPreferences("cxion", MODE_PRIVATE);
                                                                    int width = Integer.parseInt(sp.getString("d_w", "0"));
                                                                    int height = Integer.parseInt(sp.getString("d_h", "0"));
                                                                    b = Bitmap.createScaledBitmap(b, width, height, false);

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
                                                                    } catch (IOException ie) {
                                                                        Toast.makeText(getApplicationContext(),ie.getMessage(),Toast.LENGTH_LONG).show();
                                                                    }
                                                                    it.cancel();

                                                                }
                                                                else if (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/bcks").list().length > 0) {
                                                                    Random ro = new Random();
                                                                    String fname = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/bcks").list()[ro.nextInt(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/bcks").list().length)];
                                                                    WallpaperManager wp = (WallpaperManager) getSystemService(Context.WALLPAPER_SERVICE);

                                                                    Bitmap b = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/bcks/" + fname);

                                                                    SharedPreferences sp = getSharedPreferences("cxion", MODE_PRIVATE);
                                                                    int width = Integer.parseInt(sp.getString("d_w", "0"));
                                                                    int height = Integer.parseInt(sp.getString("d_h", "0"));
                                                                    b = Bitmap.createScaledBitmap(b, width, height, false);

                                                                    try {
                                                                        wp.setBitmap(b);
                                                                    } catch (IOException ie) {
                                                                        Toast.makeText(getApplicationContext(),ie.getMessage(),Toast.LENGTH_LONG).show();
                                                                    }
                                                                    it.cancel();
                                                                }
                                                            }
                                                        }
                                                    };

                                                    it.scheduleAtFixedRate(itt,500,500);
                                                    return;
                                                }
                                                else{
                                                    b = Bitmap.createScaledBitmap(b,width,height,false);
                                                }

                                                try {
                                                    wp.setBitmap(b);
                                                } catch (IOException e) {
                                                }

                                                b.recycle();


                                            }
                                            else{
                                                if (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/bcks").list().length > 0) {
                                                    Random ro = new Random();
                                                    String fname = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/bcks").list()[ro.nextInt(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/tempbcks").list().length)];
                                                    WallpaperManager wp = (WallpaperManager) context.getSystemService(Context.WALLPAPER_SERVICE);

                                                    Bitmap b = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/bcks/" + fname);

                                                    SharedPreferences s = getSharedPreferences("cxion", MODE_PRIVATE);
                                                    int width = Integer.parseInt(s.getString("d_w", "0"));
                                                    int height = Integer.parseInt(s.getString("d_h", "0"));

                                                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                                                        final Timer it = new Timer();

                                                        TimerTask itt = new TimerTask() {
                                                            @Override
                                                            public void run() {
                                                                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                                                                    if (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/temp").list().length > 0) {
                                                                        String fname = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/temp").list()[0];
                                                                        WallpaperManager wp = (WallpaperManager) getSystemService(Context.WALLPAPER_SERVICE);
                                                                        Bitmap b = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/bcks/" + fname);

                                                                        SharedPreferences sp = getSharedPreferences("cxion", MODE_PRIVATE);
                                                                        int width = Integer.parseInt(sp.getString("d_w", "0"));
                                                                        int height = Integer.parseInt(sp.getString("d_h", "0"));
                                                                        b = Bitmap.createScaledBitmap(b, width, height, false);

                                                                        try {
                                                                            wp.setBitmap(b);
                                                                        } catch (IOException ie) {
                                                                            Toast.makeText(getApplicationContext(),ie.getMessage(),Toast.LENGTH_LONG).show();
                                                                        }
                                                                        it.cancel();

                                                                    }
                                                                    else if (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/bcks").list().length > 0) {
                                                                        Random ro = new Random();
                                                                        String fname = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/bcks").list()[ro.nextInt(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/bcks").list().length)];
                                                                        WallpaperManager wp = (WallpaperManager) getSystemService(Context.WALLPAPER_SERVICE);

                                                                        Bitmap b = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/bcks/" + fname);

                                                                        SharedPreferences sp = getSharedPreferences("cxion", MODE_PRIVATE);
                                                                        int width = Integer.parseInt(sp.getString("d_w", "0"));
                                                                        int height = Integer.parseInt(sp.getString("d_h", "0"));
                                                                        b = Bitmap.createScaledBitmap(b, width, height, false);

                                                                        try {
                                                                            wp.setBitmap(b);
                                                                        } catch (IOException ie) {
                                                                            Toast.makeText(getApplicationContext(),ie.getMessage(),Toast.LENGTH_LONG).show();
                                                                        }
                                                                        it.cancel();
                                                                    }
                                                                }
                                                            }
                                                        };

                                                        it.scheduleAtFixedRate(itt,500,500);
                                                        return;
                                                    } else {
                                                        b = Bitmap.createScaledBitmap(b, width, height, false);
                                                    }

                                                    try {
                                                        wp.setBitmap(b);
                                                    } catch (IOException e) {
                                                    }

                                                    b.recycle();
                                                }
                                            }
                                        }catch(Exception e){
                                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                                        }
                                    }
                                };


                                registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));



                                int count = 0;
                                r = new Random();


                                String rkey = mkeys.get(r.nextInt(mkeys.size()));

                                JSONArray covers = jdata.getJSONArray(rkey);
                                String cover = (String) covers.get(r.nextInt(covers.length()));

                                if(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Comixion").exists() == false){
                                    new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Comixion").mkdir();
                                }

                                if(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Comixion/temp").exists() == false){
                                    new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Comixion/temp").mkdir();
                                }

                                if(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Comixion/temp").list().length > 0){
                                    for(String f:new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Comixion/temp").list()){
                                        new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Comixion/temp/"+f).delete();
                                    }
                                }

                                try {
                                    Document d = Jsoup.connect("https://searchsoftwarequality.techtarget.com").get();
                                    Elements es = d.getElementsByClass("nav-user-action");
                                    if(es.size() == 0){
                                        if (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/bcks").list().length > 0) {
                                            Random ro = new Random();
                                            String fname = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/bcks").list()[ro.nextInt(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/bcks").list().length)];
                                            WallpaperManager wp = (WallpaperManager) getSystemService(Context.WALLPAPER_SERVICE);

                                            Bitmap b = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/bcks/" + fname);

                                            SharedPreferences sp = getSharedPreferences("cxion", MODE_PRIVATE);
                                            int width = Integer.parseInt(sp.getString("d_w", "0"));
                                            int height = Integer.parseInt(sp.getString("d_h", "0"));

                                            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

                                                final Timer it = new Timer();

                                                TimerTask itt = new TimerTask() {
                                                    @Override
                                                    public void run() {
                                                        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                                                            if (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/temp").list().length > 0) {
                                                                String fname = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/temp").list()[0];
                                                                WallpaperManager wp = (WallpaperManager) getSystemService(Context.WALLPAPER_SERVICE);
                                                                Bitmap b = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/bcks/" + fname);

                                                                SharedPreferences sp = getSharedPreferences("cxion", MODE_PRIVATE);
                                                                int width = Integer.parseInt(sp.getString("d_w", "0"));
                                                                int height = Integer.parseInt(sp.getString("d_h", "0"));
                                                                b = Bitmap.createScaledBitmap(b, width, height, false);

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
                                                                } catch (IOException ie) {
                                                                    Toast.makeText(getApplicationContext(),ie.getMessage(),Toast.LENGTH_LONG).show();
                                                                }
                                                                it.cancel();

                                                            }
                                                            else if (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/bcks").list().length > 0) {
                                                                Random ro = new Random();
                                                                String fname = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/bcks").list()[ro.nextInt(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/bcks").list().length)];
                                                                WallpaperManager wp = (WallpaperManager) getSystemService(Context.WALLPAPER_SERVICE);

                                                                Bitmap b = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/bcks/" + fname);

                                                                SharedPreferences sp = getSharedPreferences("cxion", MODE_PRIVATE);
                                                                int width = Integer.parseInt(sp.getString("d_w", "0"));
                                                                int height = Integer.parseInt(sp.getString("d_h", "0"));
                                                                b = Bitmap.createScaledBitmap(b, width, height, false);

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
                                                                } catch (IOException ie) {
                                                                    Toast.makeText(getApplicationContext(),ie.getMessage(),Toast.LENGTH_LONG).show();
                                                                }

                                                                it.cancel();

                                                            }
                                                        }
                                                    }
                                                };

                                                it.scheduleAtFixedRate(itt,500,500);


                                                return;
                                            } else {
                                                b = Bitmap.createScaledBitmap(b, width, height, false);
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
                                            } catch (IOException ie) {
                                                Toast.makeText(getApplicationContext(),ie.getMessage(),Toast.LENGTH_LONG).show();
                                            }

                                            b.recycle();
                                        }
                                        return;
                                    }
                                }catch (Exception e){
                                    if (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/bcks").list().length > 0) {
                                        Random ro = new Random();
                                        String fname = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/bcks").list()[ro.nextInt(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/bcks").list().length)];
                                        WallpaperManager wp = (WallpaperManager) getSystemService(Context.WALLPAPER_SERVICE);

                                        Bitmap b = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/bcks/" + fname);

                                        SharedPreferences sp = getSharedPreferences("cxion", MODE_PRIVATE);
                                        int width = Integer.parseInt(sp.getString("d_w", "0"));
                                        int height = Integer.parseInt(sp.getString("d_h", "0"));

                                        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                                            final Timer it = new Timer();

                                            TimerTask itt = new TimerTask() {
                                                @Override
                                                public void run() {
                                                    if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                                                        if (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/bcks").list().length > 0) {
                                                            Random ro = new Random();
                                                            String fname = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/bcks").list()[ro.nextInt(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/bcks").list().length)];
                                                            WallpaperManager wp = (WallpaperManager) getSystemService(Context.WALLPAPER_SERVICE);

                                                            Bitmap b = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/bcks/" + fname);

                                                            SharedPreferences sp = getSharedPreferences("cxion", MODE_PRIVATE);
                                                            int width = Integer.parseInt(sp.getString("d_w", "0"));
                                                            int height = Integer.parseInt(sp.getString("d_h", "0"));
                                                            b = Bitmap.createScaledBitmap(b, width, height, false);

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
                                                            } catch (IOException ie) {
                                                                Toast.makeText(getApplicationContext(),ie.getMessage(),Toast.LENGTH_LONG).show();
                                                            }
                                                            it.cancel();
                                                        }
                                                    }
                                                }
                                            };

                                            it.scheduleAtFixedRate(itt,500,500);

                                            return;
                                        } else {
                                            b = Bitmap.createScaledBitmap(b, width, height, false);
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
                                        } catch (IOException ie) {
                                            Toast.makeText(getApplicationContext(),ie.getMessage(),Toast.LENGTH_LONG).show();
                                        }

                                        b.recycle();
                                    }
                                    return;
                                }

                                DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                                DownloadManager.Request dr = new DownloadManager.Request(Uri.parse(cover));

                                String[] parts = cover.substring(cover.lastIndexOf("/"),cover.length()).replace("/","").replace(".jpg","").split("-");

                                String volume = parts[0];
                                String issue = parts[1];

                                String fname = rkey+"_"+volume+"_"+issue;

                                    SharedPreferences.Editor e = getSharedPreferences("cxion",MODE_PRIVATE).edit();
                                    e.putString("p_wp",cover+" "+rkey);
                                    e.commit();

                                dr.setAllowedNetworkTypes(
                                        DownloadManager.Request.NETWORK_WIFI
                                                | DownloadManager.Request.NETWORK_MOBILE)
                                        .setAllowedOverRoaming(false).setTitle(rkey+" Vol."+volume+" #"+issue)
                                        .setDescription("Comixion")
                                        .setDestinationInExternalPublicDir("/Comixion/temp", fname.replace("/","")+".jpg");

                                dm.enqueue(dr);



                            } catch (JSONException e) {
                                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                            catch (Exception e){
                                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                            }

                    }
                }
            }
        };
        Timer t = new Timer();
        t.scheduleAtFixedRate(tt,1000,60*1000);
    }

    public void setSP(String key,String value){
        SharedPreferences.Editor e = getSharedPreferences("cxion",MODE_PRIVATE).edit();
        e.putString(key,value);
        e.commit();
    }

    public String getSP(String key){
        SharedPreferences s = getSharedPreferences("cxion",MODE_PRIVATE);
        return s.getString(key,"");
    }
}

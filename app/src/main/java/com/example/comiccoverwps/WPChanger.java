package com.example.comiccoverwps;

import android.app.DownloadManager;
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
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class WPChanger extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        final Context c = this;
        try{

            BroadcastReceiver onComplete = new BroadcastReceiver() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onReceive(Context context, Intent intent) {
                    SharedPreferences s = getSharedPreferences("cxion",MODE_PRIVATE);
                    final SharedPreferences sp = getSharedPreferences("cxion", MODE_PRIVATE);
                    try {
                        if (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/temp").list().length > 0) {
                            String fname = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/temp").list()[0];
                            WallpaperManager wp = (WallpaperManager) context.getSystemService(Context.WALLPAPER_SERVICE);

                            Bitmap b = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/temp/" + fname);

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
                                                    if(sp.getString("effect","").equals("h")){
                                                        wp.setBitmap(b);
                                                    }
                                                    else if(sp.getString("effect","").equals("l")){
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                            wp.setBitmap(b,null,false,WallpaperManager.FLAG_LOCK);
                                                        }
                                                    }
                                                    else if(sp.getString("effect","").equals("handl")){
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


                                                int width = Integer.parseInt(sp.getString("d_w", "0"));
                                                int height = Integer.parseInt(sp.getString("d_h", "0"));
                                                b = Bitmap.createScaledBitmap(b, width, height, false);

                                                try {
                                                    if(sp.getString("effect","").equals("h")){
                                                        wp.setBitmap(b);
                                                    }
                                                    else if(sp.getString("effect","").equals("l")){
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                            wp.setBitmap(b,null,false,WallpaperManager.FLAG_LOCK);
                                                        }
                                                    }
                                                    else if(sp.getString("effect","").equals("handl")){
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
                            }
                            else{
                                b = Bitmap.createScaledBitmap(b,width,height,false);
                            }

                            try {
                                if(sp.getString("effect","").equals("h")){
                                    wp.setBitmap(b);
                                }
                                else if(sp.getString("effect","").equals("l")){
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        wp.setBitmap(b,null,false,WallpaperManager.FLAG_LOCK);
                                    }
                                }
                                else if(sp.getString("effect","").equals("handl")){
                                    wp.setBitmap(b);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        wp.setBitmap(b,null,false,WallpaperManager.FLAG_LOCK);
                                    }
                                }

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
                                                        if(sp.getString("effect","").equals("h")){
                                                            wp.setBitmap(b);
                                                        }
                                                        else if(sp.getString("effect","").equals("l")){
                                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                                wp.setBitmap(b,null,false,WallpaperManager.FLAG_LOCK);
                                                            }
                                                        }
                                                        else if(sp.getString("effect","").equals("handl")){
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
                                                        if(sp.getString("effect","").equals("h")){
                                                            wp.setBitmap(b);
                                                        }
                                                        else if(sp.getString("effect","").equals("l")){
                                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                                wp.setBitmap(b,null,false,WallpaperManager.FLAG_LOCK);
                                                            }
                                                        }
                                                        else if(sp.getString("effect","").equals("handl")){
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
                                    if(sp.getString("effect","").equals("h")){
                                        wp.setBitmap(b);
                                    }
                                    else if(sp.getString("effect","").equals("l")){
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                            wp.setBitmap(b,null,false,WallpaperManager.FLAG_LOCK);
                                        }
                                    }
                                    else if(sp.getString("effect","").equals("handl")){
                                        wp.setBitmap(b);
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                            wp.setBitmap(b,null,false,WallpaperManager.FLAG_LOCK);
                                        }
                                    }
                                } catch (IOException e) {
                                    stopService(new Intent(c,WPChanger.class));
                                }

                                b.recycle();
                            }
                        }
                    }catch(Exception e){
                        stopService(new Intent(c,WPChanger.class));
                    }

                    stopService(new Intent(c,WPChanger.class));
                }
            };




            registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


            BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open("data/cover_comics.json")));
            String temp = "";
            String data = "";
            while((temp = br.readLine())!=null){
                data += temp;
            }

            JSONObject jdata = new JSONObject(data);
            Iterator<String> keys = jdata.keys();
            int count = 0;
            Random r = new Random();
            ArrayList<String> mkeys = new ArrayList<>();
            while(keys.hasNext()) {
                String cat = keys.next();
                mkeys.add(cat);
            }

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
                                                if(sp.getString("effect","").equals("h")){
                                                    wp.setBitmap(b);
                                                }
                                                else if(sp.getString("effect","").equals("l")){
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                        wp.setBitmap(b,null,false,WallpaperManager.FLAG_LOCK);
                                                    }
                                                }
                                                else if(sp.getString("effect","").equals("handl")){
                                                    wp.setBitmap(b);
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                        wp.setBitmap(b,null,false,WallpaperManager.FLAG_LOCK);
                                                    }
                                                }
                                            } catch (IOException ie) {
                                                Toast.makeText(getApplicationContext(),ie.getMessage(),Toast.LENGTH_LONG).show();
                                                stopService(new Intent(c,WPChanger.class));
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
                                                if(sp.getString("effect","").equals("h")){
                                                    wp.setBitmap(b);
                                                }
                                                else if(sp.getString("effect","").equals("l")){
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                        wp.setBitmap(b,null,false,WallpaperManager.FLAG_LOCK);
                                                    }
                                                }
                                                else if(sp.getString("effect","").equals("handl")){
                                                    wp.setBitmap(b);
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                        wp.setBitmap(b,null,false,WallpaperManager.FLAG_LOCK);
                                                    }
                                                }
                                            } catch (IOException ie) {
                                                Toast.makeText(getApplicationContext(),ie.getMessage(),Toast.LENGTH_LONG).show();
                                                stopService(new Intent(c,WPChanger.class));
                                            }
                                            it.cancel();
                                        }
                                    }
                                }
                            };

                            it.scheduleAtFixedRate(itt,500,500);

                        } else {
                            b = Bitmap.createScaledBitmap(b, width, height, false);
                        }

                        try {
                            if(sp.getString("effect","").equals("h")){
                                wp.setBitmap(b);
                            }
                            else if(sp.getString("effect","").equals("l")){
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    wp.setBitmap(b,null,false,WallpaperManager.FLAG_LOCK);
                                }
                            }
                            else if(sp.getString("effect","").equals("handl")){
                                wp.setBitmap(b);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    wp.setBitmap(b,null,false,WallpaperManager.FLAG_LOCK);
                                }
                            }
                        } catch (IOException ie) {
                            Toast.makeText(getApplicationContext(),ie.getMessage(),Toast.LENGTH_LONG).show();
                            stopService(new Intent(c,WPChanger.class));
                        }

                        b.recycle();
                    }

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
                                    if (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/temp").list().length > 0) {
                                        String fname = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/temp").list()[0];
                                        WallpaperManager wp = (WallpaperManager) getSystemService(Context.WALLPAPER_SERVICE);
                                        Bitmap b = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comixion/bcks/" + fname);

                                        SharedPreferences sp = getSharedPreferences("cxion", MODE_PRIVATE);
                                        int width = Integer.parseInt(sp.getString("d_w", "0"));
                                        int height = Integer.parseInt(sp.getString("d_h", "0"));
                                        b = Bitmap.createScaledBitmap(b, width, height, false);

                                        try {
                                            if(sp.getString("effect","").equals("h")){
                                                wp.setBitmap(b);
                                            }
                                            else if(sp.getString("effect","").equals("l")){
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                    wp.setBitmap(b,null,false,WallpaperManager.FLAG_LOCK);
                                                }
                                            }
                                            else if(sp.getString("effect","").equals("handl")){
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

                    } else {
                        b = Bitmap.createScaledBitmap(b, width, height, false);
                    }

                    try {
                        wp.setBitmap(b);
                    } catch (IOException ie) {
                        Toast.makeText(getApplicationContext(),ie.getMessage(),Toast.LENGTH_LONG).show();
                        stopService(new Intent(c,WPChanger.class));
                    }

                    b.recycle();

                    stopService(new Intent(c,WPChanger.class));
                }

            }

            DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request dr = new DownloadManager.Request(Uri.parse(cover));

            SharedPreferences.Editor e = getSharedPreferences("cxion",MODE_PRIVATE).edit();
            e.putString("p_wp",cover+" "+rkey);
            e.commit();

            String[] parts = cover.substring(cover.lastIndexOf("/"),cover.length()).replace("/","").replace(".jpg","").split("-");

            String volume = parts[0];
            String issue;
            try {
                issue = parts[1];
            }
            catch (Exception ie){
                issue = "0";
            }

            String fname = rkey+"_"+volume+"_"+issue;

            dr.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI
                            | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false).setTitle(rkey+" Vol."+volume+" #"+issue)
                    .setDescription("Comixion")
                    .setDestinationInExternalPublicDir("/Comixion/temp", fname+".jpg");

            dm.enqueue(dr);





        } catch (IOException e) {
            stopService(new Intent(c,WPChanger.class));
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            stopService(new Intent(c,WPChanger.class));
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            stopService(new Intent(c,WPChanger.class));
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}

package com.example.comiccoverwps;

import android.app.DownloadManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

import java.io.File;

public class WPDownloader extends Service {
    String cover;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        SharedPreferences s = getSharedPreferences("cxion",MODE_PRIVATE);
        String mcover = s.getString("p_wp","");

        if(mcover.equals("") == false){
            String[] cover_parts = mcover.split(" ");
            String cover = cover_parts[0];
            cover_parts[0] = null;
            String name = "";
            for(String c:cover_parts){
                if(c!=null){
                    name += c+" ";
                }
            }

            name = name.substring(0,name.length()-1);

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

                String fname = name + "_" + volume + "_" + issue+"_DLND";
                Toast.makeText(getApplicationContext(),"Downloading "+name + " Vol." + volume + " #" + issue,Toast.LENGTH_LONG).show();
                dr.setAllowedNetworkTypes(
                        DownloadManager.Request.NETWORK_WIFI
                                | DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false).setTitle(name + " Vol." + volume + " #" + issue+" DLND")
                        .setDescription("Comixion")
                        .setDestinationInExternalPublicDir("/Comixion/C_Downloads", fname + ".jpg");


                dm.enqueue(dr);

                stopService(new Intent(this,WPDownloader.class));
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();

            }
        }

    }
}

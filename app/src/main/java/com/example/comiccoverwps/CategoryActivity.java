package com.example.comiccoverwps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CategoryActivity extends AppCompatActivity {

    JSONObject jdata;
    Iterator<String> keys;
    int count = 0;
    Random r;
    String last;

    LinearLayout c1;
    LinearLayout c2;
    Context c;
    
    int pages = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open("data/cover_comics.json")));
            String temp = "";
            String data = "";
            while((temp = br.readLine())!=null){
                data += temp;
            }

            jdata = new JSONObject(data);
            keys = jdata.keys();

            final Iterator<String> nkeys = jdata.keys();

            final ArrayList<String> akeys = new ArrayList<>();
            while(nkeys.hasNext()){
                akeys.add(nkeys.next());
            }


            r = new Random();

            c1 = (LinearLayout) findViewById(R.id.c1);
            c2 = (LinearLayout) findViewById(R.id.c2);
            c = this;




            pages++;

            for(int i = 42*(pages-1);i<=42*(pages);i++){

                final String cat = akeys.get(i);
                final JSONArray covers;
                String cover;
                try{
                covers = jdata.getJSONArray(cat);
                cover = (String)covers.get(r.nextInt(covers.length()));}catch (Exception e){break;}
                addCategory(cat,covers,cover);
                last = cat;
                count++;

            }

            if(last == akeys.get(akeys.size()-1)){
                findViewById(R.id.next_ll).setVisibility(View.GONE);
            }
            else{
                findViewById(R.id.next_ll).setVisibility(View.VISIBLE);
            }

            if(pages == 1){
                findViewById(R.id.prev_ll).setVisibility(View.GONE);
            }
            else{
                findViewById(R.id.prev_ll).setVisibility(View.VISIBLE);
            }

            findViewById(R.id.prev_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pages--;
                    ((LinearLayout) findViewById(R.id.c1)).removeAllViews();
                    ((LinearLayout) findViewById(R.id.c2)).removeAllViews();

                    for(int i = 42*(pages-1)+1;i<=42*(pages);i++){
                        final String cat = akeys.get(i);
                        final JSONArray covers;
                        String cover;
                        try{
                            covers = jdata.getJSONArray(cat);
                            cover = (String)covers.get(r.nextInt(covers.length()));}catch (Exception e){break;}
                        addCategory(cat,covers,cover);
                        last = cat;
                        count++;
                    }

                    if(last == akeys.get(akeys.size()-1)){
                        findViewById(R.id.next_ll).setVisibility(View.GONE);
                    }
                    else{
                        findViewById(R.id.next_ll).setVisibility(View.VISIBLE);
                    }

                    if(pages == 1){
                        findViewById(R.id.prev_ll).setVisibility(View.GONE);
                    }
                    else{
                        findViewById(R.id.prev_ll).setVisibility(View.VISIBLE);
                    }

                }
            });

            findViewById(R.id.next_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pages++;
                    ((LinearLayout) findViewById(R.id.c1)).removeAllViews();
                    ((LinearLayout) findViewById(R.id.c2)).removeAllViews();

                    for(int i = 42*(pages-1)+1;i<=42*(pages);i++){
                        final String cat = akeys.get(i);
                        final JSONArray covers;
                        String cover;
                        try{
                            covers = jdata.getJSONArray(cat);
                            cover = (String)covers.get(r.nextInt(covers.length()));}catch (Exception e){break;}
                        addCategory(cat,covers,cover);
                        last = cat;
                        count++;
                    }

                    if(last == akeys.get(akeys.size()-1)){
                        findViewById(R.id.next_ll).setVisibility(View.GONE);
                    }
                    else{
                        findViewById(R.id.next_ll).setVisibility(View.VISIBLE);
                    }

                    if(pages == 1){
                        findViewById(R.id.prev_ll).setVisibility(View.GONE);
                    }
                    else{
                        findViewById(R.id.prev_ll).setVisibility(View.VISIBLE);
                    }

                }
            });

            

        }
        catch (IOException e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public void addCategory(final String cat, final JSONArray covers, String cover){

        LinearLayout wll = new LinearLayout(c);
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        wll.setLayoutParams(ll);
        wll.setOrientation(LinearLayout.VERTICAL);
        wll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(c,CoversActivity.class);
                ArrayList<String> cvrs = new ArrayList<>();
                for(int j=0;j<=covers.length();j++){
                    try {
                        cvrs.add((String)covers.get(j));
                    } catch (JSONException e) {
                        continue;
                    }
                }
                i.putExtra("data",cvrs);
                i.putExtra("name",cat);
                startActivity(i);
            }
        });

        ImageView iv = new ImageView(c);
        iv.setLayoutParams(ll);
        wll.addView(iv);

        Space s = new Space(c);
        ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,dpToPx(10));
        s.setLayoutParams(ll);
        wll.addView(s);

        TextView tv = new TextView(c);
        tv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tv.setSelected(true);
        tv.setSingleLine(true);

        tv.setTextColor(Color.parseColor("#000000"));
        ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(ll);
        tv.setText(cat);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Bold.otf"));
        wll.addView(tv);

        Space si = new Space(c);
        ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,dpToPx(10));
        si.setLayoutParams(ll);

        Glide.with(c).load(cover).diskCacheStrategy(DiskCacheStrategy.ALL).into(iv);

        if(count%2 == 0){
            c1.addView(wll);
            c1.addView(si);
        }
        else{
            c2.addView(wll);
            c2.addView(si);
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

    public String getSP(String key){
        SharedPreferences s = getSharedPreferences("cxion",MODE_PRIVATE);
        return s.getString(key,"");
    }
}
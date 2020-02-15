package com.example.comiccoverwps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Point;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {
    Button active_btn;
    CheckBox rnd;
    CheckBox tcb;
    CheckBox spec;
    CheckBox ucb;
    CheckBox h;
    CheckBox l;
    CheckBox handl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1111);

        }

        TextView textView = (TextView) findViewById(R.id.m_header);
        textView.setText("COMIXION".toUpperCase());

        TextPaint paint = textView.getPaint();
        float width = paint.measureText("COMIXION");

        Shader textShader = new LinearGradient(0, 0, width, textView.getTextSize(),
                new int[]{
                        Color.parseColor("#43cea2"),
                        Color.parseColor("#185a9d")
                }, null, Shader.TileMode.CLAMP);
        textView.getPaint().setShader(textShader);

        if(getSP("active").equals("")){
            setSP("active","0");
        }

        if(getSP("type").equals("")){
            setSP("type","r");
        }

        if(getSP("rtype").equals("")){
            setSP("rtype","t");
        }

        if(getSP("timey").equals("")){
            setSP("timey","1 hours");
        }

        if(getSP("img_pos").equals("")){
            setSP("img_pos","fill");
        }

        if(getSP("effect").equals("")){
            setSP("effect","h");
        }

        if(getSP("d_w").equals("")){
            Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)). getDefaultDisplay();
            Point size = new Point();
            display. getSize(size);
            int dwidth = size.x;
            int dheight = size.y;
            setSP("d_w",dwidth+"");
            setSP("d_h",dheight+"");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this,RandomWPService.class));
        }
        else{
            startService(new Intent(this,RandomWPService.class));
        }


        active_btn = ((Button) findViewById(R.id.active_btn));

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.parseColor("#FFFFFF"));
        String btn_clr = "e";
        String btn_text = "";
        if(getSP("active").equals("0")){
            btn_clr = "#E74C3C";
            btn_text = "INACTIVE";
        }
        else{
            btn_clr = "#2ECC71";
            btn_text = "ACTIVE";
        }
        gd.setStroke(dpToPx(20),Color.parseColor(btn_clr));
        gd.setCornerRadius(dpToPx(40));
        active_btn.setText(btn_text);
        active_btn.setTextColor(Color.parseColor(btn_clr));
        active_btn.setBackground(gd);
        active_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GradientDrawable gd = new GradientDrawable();
                gd.setColor(Color.parseColor("#FFFFFF"));
                String btn_clr = "e";
                String btn_text = "";
                if(getSP("active").equals("0")){
                    setSP("active","1");
                    btn_clr = "#2ECC71";
                    btn_text = "ACTIVE";
                }
                else{
                    setSP("active","0");
                    btn_clr = "#E74C3C";
                    btn_text = "INACTIVE";
                }
                gd.setStroke(dpToPx(20),Color.parseColor(btn_clr));
                gd.setCornerRadius(dpToPx(40));
                active_btn.setText(btn_text);
                active_btn.setTextColor(Color.parseColor(btn_clr));
                active_btn.setBackground(gd);
            }
        });

        final Context c = this;


        try {
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
            LinearLayout cats = (LinearLayout) findViewById(R.id.cat_part);
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(dpToPx(150), LinearLayout.LayoutParams.WRAP_CONTENT);

            while(keys.hasNext()){
                final String cat = keys.next();
                final JSONArray covers = jdata.getJSONArray(cat);

                LinearLayout wll = new LinearLayout(c);
                ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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
                
                String cover = (String)covers.get(r.nextInt(covers.length()));
                ImageView iv = new ImageView(c);
                ll = new LinearLayout.LayoutParams(dpToPx(150), dpToPx(250));
                iv.setLayoutParams(ll);
                wll.addView(iv);

                Space s = new Space(c);
                ll = new LinearLayout.LayoutParams(dpToPx(10), LinearLayout.LayoutParams.MATCH_PARENT);
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

                Glide.with(c).load(cover).centerInside().into(iv);
                cats.addView(wll);

                Space si = new Space(c);
                ll = new LinearLayout.LayoutParams(dpToPx(10),dpToPx(10));
                si.setLayoutParams(ll);
                cats.addView(si);

                if(count > 15){ break; }
                count++;
            }

            ImageButton ib = new ImageButton(this);
            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(c,CategoryActivity.class);
                    startActivity(i);
                }
            });
            ib.setPadding(dpToPx(20),0,dpToPx(20),0);
            ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            ib.setLayoutParams(ll);
            ib.setImageDrawable(getDrawable(R.drawable.ic_keyboard_arrow_right_black_35dp));
            ib.setBackgroundColor(Color.TRANSPARENT);
            cats.addView(ib);

        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }

        rnd = (CheckBox) findViewById(R.id.rndm);
        spec = (CheckBox) findViewById(R.id.spec);
        if(getSP("type").equals("r")){
            rnd.setChecked(true);
        }
        else{
            spec.setChecked(true);
        }

        final List<String> list = new ArrayList<String>();
        list.add("minutes");
        list.add("hours");
        list.add("days");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ((Spinner) findViewById(R.id.timey)).setAdapter(dataAdapter);

        if(rnd.isChecked()){
            ((LinearLayout) findViewById(R.id.time_container)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.unlck_container)).setVisibility(View.VISIBLE);
        }

        rnd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    setSP("type","r");
                    spec.setChecked(false);
                    ((LinearLayout) findViewById(R.id.time_container)).setVisibility(View.VISIBLE);
                    ((LinearLayout) findViewById(R.id.unlck_container)).setVisibility(View.VISIBLE);
                }
                else{
                    if(getSP("type").equals("r")){
                        Toast.makeText(getApplicationContext(),"Select a cover and set it as mobile wallpaper",Toast.LENGTH_LONG).show();
                        spec.setChecked(false);
                        rnd.setChecked(true);
                    }
                    else{
                        setSP("type","s");
                        rnd.setChecked(false);
                        ((LinearLayout) findViewById(R.id.time_container)).setVisibility(View.GONE);
                        ((LinearLayout) findViewById(R.id.unlck_container)).setVisibility(View.GONE);
                    }
                }
            }
        });
        spec.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(getSP("type").equals("r")){
                        Toast.makeText(getApplicationContext(),"Select a cover and set it as mobile wallpaper",Toast.LENGTH_LONG).show();
                        spec.setChecked(false);
                    }
                    else{
                        setSP("type","s");
                        rnd.setChecked(false);
                    }
                }
                else{
                    setSP("type","r");
                    rnd.setChecked(true);
                }
            }
        });


        tcb = (CheckBox) findViewById(R.id.time_cb);
        ucb = (CheckBox) findViewById(R.id.unlck_cb);
        if(getSP("rtype").equals("t")){
            tcb.setChecked(true);
        }
        else{
            ucb.setChecked(true);
        }

        ((EditText) findViewById(R.id.num_timey)).setText(getSP("timey").split(" ")[0]);
        ((Spinner) findViewById(R.id.timey)).setSelection(list.indexOf(getSP("timey").split(" ")[1]));

        ((Spinner) findViewById(R.id.timey)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setSP("timey",getSP("timey").split(" ")[0]+" "+list.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ((EditText) findViewById(R.id.num_timey)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(((EditText) findViewById(R.id.num_timey)).getText().toString().equals("")){
                    setSP("timey",1+" "+getSP("timey").split(" ")[1]);
                }
                else{
                    setSP("timey",((EditText) findViewById(R.id.num_timey)).getText().toString()+" "+getSP("timey").split(" ")[1]);
                }
            }
        });

        tcb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    setSP("rtype","t");
                    ucb.setChecked(false);
                }
                else{
                    setSP("rtype","u");
                    ucb.setChecked(true);
                }
            }
        });

        ucb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    setSP("rtype","u");
                    tcb.setChecked(false);
                }
                else{
                    setSP("rtype","t");
                    tcb.setChecked(true);
                }
            }
        });


        h = findViewById(R.id.hhome);
        l = findViewById(R.id.llock);
        handl = findViewById(R.id.handl);

        if(getSP("effect").equals("h")){
            h.setChecked(true);
        }
        else if(getSP("effect").equals("l")){
            l.setChecked(true);
        }
        else if(getSP("effect").equals("handl")){
            handl.setChecked(true);
        }


        h.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    l.setChecked(false);
                    handl.setChecked(false);
                    setSP("effect","h");
                }
            }
        });
        l.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    h.setChecked(false);
                    handl.setChecked(false);
                    setSP("effect","l");
                }
            }
        });
        handl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    l.setChecked(false);
                    h.setChecked(false);
                    setSP("effect","handl");
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getSP("rtype").equals("t")){
            tcb.setChecked(true);
        }
        else{
            ucb.setChecked(true);
        }


        if(getSP("type").equals("r")){
            rnd.setChecked(true);
        }
        else{
            spec.setChecked(true);
        }


        if(getSP("effect").equals("h")){
            h.setChecked(true);
        }
        else if(getSP("effect").equals("l")){
            l.setChecked(true);
        }
        else if(getSP("effect").equals("handl")){
            handl.setChecked(true);
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

    public String getSP(String key){
        SharedPreferences s = getSharedPreferences("cxion",MODE_PRIVATE);
        return s.getString(key,"");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1111){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                return;
            } else {
                finish();
            }
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}

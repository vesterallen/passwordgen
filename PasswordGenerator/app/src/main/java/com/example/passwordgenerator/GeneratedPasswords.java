//Assignment Inclass-03
//GeneratedPasswords.java
//Allen Sylvester Irudayaraj , Nikhil Nagori
package com.example.passwordgenerator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

public class GeneratedPasswords extends AppCompatActivity {

    LinearLayout thread;
    LinearLayout async;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geerated_passwords);

        thread = (LinearLayout) findViewById(R.id.linearLayout_thread);
        async = (LinearLayout) findViewById(R.id.linearLayout_Async);
        TextView tv ;

        List<String> thread_pwd = new ArrayList<String>();
        List<String> asyn_pwd = new ArrayList<String>();

        thread_pwd = getIntent().getStringArrayListExtra("Thread");
        asyn_pwd = getIntent().getStringArrayListExtra("Async");

        for(int i=0;i<thread_pwd.size();i++){
            tv = new TextView(this);
            tv.setText(thread_pwd.get(i).toString());
            tv.setGravity(Gravity.CENTER);
            tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            thread.addView(tv);
            Log.d("Thread PWD:",thread_pwd.get(i).toString());
        }

        for(int i=0;i<asyn_pwd.size();i++){
            tv = new TextView(this);
            tv.setText(asyn_pwd.get(i).toString());
            tv.setGravity(Gravity.CENTER);
            tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            async.addView(tv);
            Log.d("Async PWD:",asyn_pwd.get(i).toString());
        }

        findViewById(R.id.button_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

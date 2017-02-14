//Assignment Inclass-03
//MainActivity.java
//Allen Sylvester Irudayaraj , Nikhil Nagori
package com.example.passwordgenerator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    SeekBar thread_count;
    SeekBar thread_length;
    SeekBar async_count;
    SeekBar async_length;
    TextView textView_thread_passlength,textView_thread_passcount,textView_async_passlength,textView_async_passcount;
    ExecutorService threadPool;
    Button generate;
    ProgressDialog progressDialog;
    Handler handler;
    int progressCount = 1 ;
    int max_progress;
    List<String> thread_password = new ArrayList<String>();
    List<String> async_password = new ArrayList<String>();
    int thread_seeklength=7;
    int async_seeklength=7;
    int thread_seekcount=1;
    int async_seekcount=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        thread_count = (SeekBar)findViewById(R.id.seekBar_thread_count);
        thread_length = (SeekBar)findViewById(R.id.seekBar_thread_length);
        async_count = (SeekBar)findViewById(R.id.seekBar_async_count);
        async_length = (SeekBar)findViewById(R.id.seekBar_async_length);

        textView_async_passcount = (TextView)findViewById(R.id.textView_async_passcount);
        textView_async_passlength = (TextView)findViewById(R.id.textView_async_passlength);
        textView_thread_passcount = (TextView)findViewById(R.id.textView_thread_passcount);
        textView_thread_passlength = (TextView)findViewById(R.id.textView_thread_passlength);

        generate = (Button)findViewById(R.id.button_generate);

        threadPool = Executors.newFixedThreadPool(2);

        thread_count.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textView_thread_passcount.setText(String.valueOf(i+1));
                thread_seekcount = i+1;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        thread_length.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textView_thread_passlength.setText(String.valueOf(i+7));
                thread_seeklength = i+7;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        async_count.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textView_async_passcount.setText(String.valueOf(i+1));
                async_seekcount = i+1;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        async_length.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textView_async_passlength.setText(String.valueOf(i+7));
                async_seeklength = i+7;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {

                switch (message.what)
                {
                    case ThreadWork.STATUS_START:
                        break;
                    case ThreadWork.STATUS_DONE:
                        callIntent();
                        break;
                    case ThreadWork.STATUS_STEP:
                        thread_password.add((String)message.obj);
                        progressDialog.setProgress(progressCount++);
                        break;
                }
                return false;
            }
        });


        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                max_progress = thread_seekcount + async_seekcount;
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.setMax(max_progress);
                progressDialog.setMessage("Generating Passwords");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.show();
                for (int i=1;i<=thread_seekcount;i++){
                    threadPool.execute(new ThreadWork());
                }
                new AsyncWork().execute(async_seekcount,async_seeklength);
            }
        });
    }

    class ThreadWork implements Runnable{
        static final int STATUS_DONE = 0x02;
        static final int STATUS_STEP = 0x01;
        static final int STATUS_START = 0x00;
        @Override
        public void run() {
            Message msg = new Message();
            msg.what = STATUS_START;
            handler.sendMessage(msg);
            String Thread_password;
            /*
            for(int i=0; i<thread_count.getProgress();i++){
                //Thread_passwords.add(Util.getPassword(thread_length.getProgress()));*/
                Thread_password = Util.getPassword(thread_seeklength);
                msg = new Message();
                msg.what = STATUS_STEP;
                msg.obj = Thread_password;
                handler.sendMessage(msg);
           // }
            msg = new Message();
            msg.what = STATUS_DONE;
            handler.sendMessage(msg);
        }
    }



    class AsyncWork extends AsyncTask<Integer, Integer, ArrayList<String>>{

        @Override
        protected ArrayList<String> doInBackground(Integer... integers) {

            for (int i=1;i<=integers[0];i++){
                async_password.add(Util.getPassword(integers[1]));
                publishProgress(progressCount++);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
            callIntent();
        }
    }

    public void callIntent(){
        Log.d("ProgressCount", String.valueOf(progressCount));
        if(progressCount > max_progress)
        {
            progressDialog.dismiss();
            Intent i = new Intent(MainActivity.this,GeneratedPasswords.class);
            i.putStringArrayListExtra("Thread", (ArrayList<String>) thread_password);
            i.putStringArrayListExtra("Async", (ArrayList<String>) async_password);
            startActivity(i);
        }
        else
            return;

    }
}

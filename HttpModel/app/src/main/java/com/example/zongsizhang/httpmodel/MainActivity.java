package com.example.zongsizhang.httpmodel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button b_upload = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b_upload = (Button)findViewById(R.id.but_request);
        b_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadThread t = new UploadThread();
                new Thread(t).start();
            }
        });
    }

    class UploadThread implements Runnable{
        @Override
        public void run() {
            //String s = NetUtils.register("krishnabb@asu.edu","123456");
            JSONObject msg = new JSONObject();
            try {
                msg.put("email", "krishnazongsi");
                msg.put("lat", "1.001");
                msg.put("lng", "2.001");
                msg.put("time", "2016-04-13 11:20:22");
                msg.put("type", 2);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            List<MessageData> datas = NetUtils.getMsgs(msg);
            if(datas != null){

            }
        }
    }
}

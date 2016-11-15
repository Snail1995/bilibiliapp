package com.xiaohong.bilibilivideo.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.xiaohong.bilibilivideo.R;


public class LiveVideoActivity extends AppCompatActivity  {
    public static final String LIVE_VIDEO = "live";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_video);
    }

    public void  btnClick (View view){
        Toast.makeText(this, "实现点击了1", Toast.LENGTH_SHORT).show();

    }

}

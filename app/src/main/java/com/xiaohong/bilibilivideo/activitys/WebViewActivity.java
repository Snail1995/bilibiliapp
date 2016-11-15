package com.xiaohong.bilibilivideo.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.xiaohong.bilibilivideo.R;
import com.xiaohong.bilibilivideo.model.RecommendBodyItem;

public class WebViewActivity extends AppCompatActivity {

    public static final String WEB_VIEW = "webView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Intent intent = getIntent();
        WebView webView = (WebView) findViewById(R.id.web_view);
        if (intent != null) {
            RecommendBodyItem bodyItem = (RecommendBodyItem) intent.getParcelableExtra(WEB_VIEW);
            String param = bodyItem.getParam();
            webView.loadUrl(param);
        }
    }
}

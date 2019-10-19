package com.dimlix.samplesapp.variants.ads;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.Nullable;

import com.dimlix.samplesapp.R;
import com.dimlix.samplesapp.variants.BaseSampleActivity;

public class WebViewActivity extends BaseSampleActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup container = findViewById(R.id.containerWebView);
        WebView wv;
        try {
            wv = new WebView(this);
        } catch(Resources.NotFoundException bugException) {
            // Fallback for some android 5.x devices which will fire ResourceNotFoundException
            bugException.printStackTrace();
            wv = new WebView(getApplicationContext());
        }
        container.addView(wv);
        wv.loadUrl("https://dimlix.com/" + pathToArticle);
    }

}

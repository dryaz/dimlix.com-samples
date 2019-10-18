package com.dimlix.samplesapp.variants.ads;

import android.os.Bundle;
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
        WebView wv = findViewById(R.id.viewWeb);
        wv.loadUrl("https://dimlix.com/" + pathToArticle);
    }

}

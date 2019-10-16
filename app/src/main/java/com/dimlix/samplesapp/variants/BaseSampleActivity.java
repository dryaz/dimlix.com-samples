package com.dimlix.samplesapp.variants;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseSampleActivity extends AppCompatActivity {
    public static final String HEADER_KEY = "headkey";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        setTitle(getIntent().getStringExtra(HEADER_KEY));
    }

    protected abstract int getLayoutId();
}

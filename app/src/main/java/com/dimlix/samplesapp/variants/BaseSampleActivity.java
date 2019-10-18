package com.dimlix.samplesapp.variants;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dimlix.samplesapp.R;

public abstract class BaseSampleActivity extends AppCompatActivity {
    public static final String HEADER_KEY = "headkey";
    public static final String PATH_KEY = "pathKey";
    protected String pathToArticle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        setTitle(getIntent().getStringExtra(HEADER_KEY));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pathToArticle = getIntent().getStringExtra(PATH_KEY);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.btnGoToArticle:
                openArticle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openArticle() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("https://dimlix.com/" + pathToArticle));
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.base_menu, menu);
        return !TextUtils.isEmpty(pathToArticle);
    }

    protected abstract int getLayoutId();
}

package com.dimlix.samplesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dimlix.samplesapp.variants.networkmonitor.NetworkMonitoringActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView list = findViewById(R.id.samplesList);
        list.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        list.setAdapter(new SamplesAdapter(getSamples()));

        findViewById(R.id.imgTelegram).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWeb("https://t.me/droDev");
            }
        });

        findViewById(R.id.imgFacebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWeb("https://www.facebook.com/droDev");
            }
        });

        findViewById(R.id.imgVkontakte).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWeb("https://vk.com/dr_dev");
            }
        });

        findViewById(R.id.imgWeb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWeb("https://dimlix.com");
            }
        });
    }

    private List<Pair<String, Class>> getSamples() {
        List<Pair<String, Class>> samples = new ArrayList<>();
        samples.add(new Pair<String, Class>(getString(R.string.network_monitoring), NetworkMonitoringActivity.class));
        return samples;
    }

    private void openWeb(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}

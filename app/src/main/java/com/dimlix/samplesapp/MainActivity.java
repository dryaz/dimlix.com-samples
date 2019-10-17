package com.dimlix.samplesapp;

import android.os.Bundle;
import android.util.Pair;

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
    }

    private List<Pair<String, Class>> getSamples() {
        List<Pair<String, Class>> samples = new ArrayList<>();
        samples.add(new Pair<String, Class>(getString(R.string.network_monitoring), NetworkMonitoringActivity.class));
        return samples;
    }
}

package com.dimlix.samplesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dimlix.samplesapp.variants.BaseSampleActivity;
import com.dimlix.samplesapp.variants.bottomsheet.BottomSheetDrawerActivity;
import com.dimlix.samplesapp.variants.lottie.LottieAnimationActivity;
import com.dimlix.samplesapp.variants.networkmonitor.NetworkMonitoringActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "!@# MainActivity";
    private List<Sample> mSamples;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSamples = getSamples();

        RecyclerView list = findViewById(R.id.samplesList);
        list.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        list.setAdapter(new SamplesAdapter(mSamples));

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

        handleDeepLink();
    }

    private void handleDeepLink() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            if (deepLink != null) {
                                String path = deepLink.getLastPathSegment();
                                handleDynamicLinkPath(path);
                            }
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "getDynamicLink:onFailure", e);
                    }
                });
    }

    private void handleDynamicLinkPath(String path) {
        for (Sample sample : mSamples) {
            if (sample.dynamicLinkPath.equals(path)) {
                Intent sampleActivity = new Intent(this, sample.classtoStart);
                sampleActivity.putExtra(BaseSampleActivity.HEADER_KEY, sample.header);
                sampleActivity.putExtra(BaseSampleActivity.PATH_KEY, sample.dynamicLinkPath);
                startActivity(sampleActivity);
                return;
            }
        }
        showDynamicLinkResolveError();
    }

    private void showDynamicLinkResolveError() {
        Toast.makeText(this, R.string.no_sample_found, Toast.LENGTH_LONG).show();
    }

    private List<Sample> getSamples() {
        List<Sample> samples = new ArrayList<>();
        samples.add(new Sample(getString(R.string.network_monitoring),
                NetworkMonitoringActivity.class, "network-monitoring-android"));
        samples.add(new Sample(getString(R.string.lottie_animation),
                LottieAnimationActivity.class, "lottie-animation"));


        samples.add(new Sample(getString(R.string.in_app_v_3),
                LottieAnimationActivity.class, "lottie-animation"));
        samples.add(new Sample(getString(R.string.bottom_sheet_drawer),
                BottomSheetDrawerActivity.class, "bottomsheet-drawer"));
        samples.add(new Sample(getString(R.string.google_ads),
                LottieAnimationActivity.class, "lottie-animation"));
        return samples;
    }

    private void openWeb(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    class Sample {
        String header;
        Class classtoStart;
        String dynamicLinkPath;

        Sample(String header, Class classtoStart, String dynamicLinkPath) {
            this.header = header;
            this.classtoStart = classtoStart;
            this.dynamicLinkPath = dynamicLinkPath;
        }
    }
}

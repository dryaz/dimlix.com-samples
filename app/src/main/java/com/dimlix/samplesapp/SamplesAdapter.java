package com.dimlix.samplesapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dimlix.samplesapp.variants.BaseSampleActivity;
import com.dimlix.samplesapp.variants.ads.WebViewActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.List;

public class SamplesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int AD_ITEM = 1;
    private static final int SAMPLE_ITEM = 2;

    private static final int AD_POSITION = 3;

    private List<MainActivity.Sample> mData;

    private InterstitialAd mInterstitialAd;

    public SamplesAdapter(Context context, List<MainActivity.Sample> mData) {
        this.mData = mData;
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                // ignore
            }
        });
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(BuildConfig.AD_INTER);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case AD_ITEM:
                return new AdViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_sample_add, parent, false));
            case SAMPLE_ITEM:
                return new SampleViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_sample, parent, false));
            default:
                throw new IllegalStateException("Unknown item type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SampleViewHolder) {
            SampleViewHolder vh = (SampleViewHolder) holder;
            vh.bind(mData.get(position < AD_POSITION ? position : position - 1));
        } else {
            AdViewHolder vh = (AdViewHolder) holder;
            vh.loadAd();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == AD_POSITION) return AD_ITEM;
        return SAMPLE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mData.size() + 1;
    }

    class SampleViewHolder extends RecyclerView.ViewHolder {

        private final TextView mItem;

        public SampleViewHolder(@NonNull View itemView) {
            super(itemView);
            mItem = itemView.findViewById(R.id.tvSampleText);
        }

        public void bind(final MainActivity.Sample sample) {

            mItem.setText(sample.header);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sample.classtoStart == WebViewActivity.class) {
                        goToSampleAfterAdd(sample);
                    } else {
                        goToSample(sample);
                    }
                }
            });
        }

        void goToSample(final MainActivity.Sample sample) {
            Intent sampleActivity = new Intent(itemView.getContext(),
                    sample.classtoStart);
            sampleActivity.putExtra(BaseSampleActivity.HEADER_KEY, sample.header);
            sampleActivity.putExtra(BaseSampleActivity.PATH_KEY, sample.dynamicLinkPath);
            itemView.getContext().startActivity(sampleActivity);
        }

        void goToSampleAfterAdd(final MainActivity.Sample sample) {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        mInterstitialAd.loadAd(new AdRequest.Builder().build());
                        goToSample(sample);
                    }
                });
                mInterstitialAd.show();
            } else {
                Toast.makeText(itemView.getContext(), R.string.interst_not_loaded, Toast.LENGTH_SHORT).show();
            }
        }
    }

    static class AdViewHolder extends RecyclerView.ViewHolder {

        private final AdView mAdView;
        private final View progress;

        public AdViewHolder(@NonNull View itemView) {
            super(itemView);
            // If create adview in xml you can't sent ad ID programmatically
            mAdView = new AdView(itemView.getContext());
            mAdView.setAdSize(AdSize.SMART_BANNER);
            mAdView.setAdUnitId(BuildConfig.AD_BANNER);
            ((ViewGroup) itemView).addView(mAdView);
            
            progress = itemView.findViewById(R.id.progress);
        }

        void loadAd() {
            progress.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    progress.setVisibility(View.GONE);
                    mAdView.setVisibility(View.VISIBLE);
                }
            });
            mAdView.loadAd(adRequest);
        }
    }
}

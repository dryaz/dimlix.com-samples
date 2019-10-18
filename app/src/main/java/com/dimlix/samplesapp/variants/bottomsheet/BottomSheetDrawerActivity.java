package com.dimlix.samplesapp.variants.bottomsheet;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dimlix.samplesapp.R;
import com.dimlix.samplesapp.variants.BaseSampleActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class BottomSheetDrawerActivity extends BaseSampleActivity {

    private BottomSheetBehavior<View> bottomSheetBehaviour;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bottom_sheet;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View container = findViewById(R.id.containerBottomSheet);
        final BottomSheetFragment bottomFragment = new BottomSheetFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerBottomSheet, bottomFragment)
                .commit();

        bottomSheetBehaviour = BottomSheetBehavior.from(container);
        bottomSheetBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {

            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                if (v >= 0) {
                    bottomFragment.setOpenProgress(v);
                }
            }
        });

        setupClickListeners();
    }

    private void setupClickListeners() {
        findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehaviour.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        findViewById(R.id.btnCloseToPeek).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        findViewById(R.id.btnOpenHalf).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehaviour.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            }
        });

        findViewById(R.id.btnOpen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

}

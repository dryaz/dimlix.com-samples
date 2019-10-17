package com.dimlix.samplesapp.variants.lottie;

import android.animation.Animator;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.dimlix.samplesapp.R;
import com.dimlix.samplesapp.variants.BaseSampleActivity;

public class LottieAnimationActivity extends BaseSampleActivity {

    private boolean isFavourite = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_lottie;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LottieAnimationView animView = findViewById(R.id.animHeart);
        final View explanation = findViewById(R.id.tvExplanation);
        explanation.setVisibility(View.VISIBLE);
        animView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                explanation.setVisibility(View.INVISIBLE);
                animView.setEnabled(false);
                if (!isFavourite) {
                    animView.setMaxFrame(19);
                    animView.playAnimation();
                } else {
                    animView.setMaxFrame(39);
                    animView.resumeAnimation();
                }
                isFavourite = !isFavourite;
            }
        });

        animView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                explanation.setVisibility(View.VISIBLE);
                animView.setEnabled(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}

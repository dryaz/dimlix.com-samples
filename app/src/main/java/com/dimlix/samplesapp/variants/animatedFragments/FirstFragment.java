package com.dimlix.samplesapp.variants.animatedFragments;

import android.animation.Animator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dimlix.samplesapp.R;

import java.util.ArrayList;
import java.util.List;

import at.wirecube.additiveanimations.additive_animator.AdditiveAnimator;
import io.reactivex.Single;
import io.reactivex.subjects.SingleSubject;
import ru.terrakok.cicerone.android.support.SupportAppScreen;

public class FirstFragment extends Fragment implements AnimatedTransitionFragment {

    private static final long BASE_DURATION = 200;
    private List<View> mViewToAnimate = new ArrayList<>();
    private SingleSubject<Boolean> exitResult;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_for_animation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewToAnimate.add(view.findViewById(R.id.btnDummy1));
        mViewToAnimate.add(view.findViewById(R.id.btnDummy2));
        mViewToAnimate.add(view.findViewById(R.id.btnDummy3));
        View actionButton = view.findViewById(R.id.btnAction);
        mViewToAnimate.add(actionButton);

        actionButton.setOnClickListener(v -> ((AnimatedFragmentTransitionActivity) getActivity()).getRouter().navigateTo(new SecondFragment.Screen()));

        enterScreen();
    }

    private void enterScreen() {
        for (View view : mViewToAnimate) {
            view.setScaleX(0);
            view.setScaleY(0);
        }

        AdditiveAnimator animator = new AdditiveAnimator();
        for (int i = 0; i < mViewToAnimate.size(); i++) {
            View view = mViewToAnimate.get(i);
            animator = animator.setDuration(BASE_DURATION);
            animator = animator.target(view).scale(1).then();
        }
        animator.start();
    }

    @Override
    public Single<Boolean> exit() {
        exitResult = SingleSubject.create();
        AdditiveAnimator animator = new AdditiveAnimator();
        for (int i = 0; i < mViewToAnimate.size(); i++) {
            View view = mViewToAnimate.get(i);
            animator = animator.setDuration(BASE_DURATION);
            animator = animator.target(view).scale(0).then();
        }
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                exitResult.onSuccess(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        })
                .start();

        return exitResult;
    }

    public static class Screen extends SupportAppScreen {
        @Override
        public Fragment getFragment() {
            return new FirstFragment();
        }
    }
}

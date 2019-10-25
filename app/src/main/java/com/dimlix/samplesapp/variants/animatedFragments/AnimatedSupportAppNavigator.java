package com.dimlix.samplesapp.variants.animatedFragments;

import android.annotation.SuppressLint;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.List;

import ru.terrakok.cicerone.android.support.SupportAppNavigator;
import ru.terrakok.cicerone.commands.Command;

public class AnimatedSupportAppNavigator extends SupportAppNavigator {

    private final FragmentManager mFragmentManager;

    public AnimatedSupportAppNavigator(FragmentActivity activity, int containerId) {
        super(activity, containerId);
        mFragmentManager = activity.getSupportFragmentManager();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void applyCommand(final Command command) {
        List<Fragment> fragments = mFragmentManager.getFragments();
        if (fragments.size() == 0) {
            super.applyCommand(command);
            return;
        }
        Fragment topFragment = fragments.get(fragments.size() - 1);
        if (topFragment instanceof AnimatedTransitionFragment) {
            ((AnimatedTransitionFragment) topFragment).exit()
                    .subscribe(aBoolean -> AnimatedSupportAppNavigator.super.applyCommand(command));
        } else {
            super.applyCommand(command);
        }
    }
}

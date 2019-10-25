package com.dimlix.samplesapp.variants.animatedFragments;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.dimlix.samplesapp.R;
import com.dimlix.samplesapp.variants.BaseSampleActivity;

import ru.terrakok.cicerone.Cicerone;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;

public class AnimatedFragmentTransitionActivity extends BaseSampleActivity {

    private Cicerone<Router> cicerone;
    private Navigator navigator;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_animated_fragments;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initCicerone();
        getRouter().navigateTo(new FirstFragment.Screen());
    }

    private void initCicerone() {
        cicerone = Cicerone.create();
        navigator = new AnimatedSupportAppNavigator(this, R.id.containerAnimated);
    }

    public NavigatorHolder getNavigatorHolder() {
        return cicerone.getNavigatorHolder();
    }

    public Router getRouter() {
        return cicerone.getRouter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNavigatorHolder().setNavigator(navigator);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getNavigatorHolder().removeNavigator();
    }
}

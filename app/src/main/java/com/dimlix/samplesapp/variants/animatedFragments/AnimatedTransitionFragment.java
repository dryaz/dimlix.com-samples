package com.dimlix.samplesapp.variants.animatedFragments;

import io.reactivex.Single;

public interface AnimatedTransitionFragment {
    public Single<Boolean> exit();
}

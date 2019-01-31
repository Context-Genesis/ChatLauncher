package com.contextgenesis.chatlauncher.fluidresize;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.view.ViewGroup;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import lombok.val;

public final class FluidContentResizer {

    private ValueAnimator heightAnimator;

    public FluidContentResizer(Activity activity) {
        heightAnimator = new ObjectAnimator();
        val viewHolder = new ActivityViewHolder(activity);

        KeyboardVisibilityDetector.listen(viewHolder, event -> animateHeight(viewHolder, event));

        viewHolder.onDetach(() -> {
            heightAnimator.cancel();
            heightAnimator.removeAllUpdateListeners();
        });
    }

    public void animateHeight(ActivityViewHolder viewHolder, KeyboardVisibilityChanged event) {
        ViewGroup contentView = viewHolder.getContentView();
        setHeight(contentView, event.getContentHeightBeforeResize());

        heightAnimator.cancel();
        // Warning: animating height might not be very performant. Try turning on
        // "Profile GPI rendering" in developer options and check if the bars stay
        // under 16ms in your app. Using Transitions API would be more efficient, but
        // for some reason it skips the first animation and I cannot figure out why.
        heightAnimator = ObjectAnimator.ofInt(event.getContentHeightBeforeResize(), event.getContentHeight());
        heightAnimator.setInterpolator(new FastOutSlowInInterpolator());
        heightAnimator.setDuration(400);
        heightAnimator.addUpdateListener(animation -> setHeight(contentView, (Integer) animation.getAnimatedValue()));
        heightAnimator.start();
    }

    private void setHeight(ViewGroup viewGroup, int height) {
        val params = viewGroup.getLayoutParams();
        params.height = height;
        viewGroup.setLayoutParams(params);
    }

}

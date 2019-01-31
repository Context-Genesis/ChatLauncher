package com.contextgenesis.chatlauncher.fluidresize;

import android.view.ViewTreeObserver;

import lombok.RequiredArgsConstructor;
import lombok.val;

public final class KeyboardVisibilityDetector {

    public static void listen(ActivityViewHolder viewHolder, KeyboardVisibilityChangedListener listener) {
        val detector = new Detector(viewHolder, listener);
        viewHolder.getNonResizableLayout().getViewTreeObserver().addOnPreDrawListener(detector);
        viewHolder.onDetach(() -> viewHolder.getNonResizableLayout().getViewTreeObserver().removeOnPreDrawListener(detector));
    }

    @RequiredArgsConstructor
    public static class Detector implements ViewTreeObserver.OnPreDrawListener {
        private final ActivityViewHolder viewHolder;
        private final KeyboardVisibilityChangedListener listener;
        private int previousHeight = -1;

        @Override
        public boolean onPreDraw() {
            val detected = detect();

            // The layout flickers for a moment, usually on the first
            // animation. Intercepting this pre-draw seems to solve the problem.
            return !detected;
        }

        private boolean detect() {
            val contentHeight = viewHolder.getResizableLayout().getHeight();
            if (contentHeight == previousHeight) {
                return false;
            }
            if (previousHeight != -1) {
                val statusBarHeight = viewHolder.getResizableLayout().getTop();
                val isKeyboardVisible = contentHeight < viewHolder.getNonResizableLayout().getHeight() - statusBarHeight;
                KeyboardVisibilityChanged changed = new KeyboardVisibilityChanged(isKeyboardVisible, contentHeight, previousHeight);
                listener.onVisibilityChanged(changed);
            }

            previousHeight = contentHeight;
            return true;
        }
    }

}

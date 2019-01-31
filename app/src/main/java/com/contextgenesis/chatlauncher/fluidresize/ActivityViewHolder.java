package com.contextgenesis.chatlauncher.fluidresize;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import lombok.Getter;
import lombok.ToString;

import static android.view.Window.ID_ANDROID_CONTENT;

@ToString
@Getter
public class ActivityViewHolder {

    private final ViewGroup nonResizableLayout;
    private final ViewGroup resizableLayout;
    private final ViewGroup contentView;

    /**
     * The Activity View tree usually looks like this:
     * <p>
     * DecorView <- does not get resized, contains space for system Ui bars.
     * - LinearLayout
     * -- FrameLayout <- gets resized
     * --- LinearLayout
     * ---- Activity content
     */
    public ActivityViewHolder(Activity activity) {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup contentView = decorView.findViewById(ID_ANDROID_CONTENT);
        ViewGroup actionBarRootLayout = (ViewGroup) contentView.getParent();
        ViewGroup resizeableLayout = (ViewGroup) actionBarRootLayout.getParent();

        this.nonResizableLayout = decorView;
        this.resizableLayout = resizeableLayout;
        this.contentView = contentView;
    }

    public void onDetach(DetachListener detachListener) {
        nonResizableLayout.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewDetachedFromWindow(View v) {
                detachListener.onDetach();
            }

            @Override
            public void onViewAttachedToWindow(View v) {
            }
        });
    }

    public interface DetachListener {
        void onDetach();
    }

}

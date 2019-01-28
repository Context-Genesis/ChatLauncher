package com.contextgenesis.chatlauncher.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;

import com.contextgenesis.chatlauncher.R;
import com.contextgenesis.chatlauncher.RootApplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AttachmentsCardView extends CardView {

    @BindView(R.id.option_1)
    ImageTextView option1;
    @BindView(R.id.option_2)
    ImageTextView option2;
    @BindView(R.id.option_3)
    ImageTextView option3;
    @BindView(R.id.option_4)
    ImageTextView option4;
    @BindView(R.id.option_5)
    ImageTextView option5;
    @BindView(R.id.option_6)
    ImageTextView option6;

    public AttachmentsCardView(@NonNull Context context) {
        super(context);
        initView();
    }

    public AttachmentsCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.attachments_cardview, this, true);
        ButterKnife.bind(this, view);
        ((RootApplication) getContext().getApplicationContext()).getAppComponent().inject(this);
        initOptions();
    }

    /**
     * Set the individual icons if they haven't already been set
     */
    private void initOptions() {

    }

    public boolean isVisible() {
        return getVisibility() == VISIBLE;
    }

    public void show(int xRevealPosition) {
        int centerX = xRevealPosition;
        int centerY = getTop();

        int startRadius = 0;
        int endRadius = (int) Math.hypot(getWidth(), getHeight());

        Animator anim = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            anim = ViewAnimationUtils.createCircularReveal(this, centerX, centerY, startRadius, endRadius);
            anim.setDuration(300);
        }

        setVisibility(VISIBLE);
        if (anim != null) {
            anim.start();
        }
    }

    public void hide(int xRevealPosition) {
        int centerX = xRevealPosition;
        int centerY = getTop();

        int startRadius = (int) Math.hypot(getWidth(), getHeight());
        int endRadius = 0;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Animator anim = ViewAnimationUtils.createCircularReveal(this, centerX, centerY, startRadius, endRadius);
            anim.setDuration(300);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    setVisibility(GONE);
                }
            });
            anim.start();
        } else {
            setVisibility(GONE);
        }
    }
}

package com.contextgenesis.chatlauncher.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;

import com.contextgenesis.chatlauncher.R;
import com.contextgenesis.chatlauncher.RootApplication;
import com.contextgenesis.chatlauncher.events.InputMessageEvent;
import com.contextgenesis.chatlauncher.events.OutputMessageEvent;
import com.contextgenesis.chatlauncher.manager.alias.AliasManager;
import com.contextgenesis.chatlauncher.rx.RxBus;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ShortcutsCardView extends CardView implements View.OnClickListener {

    @Inject
    RxBus rxBus;
    @Inject
    AliasManager aliasManager;

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

    private int previousHideX;
    private ImageTextView[] options = null;

    public ShortcutsCardView(@NonNull Context context) {
        super(context);
        initView();
    }

    public ShortcutsCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.shortcuts_cardview, this, true);
        ButterKnife.bind(this, view);
        ((RootApplication) getContext().getApplicationContext()).getAppComponent().inject(this);
        invalidateOptions();
        setClickListeners();
    }

    private void setClickListeners() {
        for (int id = 1; id < 7; id++) {
            getOption(id).setOnClickListener(this);
        }
    }

    private String getShortcutName(int id) {
        return String.format("shortcut-%d", id);
    }

    /**
     * Set the individual icons if they haven't already been set
     */
    private void invalidateOptions() {
        for (int id = 1; id < 7; id++) {
            if (aliasManager.containsAlias(getShortcutName(id))) {
                getOption(id).getTitle().setText(aliasManager.getAlias(getShortcutName(id)).getCommand());
            } else {
                getOption(id).getTitle().setText("Click to Set");
            }
        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onClick(View v) {
        for (int id = 1; id < 7; id++) {
            if (getOption(id).getId() == v.getId()) {
                if (aliasManager.containsAlias(getShortcutName(id))) {
                    rxBus.post(new InputMessageEvent(aliasManager.getAlias(getShortcutName(id)).getCommand(), false));
                    hide();
                } else {
                    rxBus.post(new OutputMessageEvent("To create a shortcut, write the command you'd like to launch"));
                    rxBus.post(new InputMessageEvent(String.format("set shortcut-%d=", id), true));
                    hide();
                }
            }
        }
    }

    public boolean isVisible() {
        return getVisibility() == VISIBLE;
    }

    public void show(int xRevealPosition) {
        invalidateOptions();
        int centerX = xRevealPosition;
        int centerY = getHeight();

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

    private void hide() {
        hide(previousHideX);
    }

    public void hide(int xRevealPosition) {
        previousHideX = xRevealPosition;
        int centerX = xRevealPosition;
        int centerY = getHeight();

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

    /**
     * @param id between [1 and 6].
     */
    private ImageTextView getOption(int id) {
        if (id <= 0 || id >= 7) {
            throw new RuntimeException("Ahem. Coder. Galti kar raha hai bc. But we should convert this whole thing to a recyclerview really. But meh.");
        }
        if (options == null) {
            options = new ImageTextView[]{
                    option1,
                    option2,
                    option3,
                    option4,
                    option5,
                    option6
            };
        }
        return options[id - 1];
    }
}

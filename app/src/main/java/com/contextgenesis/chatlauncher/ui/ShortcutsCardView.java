package com.contextgenesis.chatlauncher.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;

import com.contextgenesis.chatlauncher.R;
import com.contextgenesis.chatlauncher.RootApplication;
import com.contextgenesis.chatlauncher.command.Alias;
import com.contextgenesis.chatlauncher.events.InputMessageEvent;
import com.contextgenesis.chatlauncher.events.OutputMessageEvent;
import com.contextgenesis.chatlauncher.manager.alias.AliasManager;
import com.contextgenesis.chatlauncher.manager.app.AppManager;
import com.contextgenesis.chatlauncher.manager.input.InputMessage;
import com.contextgenesis.chatlauncher.manager.input.InputParser;
import com.contextgenesis.chatlauncher.rx.RxBus;
import com.contextgenesis.chatlauncher.utils.StringUtils;

import javax.inject.Inject;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("PMD.GodClass")
public class ShortcutsCardView extends CardView implements View.OnClickListener,
        View.OnLongClickListener {

    @Inject
    RxBus rxBus;
    @Inject
    InputParser inputParser;
    @Inject
    AliasManager aliasManager;
    @Inject
    AppManager appManager;
    @Inject
    PackageManager packageManager;
    @Inject
    Context context;

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
            getOption(id).setOnLongClickListener(this);
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
            getOption(id).getTitle().setText(getTitleForOption(id));
            getOption(id).getImageView().setImageDrawable(getDrawableForOption(id));
        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onClick(View v) {
        for (int id = 1; id < 7; id++) {
            if (getOption(id).getId() == v.getId()) {
                if (aliasManager.containsAlias(getShortcutName(id))) {
                    rxBus.post(new InputMessageEvent(aliasManager.getAlias(getShortcutName(id)).getCommand(), false, false));
                    hide();
                } else {
                    rxBus.post(new OutputMessageEvent("To create a shortcut, write the command you'd like to launch"));
                    rxBus.post(new InputMessageEvent(String.format("set shortcut-%d=", id), true, false));
                    hide();
                }
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        for (int id = 1; id < 7; id++) {
            if (getOption(id).getId() == v.getId() && aliasManager.containsAlias(getShortcutName(id))) {
                rxBus.post(new InputMessageEvent(String.format("unset shortcut-%d", id), false, false));
                hide();
                return true;
            }
        }
        return false;
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
            for (int id = 1; id < 7; id++) {
                ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f, 1.f, 0.5f, 1.f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
                scaleAnimation.setInterpolator(new DecelerateInterpolator());
                scaleAnimation.setDuration(300);
                scaleAnimation.setStartOffset(100 - ((id - 1) % 3) * 30);
                getOption(id).startAnimation(scaleAnimation);
            }
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

    private String getTitleForOption(int id) {
        Alias alias = aliasManager.getAlias(getShortcutName(id));
        if (alias == null) {
            return "Click to set";
        }
        InputMessage validCommand = inputParser.parse(alias.getCommand());
        switch (validCommand.getCommandType()) {
            case LAUNCH_APP:
                return appManager.getAppInfoFromName(validCommand.getArgs()[0]).getLabel();
            case CALL:
                return StringUtils.getNthString(validCommand.getArgs()[0].trim(), 0);
            default:
                return alias.getCommand();
        }
    }

    private Drawable getDrawableForOption(int id) {
        Alias alias = aliasManager.getAlias(getShortcutName(id));
        if (alias == null) {
            return getDefaultDrawable(id, R.color.shortcut_unselected_option_tint);
        }
        InputMessage validCommand = inputParser.parse(alias.getCommand());
        switch (validCommand.getCommandType()) {
            case LAUNCH_APP:
                try {
                    return packageManager.getApplicationIcon(appManager.getAppInfoFromName(validCommand.getArgs()[0]).getComponentName().getPackageName());
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    return getDefaultDrawable(id, R.color.shortcut_unselected_option_tint);
                }
            case CALL:
                return context.getResources().getDrawable(R.drawable.phone);
            default:
                return getDefaultDrawable(id, R.color.shortcut_unselected_option_tint);
        }
    }

    private Drawable getDefaultDrawable(int id, @ColorRes int tint) {
        Drawable drawable = getDefaultDrawable(id);
        drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, tint), PorterDuff.Mode.SRC_IN));
        return drawable;
    }

    private Drawable getDefaultDrawable(int id) {
        switch (id) {
            case 1:
                return context.getResources().getDrawable(R.drawable.ic_circled_1);
            case 2:
                return context.getResources().getDrawable(R.drawable.ic_circled_2);
            case 3:
                return context.getResources().getDrawable(R.drawable.ic_circled_3);
            case 4:
                return context.getResources().getDrawable(R.drawable.ic_circled_4);
            case 5:
                return context.getResources().getDrawable(R.drawable.ic_circled_5);
            case 6:
                return context.getResources().getDrawable(R.drawable.ic_circled_6);
            default:
                return context.getResources().getDrawable(R.drawable.ic_circled_1);
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

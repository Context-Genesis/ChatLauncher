package com.contextgenesis.chatlauncher.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.contextgenesis.chatlauncher.R;
import com.contextgenesis.chatlauncher.RootApplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import butterknife.ButterKnife;

public class AttachmentsCardView extends CardView {
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
    }
}

package com.contextgenesis.chatlauncher.ui;

/**
 * Created by rish on 25/3/18.
 */


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.contextgenesis.chatlauncher.R;

import androidx.annotation.Dimension;
import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Getter;

/**
 * Created by rish on 5/17/2016.
 */
public class ImageTextView extends LinearLayout {

    @Getter
    @BindView(R.id.image_view)
    ImageView imageView;
    @Getter
    @BindView(R.id.title)
    TextView title;

    public ImageTextView(Context context) {
        super(context);
        initialize(context);
    }

    public ImageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
        TypedArray params = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.image_text_view,
                0, 0
        );

        Drawable drawable = params.getDrawable(R.styleable.image_text_view_image_src);
        @Dimension float width = params.getDimension(R.styleable.image_text_view_image_width, 48);
        @Dimension float height = params.getDimension(R.styleable.image_text_view_image_height, 48);

        if (drawable != null) {
            imageView.setImageDrawable(drawable);
        }
        imageView.setVisibility(drawable == null ? GONE : VISIBLE);
        imageView.getLayoutParams().width = (int) width;
        imageView.getLayoutParams().height = (int) height;

        CharSequence titleText = params.getText(R.styleable.image_text_view_text_title);
        @Dimension float titleSize = params.getDimensionPixelSize(R.styleable.image_text_view_text_title_size, 18);
        boolean titleBold = params.getBoolean(R.styleable.image_text_view_text_title_bold, true);

        title.setText(titleText);
        title.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
        title.setTypeface(title.getTypeface(), titleBold ? Typeface.BOLD : Typeface.NORMAL);

        requestLayout();
        params.recycle();
    }

    private void initialize(Context context) {
        View view = inflate(context, R.layout.image_with_text, this);
        ButterKnife.bind(this);
        imageView = (ImageView) view.findViewById(R.id.image_view);
        title = (TextView) view.findViewById(R.id.title);
    }
}
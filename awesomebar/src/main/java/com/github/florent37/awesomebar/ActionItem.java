package com.github.florent37.awesomebar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.github.florent37.awesomebar.shape.RoundRect;

/**
 * Created by florentchampigny on 30/01/2017.
 */

public class ActionItem extends LinearLayout {

    private OnClickListener onClickListener;

    private RoundRect roundRect;
    private int backgroundColor = Color.RED;
    private boolean animateBeforeClick = true;

    private final AppCompatImageView icon;
    private final AppCompatTextView text;

    public ActionItem(Context context) {
        this(context, null);
    }

    public ActionItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ActionItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.bar_action_item, this);
        setWillNotDraw(false);

        icon = findViewById(R.id.action_icon);
        text = findViewById(R.id.action_text);

        setOnClickListener(new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(final View v) {
                if(animateBeforeClick) {
                    boolean animated = tryToAnimate();
                    if(animated) {
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (onClickListener != null) {
                                    onClickListener.onClick(v);
                                }
                            }
                        }, 300);
                    } else {
                        if (onClickListener != null) {
                            onClickListener.onClick(v);
                        }
                    }
                } else {
                    if (onClickListener != null) {
                        onClickListener.onClick(v);
                    }
                }
            }
        });
    }


    private boolean tryToAnimate(){
            final Drawable drawable = icon.getDrawable();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (drawable instanceof AnimatedVectorDrawable) {
                    final AnimatedVectorDrawable d = (AnimatedVectorDrawable) drawable;
                    d.start();
                    return true;
                }
            }

            if (drawable instanceof AnimatedVectorDrawableCompat) {
                final AnimatedVectorDrawableCompat d = (AnimatedVectorDrawableCompat) drawable;
                d.start();
                return true;
            }
            return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(roundRect == null) {
            roundRect = new RoundRect(0, 0, getWidth(), getHeight());
            roundRect.setColor(backgroundColor);
        }

        roundRect.drawOn(canvas);

        super.onDraw(canvas);
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        if(roundRect != null){
            roundRect.setColor(backgroundColor);
        }
    }

    public void setText(String actionName) {
        text.setText(actionName);
        postInvalidate();
    }

    public String getText() {
        return text.getText().toString();
    }

    public void setDrawable(@Nullable @DrawableRes Integer drawable) {
        if(drawable == null){
            icon.setVisibility(GONE);
        } else {
            icon.setImageResource(drawable);
            icon.setVisibility(VISIBLE);
        }
    }

    public void setAnimateBeforeClick(boolean animateBeforeClick) {
        this.animateBeforeClick = animateBeforeClick;
    }

    public void setClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}

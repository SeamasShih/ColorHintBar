package com.seamas.colorhintbarlibrary;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class ColorHintBar extends View {
    private int itemAmount = 1;
    private int site = 0;
    private float shift = 0;
    private float itemWidth = 0;

    private Paint bgPaint = new Paint();
    private Paint hintPaint = new Paint();

    private ValueAnimator vanish = ValueAnimator.ofInt(255, 0);
    private ValueAnimator timer = ValueAnimator.ofFloat(0, 100);
    private AnimatorSet animatorSet = new AnimatorSet();
    private boolean isVanishMode = true;

    public ColorHintBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        hintPaint.setColor(getResources().getColor(R.color.defaultHint));
        bgPaint.setColor(getResources().getColor(R.color.defaultBackground));

        vanish.setDuration(500);
        vanish.addUpdateListener(animation -> {
            bgPaint.setAlpha((int) animation.getAnimatedValue());
            postInvalidate();
        });

        timer.setDuration(1500);

        animatorSet.play(timer).before(vanish);
        animatorSet.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        measureParam();
    }

    private void measureParam() {
        setItemWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0, 0, getWidth(), getHeight(), bgPaint);

        canvas.translate((site + shift) * itemWidth, 0);
        canvas.drawRect(0, 0, itemWidth, getHeight(), hintPaint);
    }

    public void setHintColor(int color) {
        hintPaint.setColor(color);
        postInvalidate();
    }

    public void setBackgroundHintColor(int color) {
        bgPaint.setColor(color);
        postInvalidate();
    }

    public int getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(int itemAmount) {
        if (itemAmount < 1)
            itemAmount = 1;
        this.itemAmount = itemAmount;
        setItemWidth();
        postInvalidate();
    }

    private void setItemWidth() {
        float w = getMeasuredWidth();
        itemWidth = w / itemAmount;
    }

    public int getSite() {
        return site;
    }

    public void setSite(int site) {
        this.site = site;
        refreshTimer();
        postInvalidate();
    }

    public float getShift() {
        return shift;
    }

    public void setShift(float shift) {
        this.shift = shift;
        refreshTimer();
        postInvalidate();
    }

    public void setSiteShift(int max, int site, float shift) {
        if (max < 1)
            max = 1;
        this.itemAmount = max;
        setItemWidth();
        this.site = site;
        this.shift = shift;
        refreshTimer();
        postInvalidate();
    }

    public void setViewConnectedPager(ViewPager viewPager) {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                setSiteShift(viewPager.getAdapter().getCount(), i, v);
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    public void setVanishTime(long time) {
        vanish.setDuration(time);
    }

    private void refreshTimer() {
        if (isVanishMode) {
            animatorSet.cancel();
            animatorSet.start();
        }else {
            animatorSet.cancel();
            bgPaint.setAlpha(255);
            postInvalidate();
        }
    }

    public void setTimerTime(long time) {
        if (time < 0) {
            isVanishMode = false;
        } else {
            timer.setDuration(time);
        }
        refreshTimer();
    }
}

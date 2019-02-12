package com.seamas.colorhintbar;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class ColorHintBar extends View {
    private int itemAmount = 1;
    private int site = 0;
    private float shift = 0;
    private float itemWidth = 0;

    private Paint bgPaint = new Paint();
    private Paint hintPaint = new Paint();

    private Timer timer = new Timer();
    private ValueAnimator vanish = ValueAnimator.ofInt(255, 0);
    private boolean isVanishMode = true;

    public ColorHintBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        hintPaint.setColor(getResources().getColor(R.color.colorAccent));
        bgPaint.setColor(getResources().getColor(R.color.colorPrimaryDark));

        vanish.setDuration(500);
        vanish.addUpdateListener(animation -> {
            bgPaint.setAlpha((int) animation.getAnimatedValue());
            postInvalidate();
        });

        timer.start();
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

    public void setVanishTime(long time) {
        vanish.setDuration(time);
    }

    private void refreshTimer() {
        if (!isVanishMode) {
            if (timer.isStart)
                timer.isEnd = true;
        } else if (timer.isEnd) {
            long time = timer.time;
            timer = new Timer();
            timer.time = time;
            timer.start();
        } else if (timer.isStart) {
            timer.starTime = System.currentTimeMillis();
        } else {
            timer.start();
        }
    }

    private void setTimerTime(long time) {
        if (time < 0) {
            isVanishMode = false;
            refreshTimer();
            bgPaint.setAlpha(255);
            postInvalidate();
        } else {
            if (timer.isEnd) {
                timer = new Timer();
                timer.time = time;
            } else {
                timer.time = time;
            }
        }
    }

    private class Timer extends Thread {
        long time = 1500;
        long starTime = System.currentTimeMillis();
        boolean isEnd = false;
        boolean isStart = false;

        @Override
        public void run() {
            beginning();

            while (true) {
                if (isEnd)
                    break;
                else if (System.currentTimeMillis() > starTime + time) {
                    doing();
                    break;
                }
            }
        }

        private void beginning() {
            isStart = true;
            bgPaint.setAlpha(255);
            postInvalidate();
        }

        private void doing() {
            isEnd = true;
            ((Activity) getContext()).runOnUiThread(() -> vanish.start());
        }
    }
}

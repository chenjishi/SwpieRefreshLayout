package com.chenjishi.swipe.library;

import android.content.res.Resources;
import android.graphics.*;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.animation.AnimationUtils;
import com.chenjishi.swipe.R;

/**
 * Created by chenjishi on 14/11/12.
 */
public class SwipeProgressBar {
    private static final int ANIMATION_DURATION_MS = 1600;
    private final static int IMAGE_WIDTH = 50;

    private final Bitmap mCompass;
    private final Bitmap mSpinner;

    private final Paint mPaint = new Paint();

    private Rect mBounds = new Rect();

    private final RectF mRect = new RectF();

    private View mParent;

    private float mPercentage;

    private int mImageWidth;

    private long mStartTime;
    private long mFinishTime;
    private boolean mRunning = false;

    public SwipeProgressBar(View parent) {
        mParent = parent;

        final Resources res = parent.getResources();

        mCompass = BitmapFactory.decodeResource(res, R.drawable.ic_compass);
        mSpinner = BitmapFactory.decodeResource(res, R.drawable.ic_spinner);

        final float density = res.getDisplayMetrics().density;

        mImageWidth = (int) (density * IMAGE_WIDTH);
    }

    void setTriggerPercentage(float percentage) {
        mPercentage = percentage;
        ViewCompat.postInvalidateOnAnimation(mParent, mBounds.left,
                mBounds.top, mBounds.right, mBounds.bottom);
    }

    void start() {
        if (!mRunning) {
            mPercentage = 0;
            mStartTime = AnimationUtils.currentAnimationTimeMillis();
            mRunning = true;
            mParent.postInvalidate();
        }
    }

    void stop() {
        if (mRunning) {
            mPercentage = 0;
            mFinishTime = AnimationUtils.currentAnimationTimeMillis();
            mRunning = false;
            mParent.postInvalidate();
        }
    }

    void draw(Canvas canvas) {
        final int width = mBounds.width();
        final int height = mBounds.height();
        int centerX = width / 2;

        int startX = centerX - mImageWidth / 2 - mImageWidth;
        int endX = centerX + mImageWidth / 2 + mImageWidth;

        int startY = -mImageWidth;
        int endY = height / 2 - mImageWidth / 2;

        int restoreCount = canvas.save();
        canvas.clipRect(mBounds);

        if (mRunning) {
            long now = AnimationUtils.currentAnimationTimeMillis();
            long elapsed = (now - mStartTime) % ANIMATION_DURATION_MS;
            long iterations = (now - mStartTime) / ANIMATION_DURATION_MS;
            float rawProgress = (elapsed / (ANIMATION_DURATION_MS / 100f));

            mRect.right = endX - mImageWidth;
            mRect.top = endY;
            mRect.left = mRect.right - mImageWidth;
            mRect.bottom = mRect.top + mImageWidth;
            canvas.drawBitmap(mCompass, null, mRect, null);

            mRect.left = startX + mImageWidth;
            mRect.top = endY;
            mRect.right = mRect.left + mImageWidth;
            mRect.bottom = mRect.top + mImageWidth;

            canvas.restoreToCount(restoreCount);
            restoreCount = canvas.save();
            canvas.clipRect(mRect);

            int cx = (int) (mRect.left + mRect.width() / 2);
            int cy = (int) (mRect.top + mRect.height() / 2);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(Color.BLUE);
            canvas.drawCircle(cx, cy, 4, mPaint);


            final float percent = rawProgress * 1.0f / 100;

            canvas.translate(cx, cy);
            canvas.rotate(360 * percent);
            canvas.translate(-cx, -cy);

            canvas.drawBitmap(mSpinner, null, mRect, null);
            ViewCompat.postInvalidateOnAnimation(mParent, mBounds.left, mBounds.top,
                    mBounds.right, mBounds.bottom);
        } else {
            mRect.right = endX - mImageWidth * mPercentage;
            mRect.top = startY + (endY - startY) * mPercentage;
            mRect.left = mRect.right - mImageWidth;
            mRect.bottom = mRect.top + mImageWidth;
            canvas.drawBitmap(mCompass, null, mRect, mPaint);

            mRect.left = startX + mImageWidth * mPercentage;
            mRect.top = startY + (endY - startY) * mPercentage;
            mRect.right = mRect.left + mImageWidth;
            mRect.bottom = mRect.top + mImageWidth;
            canvas.drawBitmap(mSpinner, null, mRect, mPaint);
        }

        canvas.restoreToCount(restoreCount);
    }

    void setBounds(int left, int top, int right, int bottom) {
        mBounds.left = left;
        mBounds.top = top;
        mBounds.right = right;
        mBounds.bottom = bottom;
    }
}

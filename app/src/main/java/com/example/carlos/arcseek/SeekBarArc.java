package com.example.carlos.arcseek;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Carlos on 07/04/2018.
 */

public class SeekBarArc extends View {

    private float mMinProgress = 0;
    private float mMaxProgress = 100;
    private float mIncreaseProgress = 1;
    private float mProgress = 0;

    private float mProgressWidth;
    private float mProgressBackgroundWidth = 2;

    private int mProgressColor;
    private int mProgressBackgroundColor;
    private int[] mProgressGradientColor;

    private float mStartAngle;
    private float mEndAngle;

    private boolean mIsShowThumb = true;
    private Drawable mThumb;

    private boolean mRoundEdges;

    private Paint mProgressPaint;
    private Paint mBackgroundPaint;

    private float mMinSize;
    private float mArcWidth;
    private float mArcRadius;
    private float mCenter;
    private float mInnerRadius;

    private RectF mArcRectF;

    private SweepGradient mSweepGradient;

    public SeekBarArc(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SeekBarArc(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attr){
        TypedArray typedArray = null;
        try{
            //SE OBTIENEN LOS VALORES DEL RECRUSO DE ESTILO
            typedArray = context.obtainStyledAttributes(attr, R.styleable.SeekBarArc);
            mMinProgress = typedArray.getFloat(R.styleable.SeekBarArc_minProgress, mMinProgress);
            mMaxProgress = typedArray.getFloat(R.styleable.SeekBarArc_maxProgress, mMaxProgress);
            mIncreaseProgress = typedArray.getFloat(R.styleable.SeekBarArc_increaseValue, mIncreaseProgress);
            mProgress = typedArray.getFloat(R.styleable.SeekBarArc_progress, mProgress);
            mStartAngle = typedArray.getFloat(R.styleable.SeekBarArc_startAngle, mStartAngle);
            mEndAngle = typedArray.getFloat(R.styleable.SeekBarArc_endAngle, mEndAngle);
            mProgressWidth = typedArray.getDimension(R.styleable.SeekBarArc_progressWidth, context.getResources().getDisplayMetrics().density * 4);
            mProgressBackgroundWidth = typedArray.getDimension(R.styleable.SeekBarArc_progressBackgroundWidth, context.getResources().getDisplayMetrics().density * 2);
            mProgressColor = typedArray.getColor(R.styleable.SeekBarArc_progressColor, ContextCompat.getColor(context, android.R.color.darker_gray));
            mProgressBackgroundColor = typedArray.getColor(R.styleable.SeekBarArc_progressBackgroundColor, ContextCompat.getColor(context, android.R.color.holo_blue_light));
            mRoundEdges = typedArray.getBoolean(R.styleable.SeekBarArc_roundEdges, mRoundEdges);
            mIsShowThumb = typedArray.getBoolean(R.styleable.SeekBarArc_showThumb, mIsShowThumb);
            mThumb = (typedArray.getDrawable(R.styleable.SeekBarArc_thumb) != null) ? typedArray.getDrawable(R.styleable.SeekBarArc_thumb) : getResources().getDrawable(R.drawable.thumb);

            //SE CREAN LOS OBJECTOS PAINT
            mProgressPaint = new Paint();
            mProgressPaint.setColor(mProgressColor);
            mProgressPaint.setStrokeWidth(mProgressWidth);
            mBackgroundPaint = new Paint();
            mBackgroundPaint.setColor(mProgressBackgroundColor);
            mBackgroundPaint.setStrokeWidth(mProgressBackgroundWidth);
            if(mRoundEdges){
                mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
                mBackgroundPaint.setStrokeCap(Paint.Cap.ROUND);
            }else{
                mProgressPaint.setStrokeCap(Paint.Cap.SQUARE);
                mBackgroundPaint.setStrokeCap(Paint.Cap.SQUARE);
            }

        } finally {
            if(typedArray != null)
                typedArray.recycle();
        }
    }

    public void setProgressGradient(int[] colors){
        mProgressGradientColor = colors;
        mProgressPaint.setShader(new SweepGradient(mCenter, mCenter, colors, null));
        invalidate();
    }
}

package com.example.carlos.arcseek;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by Carlos on 11/04/2018.
 */

public class GradientProgressBar extends View {

    private Paint mProgressPaint;
    private Paint mBorderPaint;

    private int mBorderOffset;

    private float mCurrent;
    private float mTotal;
    private int mBarColor;

    private int width;
    private int height;

    private float mCornerRadius = 0;

    private RectF mProgressRect;
    private RectF mBackgroundRect;

    private Xfermode mProgressXferMode;
    private Xfermode mRadianceXferMode;

    private LinearGradient mProgressGradient;
    private int mBackgroundColor = Color.GRAY;
    private int mProgressStartColor = Color.BLUE;
    private int mProgressEndColor = Color.CYAN;
    private int[] mProgressColors = new int[]{mProgressStartColor, mProgressEndColor};

    private Shader mRadianceGradient;
    private int mRadianceStartColor = Color.BLUE;
    private int mRadianceEndColor = Color.CYAN;
    private int[] mRadianceColors = new int[]{mRadianceStartColor, mRadianceEndColor};
    private boolean mHasRadiance;

    public GradientProgressBar(Context context) {
        super(context);
        mBorderOffset = getResources().getDimensionPixelOffset(R.dimen.border_offset);
        init();
    }

    public GradientProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public GradientProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void init(AttributeSet attr){
        //SE OBTIENEN LOS VALORES DEL RECRUSO DE ESTILO
        TypedArray typedArray = null;
        Context context = getContext();
        try{
            typedArray = context.obtainStyledAttributes(attr, R.styleable.GradientProgressBar);
            mBorderOffset = typedArray.getDimensionPixelSize(R.styleable.GradientProgressBar_borderOffset,
                    getResources().getDimensionPixelOffset(R.dimen.border_offset));
            mBackgroundColor = typedArray.getColor(R.styleable.GradientProgressBar_progressBackgroundColor, Color.GRAY);
            mProgressStartColor = typedArray.getColor(R.styleable.GradientProgressBar_progressStartColor, Color.BLUE);
            mProgressEndColor = typedArray.getColor(R.styleable.GradientProgressBar_progressEndColor, Color.CYAN);
            mCurrent = typedArray.getFloat(R.styleable.GradientProgressBar_progress, 0f);
            mTotal = typedArray.getFloat(R.styleable.GradientProgressBar_maxProgress, 100f);
            mCornerRadius = typedArray.getDimensionPixelSize(R.styleable.GradientProgressBar_cornerRadius,
                    getResources().getDimensionPixelSize(R.dimen.corner_radius));
            mHasRadiance = typedArray.getBoolean(R.styleable.GradientProgressBar_hasRadiance, false);
            mRadianceStartColor = typedArray.getColor(R.styleable.GradientProgressBar_radianceStartColor, Color.BLUE);
            mRadianceEndColor = typedArray.getColor(R.styleable.GradientProgressBar_radianceEndColor, Color.CYAN);

            mProgressColors = new int[]{mProgressStartColor, mProgressEndColor};
            mRadianceColors = new int[]{mRadianceStartColor, mRadianceEndColor};
        }finally {
            if(typedArray != null)
                typedArray.recycle();
        }

        init();
    }

    public void init(){
        mProgressRect = new RectF();
        mBackgroundRect = new RectF();

        mProgressXferMode = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);
        mRadianceXferMode = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);

        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mProgressPaint.setColor(mBackgroundColor);

        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setColor(mBackgroundColor);
        mBorderPaint.setStrokeWidth(mBorderOffset);

        mProgressGradient = new LinearGradient(0, 0, width, 0, mProgressColors, null, Shader.TileMode.CLAMP);
        //mRadianceGradient = new LinearGradient(0, 0, width, 0, mRadianceColors, null, Shader.TileMode.CLAMP);
        //mRadianceGradient = new SweepGradient()
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;
        updateGradient();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float progress = mCurrent / mTotal;

        setLayerType(LAYER_TYPE_HARDWARE, mProgressPaint);

        mProgressRect.set(mBorderOffset, mBorderOffset, progress * getWidth() - mBorderOffset, getHeight() - mBorderOffset);
        mBackgroundRect.set(mBorderOffset, mBorderOffset, getWidth() - mBorderOffset, getHeight() - mBorderOffset);

        if(mHasRadiance && progress == 1){
            mProgressPaint.setShader(null);
            canvas.drawRoundRect(mBackgroundRect, mCornerRadius, mCornerRadius, mProgressPaint);

            mProgressPaint.setShader(mRadianceGradient);
            mProgressPaint.setXfermode(mRadianceXferMode);
            canvas.drawRoundRect(mBackgroundRect, mCornerRadius, mCornerRadius, mProgressPaint);
            mProgressPaint.setXfermode(null);

//            mBorderPaint.setShader(mRadianceGradient);
//            canvas.drawRoundRect(mBackgroundRect, mCornerRadius, mCornerRadius, mBorderPaint);
//            mBorderPaint.setShader(null);
        }else{
            mProgressPaint.setShader(null);
            canvas.drawRoundRect(mBackgroundRect, mCornerRadius, mCornerRadius, mProgressPaint);
            //canvas.drawLine(20, height / 2, width, height / 2, mProgressPaint);


            mProgressPaint.setShader(mProgressGradient);
            mProgressPaint.setXfermode(mProgressXferMode);
            canvas.drawRoundRect(mProgressRect, mCornerRadius, mCornerRadius, mProgressPaint);
            mProgressPaint.setXfermode(null);

            //canvas.drawRoundRect(mBackgroundRect, mCornerRadius, mCornerRadius, mBorderPaint);
        }

    }

    private void updateGradient(){
        mProgressGradient = new LinearGradient(0, 0, width, 0, mProgressColors, null, Shader.TileMode.CLAMP);
        //mRadianceGradient = new LinearGradient(0, 0, width, 0, mRadianceColors, null, Shader.TileMode.CLAMP);
        float[] positions = {0.0f, 0.33f, 0.66f, 1.0f};
        int[] colors = {
                0xFFFFFF88, // yellow
                0xFF0088FF, // blue
                0xFF000000, // black
                0xFFFFFF88  // yellow
        };
        mRadianceGradient = new SweepGradient(width / 2, height / 2, colors, positions);
    }
}

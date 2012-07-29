package com.robomorphine.strictmode.histogram;

import com.robomorphine.strictmode.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;

import java.util.Observable;
import java.util.Observer;

public class HistogramView extends View implements Observer {
    
    /**
     * Returns histogram that can be used for preview.
     */
    private static Histogram createPreviewHistogram() {
        Histogram hist = new Histogram(0, 300, 5);
        int middleIndex = hist.getBucketCount() / 2;
        for(int i = 0; i < hist.getBucketCount(); i++) {
            int hits = middleIndex - Math.abs(i - middleIndex);
            for(int c = 0; c <= hits; c++ ) {
                hist.add(hist.getBucketMin(i));
            }
        }
        return hist;
    }
    
    private Histogram mHistogram = createPreviewHistogram();
    
    private float mCoverage = 0.9f;
    private float mExtendedCoverage = 0.95f;
    private Pair<Integer, Integer> mCoverageResult = mHistogram.coverage(mCoverage);
    private Pair<Integer, Integer> mExtendedCoverageResult = mHistogram.coverage(mExtendedCoverage);
    
    public HistogramView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public HistogramView(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
    }
    
    public HistogramView(Context context) {
        super(context);
    }
    
    public void setHistogram(Histogram hist) {
        if(mHistogram != null) {
            mHistogram.deleteObserver(this);
        }
        
        mHistogram = hist;
        if(mHistogram != null) {
            mHistogram.addObserver(this);
        }
        
        update(mHistogram, null);
    }
    
    public void setCoverage(float coverage) {
        mCoverage = coverage;
        update(mHistogram, null);
    }
    
    public void setExtendedCoverage(float extendedCoverage) {
        mExtendedCoverage = extendedCoverage;
        update(mHistogram, null);
    }
    
    @Override
    public void update(Observable observable, Object data) {
        mCoverageResult = mHistogram.coverage(mCoverage);
        mExtendedCoverageResult = mHistogram.coverage(mExtendedCoverage);
        invalidate();
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        if(mHistogram == null) {
            return;
        }
        
        Resources res = getResources();
        
        /* draw background */
        Rect fullRect = new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight());
        Paint borderPaint = new Paint();
        borderPaint.setColor(res.getColor(R.color.histogram_bg));
        borderPaint.setStrokeWidth(1);
        canvas.drawRect(fullRect, borderPaint);
        
        
        /* pre-calculate histogram related data */
        int count = mHistogram.getBucketCount();        
        int maxIndex = mHistogram.getMaxHitCountBucket();
        double xDelta = getMeasuredWidth() * 1f / count;
        int xWidth = getMeasuredWidth() / count;
        
        if(xWidth == (int)xDelta) {
            xWidth--;
        }
        
        double yDelta = getMeasuredHeight() * 1f / (mHistogram.getBucketHitCount(maxIndex) + 1);
        int height = getMeasuredHeight();        
        
        /* coverage */
        Paint coveragePaint = new Paint();
        coveragePaint.setAntiAlias(true);
        coveragePaint.setColor(res.getColor(R.color.histogram_90));
        
        
        Paint extendedCoveragePaint = new Paint();
        extendedCoveragePaint.setAntiAlias(true);
        extendedCoveragePaint.setColor(res.getColor(R.color.histogram_95));
        
        Paint coverageLinePaint = new Paint();
        coverageLinePaint.setAntiAlias(true);
        coverageLinePaint.setColor(res.getColor(R.color.histogram_avarage));
                
        int extendedCoverageLeft =(int)(xDelta * mExtendedCoverageResult.first);        
        int extendedCoverageRight = (int)(xDelta * (mExtendedCoverageResult.second + 1)) - 1;
        Rect extendedCoverageRect = new Rect(extendedCoverageLeft, 0, extendedCoverageRight, height);
        canvas.drawRect(extendedCoverageRect, extendedCoveragePaint);
        
        int coverageLeft =(int)(xDelta * mCoverageResult.first);        
        int coverageRight = (int)(xDelta * (mCoverageResult.second + 1)) - 1;
        Rect coverageRect = new Rect(coverageLeft, 0, coverageRight, height);
        canvas.drawRect(coverageRect, coveragePaint);
                
        int coverageMiddle = (coverageLeft + coverageRight) / 2; 
        Rect coverageMiddleRect = new Rect(coverageMiddle - 1, 0, coverageMiddle + 1, height);
        canvas.drawRect(coverageMiddleRect, coverageLinePaint);
        
        /* data */
        Paint ordinalPaint = new Paint();
        ordinalPaint.setColor(res.getColor(R.color.histogram_bucket));
        ordinalPaint.setAntiAlias(true);
        
        Paint maxPaint = new Paint();
        maxPaint.setColor(res.getColor(R.color.histogram_max_bucket));
        maxPaint.setAntiAlias(true);
                
        for(int i = 0; i < count; i++) {
            int left = (int)(xDelta * i);
            int right = left + xWidth;
            int bottom = height;
            int top = height - (int)(yDelta * mHistogram.getBucketHitCount(i));
            Rect rect = new Rect(left, top, right, bottom);
            canvas.drawRect(rect, ordinalPaint);//was: maxPaint
        }
        
        /* ticks */
        Paint tickPaint = new Paint();
        tickPaint.setColor(res.getColor(R.color.histogram_tick));        
        tickPaint.setAntiAlias(true);
        
        int delta = 10;
        for(int i = delta; i < count; i+=delta) {
            int right = (int)(xDelta * (i - 1)) + xWidth + 1;
            canvas.drawLine(right, 0, right, height, tickPaint);
            
        }
    }

}

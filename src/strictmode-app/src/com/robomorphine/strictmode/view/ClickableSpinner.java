package com.robomorphine.strictmode.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

public class ClickableSpinner extends Spinner {
    private OnClickListener mClickListener;
    
    public ClickableSpinner(Context context) {
        super(context);
    }
    
    public ClickableSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public ClickableSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    @Override
    public void setOnClickListener(OnClickListener listener) {
        mClickListener = listener;
    }
    
    @Override
    public boolean performClick() {
        if(mClickListener != null) {
            mClickListener.onClick(this);
            return true;
        } else {
            return super.performClick();
        }
    }
}


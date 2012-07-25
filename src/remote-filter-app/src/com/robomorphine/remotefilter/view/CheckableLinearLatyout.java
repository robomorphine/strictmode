package com.robomorphine.remotefilter.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.LinearLayout;


public class CheckableLinearLatyout extends LinearLayout implements Checkable{

    public CheckableLinearLatyout(Context context) {
        super(context);
    }

    public CheckableLinearLatyout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckableLinearLatyout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isChecked() {
        return isSelected();
    }

    @Override
    public void setChecked(boolean checked) {
        setSelected(checked);
    }

    @Override
    public void toggle() {
        setChecked(!isChecked());
    };
}

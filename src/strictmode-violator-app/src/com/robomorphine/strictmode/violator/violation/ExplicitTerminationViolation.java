package com.robomorphine.strictmode.violator.violation;


import com.robomorphine.strictmode.violator.R;
import com.robomorphine.strictmode.violator.provider.SimpleProvider;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class ExplicitTerminationViolation extends VmViolation {
    
    public ExplicitTerminationViolation(Context context) {
        super(context, 
                R.drawable.memory_recycle,
                R.string.explicit_termination_name,
                R.string.explicit_termination_descr);
    }
    
    @Override
    public void violate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ContentResolver cr = getContext().getContentResolver(); 
                    cr.query(SimpleProvider.CONTENT_URI, null, null, null, null);
                } catch(Throwable ex) {
                    error(ex.toString());
                }
                System.gc();
            }
        }).start();
    }
    
    private void error(final String error) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        });
    }
}

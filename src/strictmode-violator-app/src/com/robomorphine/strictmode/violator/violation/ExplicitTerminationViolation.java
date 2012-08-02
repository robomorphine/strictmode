package com.robomorphine.strictmode.violator.violation;


import com.robomorphine.strictmode.violator.R;
import com.robomorphine.strictmode.violator.provider.SimpleProvider;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class ExplicitTerminationViolation extends VmViolation {
    
    public ExplicitTerminationViolation(Context context) {
        super(context, 
                R.drawable.violation_type_memory_recycle,
                R.string.explicit_termination_name,
                R.string.explicit_termination_descr);
    }
    
    @SuppressFBWarnings(value="DM_GC")
    @Override
    public void violate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ContentResolver cr = getContext().getContentResolver(); 
                    cr.query(SimpleProvider.CONTENT_URI, null, null, null, null);
                } catch(Throwable ex) {//NOPMD
                    error(ex.toString());
                }
                System.gc();//NOPMD
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

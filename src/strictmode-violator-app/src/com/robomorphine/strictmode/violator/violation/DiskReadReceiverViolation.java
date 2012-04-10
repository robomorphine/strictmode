package com.robomorphine.strictmode.violator.violation;

import com.robomorphine.strictmode.violator.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class DiskReadReceiverViolation extends VmViolation {
    
    private static final String ACTION_NAME = DiskReadReceiverViolation.class.getName();
    
    private static final class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            DiskReadViolation violation = new DiskReadViolation(context);
            violation.violate();
        }
    }
    
    public DiskReadReceiverViolation(Context context) {
        super(context,
              R.drawable.disk_read,
              R.string.broadcast_name, 
              R.string.broadcast_descr);
    }
    
    @Override
    public void violate() {
        Receiver receiver = new Receiver();
        getContext().registerReceiver(receiver, new IntentFilter(ACTION_NAME));
        getContext().sendBroadcast(new Intent(ACTION_NAME));
    }
}

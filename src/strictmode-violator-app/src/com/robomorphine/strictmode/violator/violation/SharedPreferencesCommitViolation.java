package com.robomorphine.strictmode.violator.violation;

import com.robomorphine.strictmode.violator.R;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Random;

public class SharedPreferencesCommitViolation extends ThreadViolation {
    
    private final Random mRandom = new Random();
    
    public SharedPreferencesCommitViolation(Context context) {
        super(context,
              R.drawable.disk_read_write,
              R.string.shared_prefs_commit_name, 
              R.string.shared_prefs_commit_descr);
    }
    
    @Override
    public void violate() {
        String name = SharedPreferencesCommitViolation.class.getName();
        SharedPreferences prefs = getContext().getSharedPreferences(name, Context.MODE_PRIVATE);
        
        int value = mRandom.nextInt();
        prefs.edit()
             .putInt("test", value)
             .commit();
    }

}

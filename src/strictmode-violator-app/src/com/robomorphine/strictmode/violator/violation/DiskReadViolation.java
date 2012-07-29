package com.robomorphine.strictmode.violator.violation;

import com.robomorphine.strictmode.violator.R;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class DiskReadViolation extends ThreadViolation {
    
    public DiskReadViolation(Context context) {
        super(context,
              R.drawable.disk_read,
              R.string.disk_read_name, 
              R.string.disk_read_descr);
    }
        
    @Override
    public void violate() {
        try {
            FileInputStream fin = new FileInputStream("/proc/self/limits");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fin), 100);
            try {
                while(reader.readLine() != null);//NOPMD
            } finally {
                reader.close();
            }
        } catch(IOException ex) {
            Toast.makeText(getContext(), "Failed to read file: " + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }
}

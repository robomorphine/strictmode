package com.robomorphine.strictmode.violator.violation;

import com.robomorphine.strictmode.violator.R;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class DiskWriteViolation extends ThreadViolation {
    
    public DiskWriteViolation(Context context) {
        super(context,
              R.drawable.violation_type_disk_write,
              R.string.disk_write_name, 
              R.string.disk_write_descr);
    }
    
    @Override
    public void violate() {
        try {
            File file = new File(getContext().getFilesDir(), "violation-test");
            FileOutputStream fout = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(fout);
            try {
                for(int i = 0; i < 100; i++) {
                    writer.write("test");
                }
            } finally {
                writer.close();
            }
            
        } catch(IOException ex) {
            Toast.makeText(getContext(), "Failed to write file: " + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }
}

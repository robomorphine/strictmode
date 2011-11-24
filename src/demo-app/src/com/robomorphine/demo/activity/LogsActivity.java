
package com.robomorphine.demo.activity;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.robomorphine.demo.DemoTags;
import com.robomorphine.demo.R;
import com.robomorphine.log.Log;

public class LogsActivity extends Activity {

    private static String TAG = DemoTags.getTag(LogsActivity.class);
    private static String STATE_KEY_COUNTS = "counts";
    private static String STATE_KEY_MARKS = "marks";


    private int[] mCounts = new int[Log.LEVEL_COUNT];
    private int[] mButtons = new int[Log.LEVEL_COUNT];
    private int[] mCheckBoxes = new int[Log.LEVEL_COUNT];
    private Set<Integer> mMarked = new HashSet<Integer>();

    private final String capitalizeWord(String word) {
        String lower = word.toLowerCase();
        return lower.substring(0, 1).toUpperCase() + lower.substring(1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logs);

        for(int level : Log.LEVELS) {
            mCounts[level] = 0;
        }

        mButtons[Log.VERBOSE] = R.id.verboseButton;
        mButtons[Log.DEBUG] = R.id.debugButton;
        mButtons[Log.INFO] = R.id.infoButton;
        mButtons[Log.WARN] = R.id.warningButton;
        mButtons[Log.ERROR] = R.id.errorButton;
        mButtons[Log.FATAL] = R.id.fatalButton;

        mCheckBoxes[Log.VERBOSE] = R.id.verboseCheckBox;
        mCheckBoxes[Log.DEBUG] = R.id.debugCheckBox;
        mCheckBoxes[Log.INFO] = R.id.infoCheckBox;
        mCheckBoxes[Log.WARN] = R.id.warningCheckBox;
        mCheckBoxes[Log.ERROR] = R.id.errorCheckBox;
        mCheckBoxes[Log.FATAL] = R.id.fatalCheckBox;

        for(int level = 0; level < mCheckBoxes.length; level++) {
            TextView txt = (TextView)findViewById(mCheckBoxes[level]);
            txt.setText(capitalizeWord(Log.LEVEL_NAMES[level]));
        }
        updateButtons();
    }

    protected void updateButtons() {
        for(int level = 0; level < mCheckBoxes.length; level++) {
            TextView txt = (TextView)findViewById(mButtons[level]);
            String msg = String.format("%s (%d)", Log.LEVEL_NAMES[level], mCounts[level]);
            txt.setText(capitalizeWord(msg));
        }
    }

    protected void markView(int id, boolean mark) {
        TextView view = (TextView)findViewById(id);
        if(mark) {
            view.setTypeface(Typeface.DEFAULT_BOLD);
            mMarked.add(id);
        } else {
            view.setTypeface(Typeface.DEFAULT);
            mMarked.remove(id);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray(STATE_KEY_COUNTS, mCounts);

        Integer [] srcIds = mMarked.toArray(new Integer[]{});
        int [] ids = new int[srcIds.length];
        for(int i = 0; i < srcIds.length; i++) {
            ids[i] = srcIds[i];
        }
        outState.putIntArray(STATE_KEY_MARKS, ids);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        int[] counts = savedInstanceState.getIntArray(STATE_KEY_COUNTS);
        if (counts != null) {
            mCounts = counts;
        }
        updateButtons();

        int [] markIds = savedInstanceState.getIntArray(STATE_KEY_MARKS);
        for(int i = 0; i < markIds.length; i++) {
            markView(markIds[i], true);
        }
    }

    public void print(int level) {
        String levelName = Log.LEVEL_NAMES[level];
        String msg = String.format("This is demo log on %s level", levelName);
        Log.print(level, TAG, msg);

        mCounts[level]++;
        updateButtons();
    }

    public void onPrintSingleClicked(View v) {
        int btnId = v.getId();
        for (int level = 0; level < mButtons.length; level++) {
            int id = mButtons[level];
            if (id == btnId) {
                print(level);
                markView(id, true);
            } else {
                markView(id, false);
            }
        }
    }

    public void onPrintMultipleClicked(View v) {
        for (int level = 0; level < mCheckBoxes.length; level++) {
            int id = mCheckBoxes[level];
            int btnId = mButtons[level];
            CheckBox chkBox = (CheckBox) findViewById(id);
            if (chkBox.isChecked()) {
                print(level);
                markView(id, true);
                markView(btnId, true);
            } else {
                markView(id, false);
                markView(btnId, false);
            }
        }
    }
}

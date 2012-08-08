package com.robomorphine.strictmode.violator;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.robomorphine.fragment.AboutDialogFragment;
import com.robomorphine.fragment.HelpDialogFragment;

import android.os.Bundle;

public class MainActivity extends SherlockFragmentActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.app_name_title);
        actionBar.setSubtitle(R.string.app_name_subtitle);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                onAbout();
                return true;
            case R.id.help:
                onHelp();
                return true;
        }        
        return super.onOptionsItemSelected(item);
    }
    
    private void onAbout() {
        AboutDialogFragment fragment = new AboutDialogFragment();
        fragment.show(getSupportFragmentManager(), "about");
    }
    
    private void onHelp() {
        HelpDialogFragment fragment = HelpDialogFragment.createFragment(R.raw.help);
        fragment.show(getSupportFragmentManager(), "help");
    }
}
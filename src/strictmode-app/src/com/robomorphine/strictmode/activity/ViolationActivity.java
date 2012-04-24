package com.robomorphine.strictmode.activity;

import com.robomorphine.strictmode.R;
import com.robomorphine.strictmode.fragment.ViolationHeadersFragment;
import com.robomorphine.strictmode.fragment.ViolationStacktraceFragment;
import com.robomorphine.strictmode.fragment.ViolationStatsFragment;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class ViolationActivity extends FragmentActivity implements TabListener {
    
    private final static String STATS_TAB = "stats";
    private final static String STACKTRACE_TAB = "stacktrace";
    private final static String HEADERS_TAB = "headers";
    
    private static class TabInfo {
        public TabInfo(String tag, Class<? extends Fragment> clazz, Bundle args) {
            this.tag = tag;
            this.clazz = clazz;
            this.args = args;
        }
        
        public TabInfo(String tag, Class<? extends Fragment> clazz) {
            this(tag, clazz, null);
        }
        
        final String tag;
        final Class<? extends Fragment> clazz;
        final Bundle args;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.violation_activity);
        
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        Tab tab;
        TabInfo tabInfo;
        
        tabInfo = new TabInfo(STATS_TAB, ViolationStatsFragment.class);
        tab = actionBar.newTab()
                       .setTag(tabInfo)
                       .setTabListener(this)
                       .setText(R.string.violation_stats_tab);                       
        actionBar.addTab(tab);        
        
        tabInfo = new TabInfo(STACKTRACE_TAB, ViolationStacktraceFragment.class);
        tab = actionBar.newTab()
                       .setTag(tabInfo)
                       .setTabListener(this)
                       .setText(R.string.violation_stacktrace_tab);                       
        actionBar.addTab(tab);
        
        tabInfo = new TabInfo(HEADERS_TAB, ViolationHeadersFragment.class);
        tab = actionBar.newTab()
                       .setTag(tabInfo)
                       .setTabListener(this)
                       .setText(R.string.violation_headers_tab);
        actionBar.addTab(tab);
    }
    
    @Override
    public void onTabSelected(Tab tab, android.app.FragmentTransaction nft) {
        TabInfo info = (TabInfo)tab.getTag();
        
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(info.tag);
        if(fragment == null) {
            fragment = Fragment.instantiate(this, info.clazz.getName(), info.args);
        }
        
        
        FragmentTransaction ft = fm.beginTransaction();
        if(fragment.isDetached()) {
            ft.attach(fragment);
        } else {
            ft.add(android.R.id.content, fragment, info.tag);
        }
        ft.commit();
    }
    
    @Override
    public void onTabUnselected(Tab tab, android.app.FragmentTransaction nft) {
        TabInfo info = (TabInfo)tab.getTag();
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(info.tag);
        if(fragment != null && !fragment.isDetached()) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.detach(fragment);
            ft.commit();
        }
    }
    
    @Override
    public void onTabReselected(Tab tab, android.app.FragmentTransaction nft) {
        
    }
}

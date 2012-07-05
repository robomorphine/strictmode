package com.robomorphine.strictmode.activity;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.robomorphine.strictmode.R;
import com.robomorphine.strictmode.fragment.ThreadViolationStatsFragment;
import com.robomorphine.strictmode.fragment.ViolationFragmentHelper;
import com.robomorphine.strictmode.fragment.ViolationHeadersPagerFragment;
import com.robomorphine.strictmode.fragment.ViolationStacktraceFragment;
import com.robomorphine.strictmode.violation.ThreadViolation;
import com.robomorphine.strictmode.violation.Violation;
import com.robomorphine.strictmode.violation.group.ViolationGroup;
import com.robomorphine.strictmode.violation.icon.ViolationIconMap;

import java.util.HashMap;
import java.util.Map;

public class ViolationActivity extends FragmentActivity implements TabListener {
    
    private final static String STATE_SELECTED_TAB = "tab";
    
    /**
     * Type: ViolationGroup
     */
    public final static String EXTRA_VIOLATION_GROUP = "violation_group";
    
    private final static String STATS_TAB = "stats";
    private final static String STACKTRACE_TAB = "stacktrace";
    private final static String HEADERS_TAB = "headers";
    
    private static class TabInfo {
        public TabInfo(String tag, Class<? extends Fragment> clazz, Bundle args) {
            this.tag = tag;
            this.clazz = clazz;
            this.args = args;
        }
        
        public final String tag;
        public final Class<? extends Fragment> clazz;
        public final Bundle args;
    }
    
    private ViolationGroup mViolationGroup;
    
    private Map<String, Tab> mTabs = new HashMap<String, Tab>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.violation_activity);
        
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        mViolationGroup = (ViolationGroup)getIntent().getSerializableExtra(EXTRA_VIOLATION_GROUP);
        if(mViolationGroup == null) {
            throw new IllegalArgumentException("Violation not specified.");
        }
        
        initViolationInfo(mViolationGroup);
        
        if(mViolationGroup.getViolation() instanceof ThreadViolation) {
            addTab(getString(R.string.violation_stats_tab),
                             STATS_TAB, 
                             ThreadViolationStatsFragment.class);
        }
        
        addTab(getString(R.string.violation_stacktrace_tab), 
                         STACKTRACE_TAB,
                         ViolationStacktraceFragment.class);
        
        addTab(getString(R.string.violation_headers_tab), 
                         HEADERS_TAB, 
                         ViolationHeadersPagerFragment.class);
    }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        
        if(savedInstanceState != null) {
            String selectedTag = savedInstanceState.getString(STATE_SELECTED_TAB);
            Tab tab = mTabs.get(selectedTag);
            if(tab != null) {
                getActionBar().selectTab(tab);
            }
        }
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Tab tab = getActionBar().getSelectedTab();
        if(tab != null) {
            TabInfo info = (TabInfo)tab.getTag();
            outState.putString(STATE_SELECTED_TAB, info.tag);
        }
    }
    
    private void initViolationInfo(ViolationGroup violationGroup) {
        Violation violation = violationGroup.getViolation();
        
        ImageView violationIconView = (ImageView)findViewById(R.id.violation_icon);
        ImageView appIconView = (ImageView)findViewById(R.id.application_icon);
        TextView appView = (TextView)findViewById(R.id.application);
        TextView violationView = (TextView)findViewById(R.id.violation);
            
        String appName = null;
        Drawable appIcon = null;
        appIconView.setVisibility(View.VISIBLE);
        PackageManager pm = getPackageManager();
        try {
            ApplicationInfo info = pm.getApplicationInfo(violation.getPackage(), 0);
            appIcon = pm.getApplicationIcon(info);
            appName = pm.getApplicationLabel(info).toString();
        } catch(NameNotFoundException ex) {
            //do nothing
        }
        if(appIcon == null) {
            appIcon = getResources().getDrawable(R.drawable.ic_launcher);
        }
        appIconView.setImageDrawable(appIcon);
                    
        if(TextUtils.isEmpty(appName)) {
            appName = violation.getPackage();
        }
        appView.setText(appName);
            
        ViolationIconMap iconMap = new ViolationIconMap();
        violationIconView.setImageDrawable(iconMap.getIcon(this, violation));
        violationView.setText(violation.getClass().getSimpleName());
    }
    
    private void addTab(String name, String tag, Class<? extends Fragment> clazz) {
        Bundle args = new Bundle();        
        args.putSerializable(ViolationFragmentHelper.EXTRA_VIOLATION_GROUP, mViolationGroup);
        addTab(name, tag, clazz, args);
    }
    
    private void addTab(String name, String tag, Class<? extends Fragment> clazz, Bundle args) {
        ActionBar actionBar = getActionBar();
        TabInfo tabInfo = new TabInfo(tag, clazz, args);
        Tab tab = actionBar.newTab()
                           .setTag(tabInfo)
                           .setTabListener(this)
                           .setText(name);
        actionBar.addTab(tab);
        mTabs.put(tag, tab);
    }
    
    @Override
    public void onTabSelected(Tab tab, android.app.FragmentTransaction nft) {
        TabInfo info = (TabInfo)tab.getTag();
        
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        
        /* Detach visible tab fragments */
        for(String tag : mTabs.keySet()) {
            Fragment fragment = fm.findFragmentByTag(tag);
            if(!tag.equals(info.tag) && fragment != null && !fragment.isDetached()) {
                ft.detach(fragment);
            }
        }
        
        /* Find existing or create new fragment */
        Fragment fragment = fm.findFragmentByTag(info.tag);
        if(fragment == null) {
            fragment = Fragment.instantiate(this, info.clazz.getName(), info.args);            
            ft.add(R.id.container, fragment, info.tag);
        } else if(fragment.isDetached()) {
            ft.attach(fragment);
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
    public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
        
    }
    
}

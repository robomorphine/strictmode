package com.robomorphine.strictmode.viewer.activity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.robomorphine.strictmode.viewer.R;
import com.robomorphine.strictmode.viewer.fragment.ThreadViolationStatsFragment;
import com.robomorphine.strictmode.viewer.fragment.ViolationFragmentHelper;
import com.robomorphine.strictmode.viewer.fragment.ViolationHeadersPagerFragment;
import com.robomorphine.strictmode.viewer.fragment.ViolationStacktraceFragment;
import com.robomorphine.strictmode.viewer.violation.CustomThreadViolation;
import com.robomorphine.strictmode.viewer.violation.DiskReadThreadViolation;
import com.robomorphine.strictmode.viewer.violation.DiskWriteThreadViolation;
import com.robomorphine.strictmode.viewer.violation.ExplicitTerminationVmViolation;
import com.robomorphine.strictmode.viewer.violation.InstanceCountVmViolation;
import com.robomorphine.strictmode.viewer.violation.NetworkThreadViolation;
import com.robomorphine.strictmode.viewer.violation.ThreadViolation;
import com.robomorphine.strictmode.viewer.violation.Violation;
import com.robomorphine.strictmode.viewer.violation.VmViolation;
import com.robomorphine.strictmode.viewer.violation.group.ViolationGroup;
import com.robomorphine.strictmode.viewer.violation.icon.ViolationIconMap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ViolationActivity extends SherlockFragmentActivity implements TabListener {
    
    private final static String STATE_SELECTED_TAB = "tab";
    
    private final static String HELP_DIALOG_FRAGMENT_TAG = "help";
    
    /**
     * Type: ViolationGroup
     */
    public final static String EXTRA_VIOLATION_GROUP = "violation_group";
    
    private final static Map<Class<? extends Violation>, Integer> sViolationHelp;
    static {
        Map<Class<? extends Violation>, Integer> help;
        help = new HashMap<Class<? extends Violation>, Integer>();
        help.put(ThreadViolation.class, R.string.violation_info_thread);
        help.put(CustomThreadViolation.class, R.string.violation_info_thread_custom);
        help.put(DiskReadThreadViolation.class, R.string.violation_info_thread_disk_read);
        help.put(DiskWriteThreadViolation.class, R.string.violation_info_thread_disk_write);
        help.put(NetworkThreadViolation.class, R.string.violation_info_thread_network);
        help.put(VmViolation.class, R.string.violation_info_vm);
        help.put(ExplicitTerminationVmViolation.class, R.string.violation_info_vm_explicit_termination);
        help.put(InstanceCountVmViolation.class, R.string.violation_info_vm_instance_count);
        
        sViolationHelp = Collections.unmodifiableMap(help);
    }    
    
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
    
    public static class ViolationHelpDialog extends DialogFragment {
        
        public static final String EXTRA_TITLE = "title";
        public static final String EXTRA_BODY = "body";
        
        public static ViolationHelpDialog newInstance(String title, String body) {
            Bundle args = new Bundle();
            args.putString(EXTRA_TITLE, title);
            args.putString(EXTRA_BODY, body);
            
            ViolationHelpDialog fragment = new ViolationHelpDialog();
            fragment.setArguments(args);
            return fragment;
        }
        
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String title = null;
            String body = null;
            Bundle args = getArguments();
            if(args != null) {
                title = args.getString(EXTRA_TITLE);
                body = args.getString(EXTRA_BODY);
            }
            
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(title).setMessage(body);
            return builder.create();
        }
    }
    
    private ViolationGroup mViolationGroup;
    
    private final Map<String, Tab> mTabs = new HashMap<String, Tab>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_violation);
        
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
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
        
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            addTab(getString(R.string.violation_headers_tab), 
                             HEADERS_TAB, 
                             ViolationHeadersPagerFragment.class);
        }
    }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        
        if(savedInstanceState != null) {
            String selectedTag = savedInstanceState.getString(STATE_SELECTED_TAB);
            Tab tab = mTabs.get(selectedTag);
            if(tab != null) {
                getSupportActionBar().selectTab(tab);
            }
        }
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Tab tab = getSupportActionBar().getSelectedTab();
        if(tab != null) {
            TabInfo info = (TabInfo)tab.getTag();
            outState.putString(STATE_SELECTED_TAB, info.tag);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {        
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.activity_violation, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            return true;
        } else if(item.getItemId() == R.id.help) {
            showViolationInfoDialog(mViolationGroup);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
    
    private String getViolationHelp(ViolationGroup violationGroup) {
        Violation violation = violationGroup.getViolation();
                
        Class<?> clazz = violation.getClass();
        if(sViolationHelp.containsKey(clazz)) {
            return getString(sViolationHelp.get(clazz));
        }
       
        return getString(R.string.violation_info_not_found);
    }
        
    private void showViolationInfoDialog(ViolationGroup violationGroup) {
        final String title = violationGroup.getViolation().getClass().getSimpleName();
        final String body = getViolationHelp(violationGroup);
        
        DialogFragment dialog = ViolationHelpDialog.newInstance(title, body);
        dialog.show(getSupportFragmentManager(), HELP_DIALOG_FRAGMENT_TAG);
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
        } catch(NameNotFoundException ex) {//NOPMD
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
        ActionBar actionBar = getSupportActionBar();
        TabInfo tabInfo = new TabInfo(tag, clazz, args);
        Tab tab = actionBar.newTab()
                           .setTag(tabInfo)
                           .setTabListener(this)
                           .setText(name);
        actionBar.addTab(tab);
        mTabs.put(tag, tab);
    }
    
    @Override
    public void onTabSelected(Tab tab, FragmentTransaction nft) {
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
    public void onTabUnselected(Tab tab, FragmentTransaction nft) {
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
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        
    }
    
}

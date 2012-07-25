package com.robomorphine.remoteprefs.activity;

import com.robomorphine.remoteprefs.R;
import com.robomorphine.remoteprefs.fragment.DomainListFragment;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class DomainListActivity extends FragmentActivity {
    
    public static final String EXTRA_PACKAGE = "package";
        
    private String mPackage;
    
    private View mPackageInfoLayout;
    private ImageView mApplicationIcon;
    private TextView mApplicationLabel;
    private TextView mPackageTextView;
    private TextView mErrorMessageView;
    
    private DomainListFragment mDomainListFragment;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.domain_list_activity);
        
        mPackage = getIntent().getStringExtra(EXTRA_PACKAGE);
        mPackageInfoLayout = findViewById(R.id.package_info);
        mApplicationIcon = (ImageView) findViewById(R.id.icon);
        mApplicationLabel = (TextView) findViewById(R.id.name);
        mPackageTextView = (TextView) findViewById(R.id.package_name);
        mErrorMessageView = (TextView) findViewById(R.id.error);
        
        FragmentManager fm = getSupportFragmentManager();
        mDomainListFragment = (DomainListFragment)fm.findFragmentById(R.id.domain_list);
                
        init();
    }
    
    private void init() {
        String errorMessage = null;
        if(mPackage != null) {
            mDomainListFragment.setPackage(mPackage);
            
            PackageManager pm = getPackageManager(); 
            try {
                PackageInfo info = pm.getPackageInfo(mPackage, 0);
                mApplicationIcon.setImageDrawable(pm.getApplicationIcon(mPackage));
                mApplicationLabel.setText(mPackage);
                
                CharSequence label = null;
                if(info.applicationInfo != null) {
                     label = pm.getApplicationLabel(info.applicationInfo);
                }
                
                if(label == null) {
                    /* no label, treat package name as application label */
                    mApplicationLabel.setText(mPackage);
                    mPackageTextView.setVisibility(View.GONE);
                } else {
                    /* has label, show both label and package name */
                    mApplicationLabel.setText(label);
                    mPackageTextView.setText(mPackage);
                }
                
            } catch (NameNotFoundException ex) {
                errorMessage = getString(R.string.package_not_found_args, mPackage);
            }
        } else {
            errorMessage = getString(R.string.package_not_specified);
        }
        
        if(errorMessage != null) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
              .remove(mDomainListFragment)
              .commit();
            
            mPackageInfoLayout.setVisibility(View.GONE);
            mErrorMessageView.setVisibility(View.VISIBLE);
            mErrorMessageView.setText(errorMessage);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.domain_list, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_generate) {
            Random random = new Random();
            String name = "shared-prefs-" + random.nextInt(1000);
            getSharedPreferences(name, 0);//.edit().commit();
        }
        return super.onOptionsItemSelected(item);
    }
}
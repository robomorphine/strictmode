package com.robomorphine.strictmode.viewer.fragment;

import com.actionbarsherlock.app.SherlockFragment;
import com.robomorphine.strictmode.viewer.R;
import com.robomorphine.strictmode.viewer.adapter.ViolationHeadersPagerAdapter;
import com.robomorphine.strictmode.viewer.violation.group.ViolationGroup;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ViolationHeadersPagerFragment extends SherlockFragment implements OnPageChangeListener {
    
    private Handler mHandler;
    private ViolationGroup mViolationGroup;
    private ViewPager mViewPager;
    private ViolationHeadersPagerAdapter mAdapter;
    private TextView mViewPagerPosition;
    
    private final Runnable mSetAdapterRunnable = new Runnable() {
        @Override
        public void run() {
            mViewPager.setAdapter(mAdapter);
        }
    };
    
    private final Runnable mUnsetAdapterRunnable = new Runnable() {
        @Override
        public void run() {
            mViewPager.setAdapter(null);
        }
    };
        
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setHasOptionsMenu(true);
        mViolationGroup = ViolationFragmentHelper.getViolationGroup(getArguments());
        mAdapter = new ViolationHeadersPagerAdapter(getFragmentManager(), mViolationGroup);
        mHandler = new Handler();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_violation_headers_pager, container, false);
                
        mViewPagerPosition = (TextView)view.findViewById(R.id.pager_position);
        mViewPager = (ViewPager)view.findViewById(R.id.pager);        
                
        Resources res = getResources();
        ColorDrawable drawable = new ColorDrawable(Color.GRAY);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setPageMarginDrawable(drawable);
        mViewPager.setPageMargin(res.getDimensionPixelSize(R.dimen.small));
        
        mHandler.post(mSetAdapterRunnable);
        return view;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        onPageSelected(mViewPager.getCurrentItem());
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();       
        mHandler.post(mUnsetAdapterRunnable);
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        /* These callbacks should not be called _after_ fragment is destroyed */
        mHandler.removeCallbacks(mSetAdapterRunnable);
        mHandler.removeCallbacks(mUnsetAdapterRunnable);
    }
    
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        
    }
    
    @Override
    public void onPageScrollStateChanged(int state) {        
    }
    
    @Override
    public void onPageSelected(int position) {
        String text = getString(R.string.page_position, (position + 1), mViolationGroup.getSize());
        mViewPagerPosition.setText(text);
    }
}

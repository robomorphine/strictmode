package com.robomorphine.strictmode.fragment;

import com.robomorphine.strictmode.R;
import com.robomorphine.strictmode.violation.ThreadViolation;
import com.robomorphine.strictmode.violation.Violation;
import com.robomorphine.strictmode.violation.group.ViolationGroup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class ThreadViolationStatsFragment extends ViolationDetailsFragment {
    
    private TextView mSizeTextView;
    private TextView mTotalTextView;
    private TextView mMinTextView;
    private TextView mMaxTextView;
    private TextView mAvgTextView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViolationGroup violationGroup = getViolation();
        if(!(violationGroup.getViolation() instanceof ThreadViolation)) {
            throw new IllegalArgumentException("Only ThreadViolations are acceptable.");
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.thread_violation_stats, container, false);
        mSizeTextView = (TextView)view.findViewById(R.id.size);
        mTotalTextView = (TextView)view.findViewById(R.id.total_duration);
        mMinTextView = (TextView)view.findViewById(R.id.min_duration);
        mMaxTextView = (TextView)view.findViewById(R.id.max_duration);
        mAvgTextView = (TextView)view.findViewById(R.id.avg_duration);
        return view;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateStats();
    }
    
    private void updateStats() {
                
        ViolationGroup group = getViolation();
        List<Violation> violations = group.getViolations();
        
        
        long total = 0;
        long min = 0;
        long max = 0;
        
        if(violations.size() > 0) {
            ThreadViolation threadViolation = (ThreadViolation)violations.get(0);
            min = threadViolation.getDuration();
            max = threadViolation.getDuration();
        }
                
        for(Violation violation : violations) {
            ThreadViolation threadViolation = (ThreadViolation)violation;
            long duration = threadViolation.getDuration();
            total += duration;
            if(min > duration) {
                min = duration;
            }
            if(max < duration) {
                max = duration;
            }
        }
        
        long avg = total / violations.size();
        
        mSizeTextView.setText(Integer.toString(violations.size()));
        mTotalTextView.setText(getString(R.string.thread_violation_duration_args, total));
        mMinTextView.setText(getString(R.string.thread_violation_duration_args, min));
        mMaxTextView.setText(getString(R.string.thread_violation_duration_args, max));
        mAvgTextView.setText(getString(R.string.thread_violation_duration_args, avg));
    }
}

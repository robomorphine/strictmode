package com.robomorphine.strictmode.fragment;

import com.robomorphine.strictmode.R;
import com.robomorphine.strictmode.histogram.Histogram;
import com.robomorphine.strictmode.histogram.HistogramView;
import com.robomorphine.strictmode.violation.ThreadViolation;
import com.robomorphine.strictmode.violation.Violation;
import com.robomorphine.strictmode.violation.group.ViolationGroup;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class ThreadViolationStatsFragment extends ViolationDetailsFragment {

    private static class HistogramRange {        
        private final int min;
        private final int max;
        private final int buckets;
        
        public HistogramRange(int min, int max, int buckets) {
            this.min = min;
            this.max = max;
            this.buckets = buckets;
        }  
        
        public boolean matches(int min, int max) {
            return this.min <= min && max <= this.max;
        }
        
        public Histogram createHistogram() {
            int delta = (max - min) / buckets;
            if(delta > 5) {
                //should be multiple of 5
                delta = (delta / 5) * 5; 
            }
            return new Histogram(min, max, delta);
        }
    }
    
    private static List<HistogramRange> sHistogramRanges;
    static {
        List<HistogramRange> ranges = new LinkedList<HistogramRange>();
        ranges.add(new HistogramRange(0,    50,   25));
        ranges.add(new HistogramRange(0,    100,  50));
        ranges.add(new HistogramRange(0,    250,  50));
        ranges.add(new HistogramRange(0,    500,  50));
        ranges.add(new HistogramRange(0,    1000, 50));
        ranges.add(new HistogramRange(1000, 2000, 50));
        ranges.add(new HistogramRange(0,    2000, 50));
        ranges.add(new HistogramRange(4000, 5000, 50));
        ranges.add(new HistogramRange(2500, 5000, 50));
        ranges.add(new HistogramRange(0,    5000, 50));
        sHistogramRanges = Collections.unmodifiableList(ranges);
    }
    
    private static Histogram createHistogram(int min, int max) {
        HistogramRange selectedRange = null;
        for(HistogramRange range : sHistogramRanges) {
            if(range.matches(min, max)) {
                selectedRange = range;
                break;
            }
        }
        if(selectedRange == null) {
            selectedRange = new HistogramRange((int)(min * 0.9), (int)(max * 1.1), 50);
        }
        return selectedRange.createHistogram();
    }
    
    private TextView mSizeTextView;
    private TextView mTotalTextView;
    private TextView mMinTextView;
    private TextView mMaxTextView;
    private TextView mAvgTextView;
    
    private TextView mHistogramRangeView;
    private TextView mHistogramDeltaView;
    private TextView mHistogram90View;
    private TextView mHistogram95View;
    private TextView mHistogramAvgView;
    private HistogramView mHistogramView;
    
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
        View view = inflater.inflate(R.layout.thread_violation_stats_fragment, container, false);
        mSizeTextView = (TextView)view.findViewById(R.id.size);
        mTotalTextView = (TextView)view.findViewById(R.id.total_duration);
        mMinTextView = (TextView)view.findViewById(R.id.min_duration);
        mMaxTextView = (TextView)view.findViewById(R.id.max_duration);
        mAvgTextView = (TextView)view.findViewById(R.id.avg_duration);
        
        mHistogramRangeView = (TextView)view.findViewById(R.id.hist_range);
        mHistogramDeltaView = (TextView)view.findViewById(R.id.hist_delta);
        mHistogram90View = (TextView)view.findViewById(R.id.hist_90);
        mHistogram95View = (TextView)view.findViewById(R.id.hist_95);
        mHistogramAvgView = (TextView)view.findViewById(R.id.hist_avg);
        mHistogramView = (HistogramView)view.findViewById(R.id.histogram);
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
        
        Histogram histogram = createHistogram((int)min, (int)max);
        for(Violation violation : violations) {
            ThreadViolation threadViolation = (ThreadViolation)violation;
            histogram.add((int)threadViolation.getDuration());
        }
        mHistogramView.setHistogram(histogram);
        int histMin = histogram.getMin();
        int histMax = histogram.getMax();
        mHistogramRangeView.setText(getString(R.string.histogram_range, histMin, histMax));
        mHistogramDeltaView.setText(getString(R.string.histogram_value, histogram.getDelta()));
        
        Pair<Integer, Integer> hist90 = histogram.coverage(0.9f);
        int hist90min = histogram.getBucketMin(hist90.first);
        int hist90max = histogram.getBucketMax(hist90.second);
        mHistogram90View.setText(getString(R.string.histogram_range, hist90min, hist90max));
        
        Pair<Integer, Integer> hist95 = histogram.coverage(0.95f);
        int hist95min = histogram.getBucketMin(hist95.first);
        int hist95max = histogram.getBucketMax(hist95.second);
        mHistogram95View.setText(getString(R.string.histogram_range, hist95min, hist95max));
        
        mHistogramAvgView.setText(getString(R.string.histogram_value, (hist90min + hist90max) / 2));
    }
}

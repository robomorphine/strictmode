package com.robomorphine.strictmode.histogram;

import android.util.Pair;

import java.util.Observable;


//TODO: unit-test this
public class Histogram extends Observable {
    
    private final int mMin;
    private final int mMax;
    private final int mBucketDelta;
    private final int [] mCounts;    
    
    public Histogram(int min, int max, int bucketSize) {
        int delta = max - min;
        int bucketCount = delta / bucketSize;
        if(delta % bucketSize > 0) {
            bucketCount++;
        }
        
        mMin = min;
        mMax = max;
        mBucketDelta = bucketSize;
        mCounts = new int [bucketCount];
    }
    
    public boolean add(int value) {
        if(value < mMin || value >= mMax) {
            return false;
        }        
        mCounts[(value - mMin) / mBucketDelta]++;
        setChanged();
        notifyObservers();
        return true;
    }
    
    public int getMin() {
        return mMin;
    }
    
    public int getMax() {
        return mMax;
    }
    
    public int getDelta() {
        return mBucketDelta;
    }
    
    public int getMiddle() {
        return (mMax + mMin) / 2; 
    }
        
    public int getBucketCount() {
        return mCounts.length;
    }    
    
    /**
     * Returns minimum value that's required to get into bucket @param bucketNumber.
     */
    public int getBucketMin(int bucketNumber) {
        if(bucketNumber < 0 || bucketNumber >= mCounts.length) {
            return -1;
        }
        return bucketNumber * mBucketDelta + mMin; 
    }
    
    /**
     * Returns maximum value that's still can get into the bucket @param bucketNumber.
     */
    public int getBucketMax(int bucketNumber) {
        if(bucketNumber < 0 || bucketNumber >= mCounts.length) {
            return -1;
        }
        return Math.min(mMax, (bucketNumber + 1) * mBucketDelta);
    }
    
    /**
     * Return middle of bucket @param bucketNumber range.
     */
    public int getBucketMiddle(int bucketNumber) {
        if(bucketNumber < 0 || bucketNumber >= mCounts.length) {
            return -1;
        }
        return (getBucketMax(bucketNumber) + getBucketMin(bucketNumber)) / 2;
    }
    
    /**
     * Returns number of hits for specified @param bucketNumber 
     */
    public int getBucketHitCount(int bucketNumber) {
        if(bucketNumber < 0 || bucketNumber >= mCounts.length) {
            return 0;    
        } 
        return mCounts[bucketNumber];
    }
    
    
    /**
     * Return bucket with maximum number of hits. 
     * If multiple maximums exists, return first found maximum.
     */
    public int getMaxHitCountBucket() {
        int maxIndex = 0;
        for(int i = 1; i < mCounts.length; i++) {
            if(mCounts[i] > mCounts[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }
    
    /**
     * Searches for bucket with maximum number of hits and returns its hit count. 
     */
    public int getMaxHitCount() {
        return mCounts[getMaxHitCountBucket()];
    }
    
    /**
     * Return total number of samples (hits) stored in this histogram.
     */
    public int getTotalHitCount() {
        int total = 0;
        for(int i = 0; i < mCounts.length; i++) {
            total += mCounts[i];
        }
        return total;
    }
    
    /**
     * Resets histogram.
     */
    public void clear() {
        for(int i = 0; i < mCounts.length; i++) {
            mCounts[i] = 0;
        }
        setChanged();
        notifyObservers();
    }
    
    /**
     * Calculates minimum bucket range that covers @param percents of all hits.
     */
    public Pair<Integer, Integer> coverage(float percents) {
        int total = getTotalHitCount();
        int current = 0;        
        int index = 0;        
        int length = 0;
        
        while(current < total * percents) {
            length++;
            int max = 0;
            int maxIndex = 0;
            for(int i = 0; i < mCounts.length - length + 1; i++) {
                int sum = 0;
                for(int b = i; b < length + i; b++) {
                    sum+= mCounts[b];
                }
                if(sum > max) {
                    max = sum;
                    maxIndex = i;
                }
            }            
            current = max;
            index = maxIndex; 
        }
        return new Pair<Integer, Integer>(index, index + length - 1);
    }
    
    public String save() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%s", mMin));
        builder.append(String.format(";%s", mMax));
        builder.append(String.format(";%s", mBucketDelta));
        for(int i = 0; i < mCounts.length; i++) {
            builder.append(String.format(";%s", mCounts[i]));
        }
        return builder.toString(); 
    }
    
    public static Histogram restore(String histStr) {
        String [] numbers = histStr.split("\\;");
        if(numbers.length < 3) {
            throw new IllegalArgumentException();
        }        
        int min = Integer.parseInt(numbers[0]); 
        int max = Integer.parseInt(numbers[1]);
        int delta = Integer.parseInt(numbers[2]);
        Histogram hist = new Histogram(min, max, delta);
        if(numbers.length < hist.mCounts.length + 3) {
            throw new IllegalArgumentException();
        }
        for(int i = 0; i < hist.mCounts.length; i++) {
            hist.mCounts[i] = Integer.parseInt(numbers[i+3]);
        }
        return hist;
    }
}

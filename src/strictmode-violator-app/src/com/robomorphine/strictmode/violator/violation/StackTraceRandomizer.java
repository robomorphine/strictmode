package com.robomorphine.strictmode.violator.violation;

import java.util.Random;

public class StackTraceRandomizer {//NOPMD
        
    private final Random mRandom = new Random();
    
    public void call(int depth, Runnable runnable) {//NOPMD
        if(depth == 0) {
            runnable.run();
            return;
        }
        
        int choice = mRandom.nextInt(10);
        switch(choice) {
            case 0:
                f0(depth, runnable);
                break;
            case 1:
                f1(depth, runnable);
                break;
            case 2:
                f2(depth, runnable);
                break;
            case 3:
                f3(depth, runnable);
                break;
            case 4:
                f4(depth, runnable);
                break;
            case 5:
                f5(depth, runnable);
                break;
            case 6:
                f6(depth, runnable);
                break;
            case 7:
                f7(depth, runnable);
                break;
            case 8:
                f8(depth, runnable);
                break;
            case 9:
                f9(depth, runnable);
                break;
            default:
                f0(depth, runnable);
                break;
        }
    }
    
    private void f0(int depth, Runnable runnable) {
        call(depth - 1, runnable);
    }
    
    private void f1(int depth, Runnable runnable) {
        call(depth - 1, runnable);
    }
    
    private void f2(int depth, Runnable runnable) {
        call(depth - 1, runnable);
    }
    
    private void f3(int depth, Runnable runnable) {
        call(depth - 1, runnable);
    }
    
    
    private void f4(int depth, Runnable runnable) {
        call(depth - 1, runnable);
    }
    
    private void f5(int depth, Runnable runnable) {
        call(depth - 1, runnable);
    }
    
    private void f6(int depth, Runnable runnable) {
        call(depth - 1, runnable);
    }
    
    
    private void f7(int depth, Runnable runnable) {
        call(depth - 1, runnable);
    }
    
    private void f8(int depth, Runnable runnable) {
        call(depth - 1, runnable);
    }            
    
    private void f9(int depth, Runnable runnable) {
        call(depth - 1, runnable);
    }

}

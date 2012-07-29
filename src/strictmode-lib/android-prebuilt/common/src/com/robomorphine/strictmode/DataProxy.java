package com.robomorphine.strictmode;

public interface DataProxy<T> {
    T handle(T in);
}

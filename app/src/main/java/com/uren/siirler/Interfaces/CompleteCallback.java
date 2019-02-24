package com.uren.siirler.Interfaces;

public interface CompleteCallback<T> {
    void onComplete(T object);
    void onFailed(Exception e);
}

package com.example.timer;


public interface TimerCountDownCallback {
    void onStart();
    void onPause();
    void onCancel();
    void onCompleted();
    void onRunning(String stamp,boolean enable);
}

package com.example.timer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.IdRes;

public class ProxyTimeLogic implements TimerCountDownCallback {
    private ActivityProvider activityProvider;
    private TimerView mTimerView;
    private Context mContext;

    public ProxyTimeLogic(ActivityProvider activityProvider, Bundle savedInstanceState) {
        this.activityProvider = activityProvider;
        initView();
    }

    private void initView() {
        mTimerView = activityProvider.findViewById(R.id.tabTimer);
        mContext = mTimerView.getContext();
        mTimerView.setTimerCountDownCallback(this);
    }

    @Override
    public void onStart() {
        showNotificationDialog();
        showFloatView();
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onCompleted() {
        showToast();
    }

    @Override
    public void onRunning(String stamp,boolean enable) {
        FloatViewWindowManager.updateUsedPercent(stamp,enable);
    }

    private void showToast() {
        View toastView = FloatViewWindowManager.createToastView(mContext.getApplicationContext());
        toastView.setOnClickListener(v ->{
            mContext.startActivity(new Intent(mContext,MainActivity.class));
            FloatViewWindowManager.getWindowManager(mContext.getApplicationContext()).removeView(toastView);
        });
    }

    private void showNotificationDialog() {
        NotificationBuilder.from(mContext).createNotification();
    }

    private void showFloatView() {
        Intent intent = new Intent(mContext, FloatWindowService.class);
        mContext.startService(intent);
    }

    public interface ActivityProvider {
        <T extends View> T findViewById(@IdRes int id);
    }

}

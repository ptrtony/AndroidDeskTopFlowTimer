package com.example.timer;

import android.annotation.SuppressLint;
import android.widget.LinearLayout;
import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

public class TimerView extends LinearLayout {
    public TimerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TimerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TimerView(Context context) {
        super(context);
    }

    private TimerCountDownCallback mTimerCountDownCallback;
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        btnPause = (Button) findViewById(R.id.btnPause);
        btnPause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mTimerCountDownCallback!=null){
                    mTimerCountDownCallback.onPause();
                }
                stopTimer();
                btnPause.setVisibility(View.GONE);
                btnStart.setVisibility(View.VISIBLE);
                btnReset.setVisibility(View.VISIBLE);
            }
        });
        btnReset = (Button) findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mTimerCountDownCallback!=null){
                    mTimerCountDownCallback.onCancel();
                }
                stopTimer();

                etHour.setText("0");
                etMin.setText("0");
                etSec.setText("0");

                btnReset.setVisibility(View.GONE);
                btnPause.setVisibility(View.GONE);
                btnStart.setVisibility(View.VISIBLE);
            }
        });

        btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startTimer();
                if (mTimerCountDownCallback!=null){
                    mTimerCountDownCallback.onStart();
                    enable = false;
                }
                btnStart.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
                btnReset.setVisibility(View.VISIBLE);
            }
        });


        etHour = (EditText) findViewById(R.id.etHour);
        etMin = (EditText) findViewById(R.id.etMin);
        etSec = (EditText) findViewById(R.id.etSec);

        etHour.setText("00");
        etHour.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    int value = Integer.parseInt(s.toString());

                    if (value > 59) {
                        etHour.setText("59");
                    } else if (value < 0) {
                        etHour.setText("0");
                    }
                }
                checkToEnableBenStart();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etMin.setText("00");
        etMin.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    int value = Integer.parseInt(s.toString());

                    if (value > 59) {
                        etMin.setText("59");
                    } else if (value < 0) {
                        etMin.setText("0");
                    }
                }
                checkToEnableBenStart();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etSec.setText("00");
        etSec.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    int value = Integer.parseInt(s.toString());

                    if (value > 59) {
                        etSec.setText("59");
                    } else if (value < 0) {
                        etSec.setText("0");
                    }
                }
                checkToEnableBenStart();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnStart.setVisibility(View.VISIBLE);
        btnStart.setEnabled(false);
        btnPause.setVisibility(View.GONE);
        btnReset.setVisibility(View.GONE);
    }

    private void checkToEnableBenStart() {
        btnStart.setEnabled((!TextUtils.isEmpty(etHour.getText()) && Integer.parseInt(etHour.getText().toString()) > 0) ||

                (!TextUtils.isEmpty(etMin.getText()) && Integer.parseInt(etMin.getText().toString()) > 0) ||

                (!TextUtils.isEmpty(etSec.getText()) && Integer.parseInt(etSec.getText().toString()) > 0));
    }

    private void startTimer() {

        allTimerCount = Integer.parseInt(etHour.getText().toString()) * 60 * 60 + Integer.parseInt(etMin.getText().toString()) * 60 + Integer.parseInt(etSec.getText().toString());
        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    allTimerCount--;
                    handler.sendEmptyMessage(MSG_WHAT_TIME_TICK);
                    if (allTimerCount == 0) {
                        handler.sendEmptyMessage(MSG_WHAT_TIME_IS_UP);
                        stopTimer();
                    }
                }

            };

            if (timer == null){
                timer = new Timer();
            }

            timer.schedule(timerTask, 1000, 1000);
        }

    }


    private void stopTimer() {
        if (timerTask != null) {
            timer.cancel();
            timer = null;
            timerTask = null;
        }
    }
    private boolean enable = false;
    @SuppressLint("HandlerLeak")
    private final android.os.Handler handler = new android.os.Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_WHAT_TIME_TICK:
                    String hourStr,minStr,secStr;
                    int hour = allTimerCount / 60 / 60;
                    int min = (allTimerCount / 60) % 60;
                    int sec = allTimerCount % 60;

                    if (hour ==  0 && min ==  0 && sec == 0){
                        if (mTimerCountDownCallback!=null){
                            mTimerCountDownCallback.onCompleted();
                            enable = true;
                        }
                    }else{
                        enable = false;
                    }


                    if (hour < 10){
                        hourStr = "0" + hour;
                    }else{
                        hourStr = String.valueOf(hour);
                    }


                    if (min < 10){
                        minStr = "0" + min;
                    }else{
                        minStr = String.valueOf(min);
                    }

                    if (sec < 10){
                        secStr = "0" + sec;
                    }else{
                        secStr = String.valueOf(sec);
                    }

                    if (mTimerCountDownCallback != null){
                        mTimerCountDownCallback.onRunning(hourStr +":" + minStr + ":" + secStr,enable);
                    }
                    etHour.setText(hourStr);
                    etMin.setText(minStr);
                    etSec.setText(secStr);
                    break;

                case MSG_WHAT_TIME_IS_UP:

                    /*NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext())
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("TimeKeeper")
                            .setContentText("IT'S TIME TO PUT DOWN THE PHONE!")
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setPriority(Notification.PRIORITY_HIGH);

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());

                    //Transfer notify
                    notificationManager.notify(2, builder.build());*/

//                    new AlertDialog.Builder(getContext())
//                            .setIcon(R.mipmap.ic_launcher)
//                            .setTitle("TIMEKEEPER")
//                            .setMessage("IT'S TIME TO PUT DOWN THE PHONE! TIME IS MONEY!")
//                            .setNegativeButton("BACK", null)
//                            .show();
                    btnStart.setVisibility(View.VISIBLE);
                    btnPause.setVisibility(View.GONE);
                    btnReset.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }

    };


    private Button btnStart, btnPause, btnReset;
    private EditText etHour, etMin, etSec;
    private Timer timer;
    private TimerTask timerTask = null;
    private int allTimerCount = 0;
    private static final int MSG_WHAT_TIME_IS_UP = 1;
    private static final int MSG_WHAT_TIME_TICK = 2;


    public void setTimerCountDownCallback(TimerCountDownCallback mTimerCountDownCallback){
        this.mTimerCountDownCallback = mTimerCountDownCallback;
    }
}
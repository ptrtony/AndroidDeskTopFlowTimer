package com.example.timer;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;

import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Date;

public class AlarmView extends LinearLayout {

    public AlarmView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public AlarmView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AlarmView(Context context) {
        super(context);
        init();
    }

    private void init() {
        alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        btAddAlarm = (Button) findViewById(R.id.btnAddAlarm);
        lvAlarmList = (ListView) findViewById(R.id.lvAlarmList);

        adapter = new ArrayAdapter<AlarmData>(getContext(), android.R.layout.simple_list_item_1);
        lvAlarmList.setAdapter(adapter);
        readSavedAlarmList();

        btAddAlarm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addAlarm();
            }
        });

        lvAlarmList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(getContext()).setTitle("OPTION").setItems(new CharSequence[]{"DELETE"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                deleteAlarm(position);
                                break;
                            default:
                                break;
                        }
                    }
                }).setNegativeButton("CANCEL", null).show();
                return true;
            }
        });
    }

    private void deleteAlarm(int position) {
        AlarmData ad = adapter.getItem(position);
        adapter.remove(ad);
        saveAlarmList();

        alarmManager.cancel(PendingIntent.getBroadcast(getContext(), ad.getId(), new Intent(getContext(), AlarmReceiver.class), 0));
    }

    private void addAlarm() {
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                Calendar currentTime = Calendar.getInstance();
                if (calendar.getTimeInMillis() <= currentTime.getTimeInMillis()) {
                    calendar.setTimeInMillis(calendar.getTimeInMillis() + 24 * 60 * 60 * 1000);
                }
                AlarmData ad = (new AlarmData(calendar.getTimeInMillis()));
                adapter.add(ad);
                alarmManager.set(AlarmManager.RTC_WAKEUP,
                        ad.getTime(),
                        PendingIntent.getBroadcast(getContext(),
                                ad.getId(),
                                new Intent(getContext(), AlarmReceiver.class), 0));
                saveAlarmList();

            }
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();

    }

    private void saveAlarmList() {

        SharedPreferences.Editor editor = getContext().getSharedPreferences(AlarmView.class.getName(), Context.MODE_PRIVATE).edit();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < adapter.getCount(); ++i) {
            sb.append(adapter.getItem(i).getTime()).append(",");
        }
        if (sb.length() > 1) {
            String content = sb.toString().substring(0, sb.length() - 1);
            editor.putString(KEY_ALARM_LIST, content);
            System.out.println(content);
        } else {
            editor.putString(KEY_ALARM_LIST, null);
        }
        editor.commit();

    }

    private void readSavedAlarmList() {
        SharedPreferences sp = getContext().getSharedPreferences(AlarmView.class.getName(), Context.MODE_PRIVATE);
        String content = sp.getString(KEY_ALARM_LIST, null);

        if (content != null) {
            String[] timeString = content.split(",");
            for (String string : timeString) {
                adapter.add(new AlarmData(Long.parseLong(string)));
            }
        }
    }


    private static final String KEY_ALARM_LIST = "alarmlist";
    private Button btAddAlarm;
    private ListView lvAlarmList;
    private ArrayAdapter<AlarmData> adapter;
    private AlarmManager alarmManager;

    private static class AlarmData {
        public AlarmData(long time) {
            this.time = time;
            date = Calendar.getInstance();
            date.setTimeInMillis(time);

            timeLable = String.format("%d/%d  %d:%d",
                    date.get(Calendar.MONTH) + 1,
                    date.get(Calendar.DAY_OF_MONTH),
                    date.get(Calendar.HOUR_OF_DAY),
                    date.get(Calendar.MINUTE));

        }

        public long getTime() {
            return time;
        }

        public String getTimeLable() {
            return timeLable;
        }

        @Override
        public String toString() {
            return getTimeLable();
        }

        public int getId() {
            return (int) (getTime() / 1000 / 60);

        }

        private String timeLable = "";
        private long time = 0;
        private Calendar date;
    }

}

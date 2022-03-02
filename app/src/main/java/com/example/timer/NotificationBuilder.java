package com.example.timer;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

public class NotificationBuilder {

    @SuppressLint("StaticFieldLeak")
    private static NotificationBuilder notificationBuilder = null;
    public static final String CHANNEL_ID = "channel_1";
    public static final String CHANNEL_DESC = "143";
    private Context mContext;
    private NotificationBuilder(Context context){
        this.mContext = context;

    }


    public static NotificationBuilder from(Context context){
        if (notificationBuilder == null){
            notificationBuilder = new NotificationBuilder(context);
        }
        return notificationBuilder;
    }


    public void createNotification(){
        NotificationManager manager = (NotificationManager) mContext.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE);
        //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.baidu.com"));
        //PendingIntent pendingIntent = PendingIntent.getActivity(mContext,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        // Create an Intent for the activity you want to start
        Intent resultIntent = new Intent(mContext, MainActivity.class);
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext.getApplicationContext());
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_DESC, importance);
            manager.createNotificationChannel(channel);
            Notification notification = new Notification.Builder(mContext, CHANNEL_ID)
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("This is a content title")
                    .setContentText("This is a content text")
                    .setContentIntent(pendingIntent)
                    .setStyle(new Notification.BigTextStyle().bigText("Much longer text that cannot fit one line..."))
                    .setOngoing(true)
                    .build();
            manager.notify(2, notification);
        }
        else {
            //当sdk版本小于O
            Notification notification = new NotificationCompat.Builder(mContext)
                    .setContentTitle("This is content title")
                    .setContentText("This is content text")
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setOngoing(true)
                    .build();
            manager.notify(2,notification);
        }

    }
}

package com.berkal.audmonitor;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.berkal.audmonitor.activities.ActivityMain;
import com.berkal.audmonitor.background.ServiceMain;

public class App extends Application
{
    public static final String TAG = App.class.getSimpleName();

    public static final int SERVICE_NOTIFY_ID = 32;
    public static final int CHANGE_NOTIFY_ID = 33;

    public static final String ACTION_START = "start";
    public static final String ACTION_ALARM = "alarm";
    public static final String ACTION_BOOT = "boot";
    public static final String ACTION_TIMING = "timing";

    public static final String EVENT_UPDATE = "update";
    public static final String CHANNEL_ID = "channel";


    public static final long    INTERVAL = 10 * 1000;
    public static final float   INCREMENT = 0.005f;
    public static final int     PROGRESS_MAX = 100;
    public static final float   PERCENT_MAX = 0.5f;

    public static final String URL = "https://www.plus500.com.au/Trading/Forex";

    public static SQLiteDatabase appDb;
    public static String appName;

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.d(TAG, "Starting...");

        appName = getString(R.string.app_name);
    }

    @Override
    public void onTerminate()
    {
        super.onTerminate();
        Log.d(TAG, "Terminating...");
    }


    public static void serviceStart(Context context, String action)
    {
        Intent intent = new Intent(context, ServiceMain.class);
        intent.setAction(action);
        context.startService(intent);
    }

    public static void unnotify(Context context, int id)
    {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(id);
    }

    public static NotificationCompat.Builder buildNotification(Context context, String title, String message)
    {
        Intent intent = new Intent(context, ActivityMain.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setAction(Long.toString(System.currentTimeMillis()));

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, App.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationChannel channel = new NotificationChannel(App.CHANNEL_ID, App.appName, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Notification Channel");
            channel.setShowBadge(true);

            notificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channel.getId());
        }

        return mBuilder;
    }
}

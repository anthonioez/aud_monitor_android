package com.berkal.audmonitor.background;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;

import com.berkal.audmonitor.App;
import com.berkal.audmonitor.Prefs;
import com.berkal.audmonitor.R;
import com.berkal.audmonitor.Utils;
import com.berkal.audmonitor.activities.ActivityMain;
import com.berkal.audmonitor.http.HttpResponse;
import com.berkal.audmonitor.http.HttpUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ServiceMain extends Service
{
    private static final String TAG = ServiceMain.class.getSimpleName();

    private static AlarmManager alarmManager;

    private static PendingIntent pendingIntent;

    private UpdateTask task = null;

    @Override
    public void onCreate()
    {
        super.onCreate();

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent alarmIntent = new Intent(this, ServiceMain.class);
        alarmIntent.setAction(App.ACTION_ALARM);
        pendingIntent = PendingIntent.getService(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = App.buildNotification(this, App.appName, "Running");
        startForeground(App.SERVICE_NOTIFY_ID, builder.build());
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        unupdate();
    }

    @Override
    public IBinder onBind(final Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if (intent != null)
        {
            String action = intent.getAction();

            if (action.equals(App.ACTION_BOOT))
            {
                boot();
            }
            else if (action.equals(App.ACTION_ALARM))
            {
                alarm();
            }
            else if (action.equals(App.ACTION_TIMING))
            {
                timing();
            }
            else if (action.equals(App.ACTION_START))
            {
                start();
            }
        }
        else
        {
            timing();
        }
        return START_STICKY;
    }

    public void boot()
    {
        start();
    }

    public void alarm()
    {
        update();
    }

    public void timing()
    {
        long now = System.currentTimeMillis();

        long stamp = now + App.INTERVAL;

        alarmManager.cancel(pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, stamp, App.INTERVAL, pendingIntent);

        Log.i(TAG, "w:" + Utils.getDateTimeFormat(now) + " x: " + Utils.getDateTimeFormat(stamp));
    }

    private void start()
    {
        timing();

        update();
    }

    private void unupdate()
    {
        if(task != null)
        {
            task.cancel(true);
            task = null;
        }
    }

    private void update()
    {
        if(task != null)
        {
            return;
        }

        task = new UpdateTask(this);
        task.execute();
    }

    public class UpdateTask extends AsyncTask<Void, Boolean, Boolean>
    {
        private Context context;

        public UpdateTask(Context context)
        {
            this.context = context;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(Void... voids)
        {
            boolean ret = false;

            HttpResponse response = HttpUtils.get(App.URL);
            if(response == null || !response.isOK())
            {
                return ret;
            }

            String html = new String(response.data);
            Log.d(TAG, "html: " + html);
            try
            {
                //Connect to website
                Document doc = Jsoup.parse(html);

                if(doc == null)
                {
                    return ret;
                }

                Element div = doc.select("div[data-tab-id$=tab_Forex]").first();
                if(div == null)
                {
                    return ret;
                }

                Element input = div.select("input[data-instruments]").first();
                if(input == null)
                {
                    return ret;
                }

                String instruments = input.attr("data-instruments");
                String json = input.attr("data-json");

                if(TextUtils.isEmpty(instruments) || TextUtils.isEmpty(json))
                {
                    return ret;
                }

                JSONArray jsonList = new JSONArray(json);
                if(jsonList == null || jsonList.length() == 0)
                {
                    return ret;
                }

                for(int i = 0; i < jsonList.length(); i++)
                {
                    JSONObject jsonItem = jsonList.getJSONObject(i);
                    if(jsonItem == null)
                    {
                        continue;
                    }

                    int id = jsonItem.optInt("InstrumentId", -1);
                    if(id == 39)
                    {
                        double buy = jsonItem.optDouble("BuyPrice");
                        double sell = jsonItem.optDouble("SellPrice");
                        double change = jsonItem.optDouble("ChangePercent");

                        Prefs.setBuy(context, (float)buy);
                        Prefs.setSell(context, (float)sell);
                        Prefs.setChange(context, (float)change);

                        ret = true;
                        break;
                    }
                }
            }
            catch (Exception e)
            {

            }


            return ret;
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            super.onPostExecute(result);

            if(result != null && result.booleanValue())
            {
                float sell = Prefs.getSell(context);

                if(Prefs.getAbove(context))
                {
                    float point = Prefs.getAboveAmount(context);
                    if(point > 0 && sell > point)
                    {
                        reset(sell);

                        sendNotification("Price Up", String.format("AUD/USD price went up to %.5f", sell), R.color.green);
                    }
                }

                if(Prefs.getBelow(context))
                {
                    float point = Prefs.getBelowAmount(context);
                    if(point > 0 && sell < point)
                    {
                        reset(sell);

                        sendNotification("Price Down", String.format("AUD/USD price went down to %.5f", sell), R.color.red);
                    }
                }

                EventBus.getDefault().post(App.EVENT_UPDATE);
            }

            task = null;
        }

        private void reset(float value)
        {
            float percent = 0.005f;
            float change = ((percent / 100.0f) * value);

            Prefs.setAbove(context, false);
            Prefs.setBelow(context, false);

            Prefs.setAbovePercent(context, percent);
            Prefs.setBelowPercent(context, percent);

            Prefs.setAboveAmount(context, value + change);
            Prefs.setBelowAmount(context, value - change);
        }
    }

    private void sendNotification(String title, String body, int color)
    {

        try
        {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder builder = App.buildNotification(this, title, body);

            builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            builder.setAutoCancel(true);
            builder.setColor(getResources().getColor(color));

            notificationManager.notify(App.CHANGE_NOTIFY_ID, builder.build());
        }
        catch(SecurityException se)
        {
            se.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}
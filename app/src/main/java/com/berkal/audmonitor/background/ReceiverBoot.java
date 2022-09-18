package com.berkal.audmonitor.background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.berkal.audmonitor.App;

public class ReceiverBoot extends BroadcastReceiver
{
    private static final String TAG = ReceiverBoot.class.getSimpleName();

    @Override
    public void onReceive(final Context context, final Intent intentData)
    {
        App.serviceStart(context, App.ACTION_BOOT);
    }
}

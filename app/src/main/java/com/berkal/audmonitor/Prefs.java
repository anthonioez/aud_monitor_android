package com.berkal.audmonitor;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs
{
    public static final String TAG = Prefs.class.getSimpleName();
    private static final String SHARED_PREFS = "prefs";

    private static final String KEY_BUY     = "buy";
    private static final String KEY_SELL    = "sell";
    private static final String KEY_CHANGE  = "change";

    private static final String KEY_ABOVE           = "above";
    private static final String KEY_ABOVE_AMOUNT = "above_rate";
    private static final String KEY_ABOVE_PERCENT = "above_change";

    private static final String KEY_BELOW           = "below";
    private static final String KEY_BELOW_AMOUNT = "below_rate";
    private static final String KEY_BELOW_PERCENT = "below_change";


    public static void setBoolean(Context context, String mKey, boolean mValue)
    {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(mKey, mValue);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String mKey, boolean mDefValue)
    {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return mSharedPreferences.getBoolean(mKey, mDefValue);
    }

    public static void setInt(Context context, String mKey, int mValue)
    {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(mKey, mValue);
        editor.commit();
    }

    public static int getInt(Context context, String mKey, int mDefValue)
    {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return mSharedPreferences.getInt(mKey, mDefValue);
    }

    public static void setLong(Context context, String mKey, long mValue)
    {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(mKey, mValue);
        editor.commit();
    }

    public static long getLong(Context context, String mKey, long mDefValue)
    {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return mSharedPreferences.getLong(mKey, mDefValue);
    }

    public static void setFloat(Context context, String mKey, float mValue)
    {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putFloat(mKey, mValue);
        editor.commit();
    }

    public static float getFloat(Context context, String mKey, float mDefValue)
    {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return mSharedPreferences.getFloat(mKey, mDefValue);
    }

    public static void setString(Context context, String mKey, String mValue)
    {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(mKey, mValue);
        editor.commit();
    }

    public static String getString(Context context, String mKey, String mDefValue)
    {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(mKey, mDefValue);
    }

    public static float getBuy(Context context)
    {
        return getFloat(context, KEY_BUY, 0);
    }
    public static void setBuy(Context context, float value)
    {
        setFloat(context, KEY_BUY, value);
    }

    public static float getSell(Context context)
    {
        return getFloat(context, KEY_SELL, 0);
    }
    public static void setSell(Context context, float value)
    {
        setFloat(context, KEY_SELL, value);
    }

    public static float getChange(Context context)
    {
        return getFloat(context, KEY_CHANGE, 0);
    }
    public static void setChange(Context context, float value)
    {
        setFloat(context, KEY_CHANGE, value);
    }

    public static boolean getAbove(Context context)
    {
        return getBoolean(context, KEY_ABOVE, false);
    }
    public static void setAbove(Context context, boolean value)
    {
        setBoolean(context, KEY_ABOVE, value);
    }


    public static float getAboveAmount(Context context)
    {
        return getFloat(context, KEY_ABOVE_AMOUNT, 0);
    }
    public static void setAboveAmount(Context context, float value)
    {
        setFloat(context, KEY_ABOVE_AMOUNT, value);
    }


    public static float getAbovePercent(Context context)
    {
        return getFloat(context, KEY_ABOVE_PERCENT, 0);
    }
    public static void setAbovePercent(Context context, float value)
    {
        setFloat(context, KEY_ABOVE_PERCENT, value);
    }

    //

    public static boolean getBelow(Context context)
    {
        return getBoolean(context, KEY_BELOW, false);
    }
    public static void setBelow(Context context, boolean value)
    {
        setBoolean(context, KEY_BELOW, value);
    }


    public static float getBelowAmount(Context context)
    {
        return getFloat(context, KEY_BELOW_AMOUNT, 0);
    }
    public static void setBelowAmount(Context context, float value)
    {
        setFloat(context, KEY_BELOW_AMOUNT, value);
    }


    public static float getBelowPercent(Context context)
    {
        return getFloat(context, KEY_BELOW_PERCENT, 0);
    }
    public static void setBelowPercent(Context context, float value)
    {
        setFloat(context, KEY_BELOW_PERCENT, value);
    }

}

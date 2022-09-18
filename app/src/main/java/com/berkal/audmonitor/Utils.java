package com.berkal.audmonitor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.Date;

public class Utils
{
    public static boolean isOnline(Context mContext)
    {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }

    public static void alert(Context context, String title, String msg)
    {
        alert(context, title, msg, null);
    }

    public static void alert(Context context, String title, String msg, final Runnable runnable)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setMessage(msg);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.cancel();

                if(runnable != null) runnable.run();
            }
        });
        dialog.show();
    }

    public static void hideKeyboard(Context ctx, View vw)
    {
        if (vw == null)
        {
            return;
        }
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(vw.getWindowToken(), 0);
    }

    public static void toast(Context context, String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static String getDateTimeFormat(long date)
    {
        return (String) DateFormat.format("EEE, MMM d, yyyy, kk:mm:ss", new Date(date));
    }

}

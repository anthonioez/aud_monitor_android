package com.berkal.audmonitor.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.berkal.audmonitor.App;
import com.berkal.audmonitor.Prefs;
import com.berkal.audmonitor.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ActivityMain extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener
{
    private TextView    textPrice;
    private View        layoutAbove;
    private View        layoutBelow;

    private TextView    textPriceAbove;
    private TextView    textPriceBelow;

    private TextView    textPercentAbove;
    private TextView    textPercentBelow;

    private Switch      switchTypeAbove;
    private Switch      switchTypeBelow;

    private SeekBar     seekPriceAbove;
    private SeekBar     seekPriceBelow;

    private ImageButton buttonIncBelow;
    private ImageButton buttonIncAbove;

    private ImageButton buttonDecBelow;
    private ImageButton buttonDecAbove;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        textPrice = findViewById(R.id.textPrice);

        layoutAbove = findViewById(R.id.layoutAbove);
        layoutBelow = findViewById(R.id.layoutBelow);

        TextView textType;

        textType            = layoutAbove.findViewById(R.id.textType);
        textType.setText("Price Above");

        textType            = layoutBelow.findViewById(R.id.textType);
        textType.setText("Price Below");

        textPriceAbove      = layoutAbove.findViewById(R.id.textPrice);
        textPercentAbove    = layoutAbove.findViewById(R.id.textPercent);
        switchTypeAbove     = layoutAbove.findViewById(R.id.switchType);
        seekPriceAbove      = layoutAbove.findViewById(R.id.seekPrice);
        buttonIncAbove      = layoutAbove.findViewById(R.id.buttonInc);
        buttonDecAbove      = layoutAbove.findViewById(R.id.buttonDec);

        textPriceBelow      = layoutBelow.findViewById(R.id.textPrice);
        textPercentBelow    = layoutBelow.findViewById(R.id.textPercent);
        switchTypeBelow     = layoutBelow.findViewById(R.id.switchType);
        seekPriceBelow      = layoutBelow.findViewById(R.id.seekPrice);
        buttonIncBelow      = layoutBelow.findViewById(R.id.buttonInc);
        buttonDecBelow      = layoutBelow.findViewById(R.id.buttonDec);

        updateBelowText();
        updateAboveText();
        updateSwitch();
        updateStates();

        updateAboveSeek(Prefs.getAbovePercent(this));
        updateBelowSeek(Prefs.getBelowPercent(this));

        switchTypeBelow.setOnClickListener(this);
        switchTypeAbove.setOnClickListener(this);

        buttonIncBelow.setOnClickListener(this);
        buttonDecBelow.setOnClickListener(this);

        buttonIncAbove.setOnClickListener(this);
        buttonDecAbove.setOnClickListener(this);

        seekPriceBelow.setOnSeekBarChangeListener(this);
        seekPriceAbove.setOnSeekBarChangeListener(this);

        App.serviceStart(this, App.ACTION_START);

    }

    @Override
    public void onResume()
    {
        super.onResume();

        update();
    }

    @Override
    public void onStart()
    {
        super.onStart();

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onStop()
    {
        super.onStop();

        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String event)
    {
        if (event == null)
            return;

        if (event.equals(App.EVENT_UPDATE))
        {
            update();
        }
    }

    @Override
    public void onClick(View view)
    {
        if(view == switchTypeAbove)
        {
            Prefs.setAbove(this, switchTypeAbove.isChecked());

            updateStates();
        }
        else if(view == switchTypeBelow)
        {
            Prefs.setBelow(this, switchTypeBelow.isChecked());

            updateStates();
        }

        else if(view == buttonIncAbove)
        {
            float percent = Prefs.getAbovePercent(this) + App.INCREMENT;
            if(percent > App.PERCENT_MAX) percent = App.PERCENT_MAX;

            updateAboveSeek(percent);

            updateAbove(percent);
            updateAboveText();
        }
        else if(view == buttonDecAbove)
        {
            float percent = Prefs.getAbovePercent(this) - App.INCREMENT;
            if(percent < 0) percent = 0;

            updateAboveSeek(percent);

            updateAbove(percent);
            updateAboveText();
        }

        else if(view == buttonIncBelow)
        {
            float percent = Prefs.getBelowPercent(this) + App.INCREMENT;
            if(percent > App.PERCENT_MAX) percent = App.PERCENT_MAX;

            updateBelowSeek(percent);

            updateBelow(percent);
            updateBelowText();
        }
        else if(view == buttonDecBelow)
        {
            float percent = Prefs.getBelowPercent(this) - App.INCREMENT;
            if(percent < 0) percent = 0;

            updateBelowSeek(percent);

            updateBelow(percent);
            updateBelowText();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int prog, boolean fromUser)
    {
        float percent = (float)prog * (App.PERCENT_MAX / (float)App.PROGRESS_MAX);

        if(seekBar == seekPriceAbove)
        {
            if(fromUser)
            {
                updateAbove(percent);
            }

            updateAboveText();
        }
        else if(seekBar == seekPriceBelow)
        {
            if(fromUser)
            {
                updateBelow(percent);
            }

            updateBelowText();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar)
    {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar)
    {

    }

    private void update()
    {
        textPrice.setText(String.format("%.5f", Prefs.getSell(this)));

        updateAboveText();
        updateBelowText();
        updateSwitch();
        updateStates();
    }

    private void updateAboveText()
    {
        float amount = Prefs.getAboveAmount(this);
        textPriceAbove.setText(amount == 0 ? "" : String.format("%.5f", amount));
        textPercentAbove.setText(String.format("%.3f", Prefs.getAbovePercent(this)) + "%");
    }

    private void updateBelowText()
    {
        float amount = Prefs.getBelowAmount(this);
        textPriceBelow.setText(amount == 0 ? "" : String.format("%.5f", amount));
        textPercentBelow.setText(String.format("%.3f", Prefs.getBelowPercent(this)) + "%");
    }

    private void updateSwitch()
    {
        switchTypeAbove.setChecked(Prefs.getAbove(this));
        switchTypeBelow.setChecked(Prefs.getBelow(this));
    }

    private void updateAboveSeek(float percent)
    {
        float prog = percent * ((float)App.PROGRESS_MAX / App.PERCENT_MAX);

        seekPriceAbove.setProgress((int)(prog));
    }

    private void updateBelowSeek(float percent)
    {
        float prog = percent * ((float)App.PROGRESS_MAX / App.PERCENT_MAX);

        seekPriceBelow.setProgress((int)(prog));
    }

    private void updateStates()
    {
        /*
        boolean above = Prefs.getAbove(this);
        boolean below = Prefs.getBelow(this);

        seekPriceAbove.setEnabled(above);
        buttonIncAbove.setEnabled(above);
        buttonDecAbove.setEnabled(above);

        seekPriceBelow.setEnabled(below);
        buttonIncBelow.setEnabled(below);
        buttonDecBelow.setEnabled(below);
        */
    }

    private void updateAbove(float percent)
    {
        float current = Prefs.getSell(this);

        float point = current + ((percent / 100.0f) * current);

        Prefs.setAboveAmount(this, point);
        Prefs.setAbovePercent(this, percent);
    }

    private void updateBelow(float percent)
    {
        float current = Prefs.getSell(this);

        float point = current - ((percent / 100.0f) * current);

        Prefs.setBelowAmount(this, point);
        Prefs.setBelowPercent(this, percent);
    }


}
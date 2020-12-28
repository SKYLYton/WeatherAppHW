package com.weather.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;

public class PowerConnectionReceiver extends BroadcastReceiver {

    public enum Type {
        empty, charging, normal, low, critical
    }

    private final int LOW_LEVEL_BATTERY_FOR_SENDING = 40;
    private final int CRITICAL_LOW_LEVEL_BATTERY_FOR_SENDING = 10;

    private Type currentType = Type.empty;

    private OnPowerStateListener onPowerStateListener;

    public interface OnPowerStateListener {
        void onCharging();

        void onNormalLevel();

        void onLowLevel();

        void onCriticalLowLevel();
    }

    public void setOnPowerStateListener(OnPowerStateListener onPowerStateListener) {
        this.onPowerStateListener = onPowerStateListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (onPowerStateListener == null) {
            return;
        }

        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

        if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
            if(currentType != Type.charging) {
                currentType = Type.charging;
                onPowerStateListener.onCharging();
            }
        } else if(level > LOW_LEVEL_BATTERY_FOR_SENDING) {
            if(currentType != Type.normal) {
                currentType = Type.normal;
                onPowerStateListener.onNormalLevel();
            }
        } else if (level > CRITICAL_LOW_LEVEL_BATTERY_FOR_SENDING) {
            if(currentType != Type.low) {
                currentType = Type.low;
                onPowerStateListener.onLowLevel();
            }
        } else {
            if(currentType != Type.critical) {
                currentType = Type.critical;
                onPowerStateListener.onCriticalLowLevel();
            }
        }

    }
}

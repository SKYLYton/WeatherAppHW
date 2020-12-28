package com.weather.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.weather.Constants;

public class NetworkReceiver extends BroadcastReceiver {
    private OnNetworkStateListener onNetworkStateListener;

    public interface OnNetworkStateListener{
        void onState(boolean isConnected);
    }

    public void setOnNetworkStateListener(OnNetworkStateListener onNetworkStateListener) {
        this.onNetworkStateListener = onNetworkStateListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(onNetworkStateListener == null){
            return;
        }

        boolean online = isOnline(context);

        String actionOfIntent = intent.getAction();
        if(actionOfIntent.equals(Constants.CONNECTIVITY_ACTION)){
            onNetworkStateListener.onState(online);
        }

    }

    public boolean isOnline(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}

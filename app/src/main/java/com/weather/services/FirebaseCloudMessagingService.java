package com.weather.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.weather.Constants;
import com.weather.R;
import com.weather.SplashActivity;

public class FirebaseCloudMessagingService extends FirebaseMessagingService {

    private int notifyId = 0;
    private final String NAME = "NOTIFICATION_WeatherApp";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        showMessage(getApplicationContext(), remoteMessage);

    }

    public void showMessage(Context context, RemoteMessage remoteMessage) {
        String title = remoteMessage.getNotification().getTitle();
        if (title == null){
            title = "Push Message";
        }
        String msg = remoteMessage.getNotification().getBody();

        final int NOTIFY_ID = notifyId++;

        Notification.Builder builder = new Notification.Builder(context);
        NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        builder.setSmallIcon(R.drawable.ic_notification)
                .setTicker(msg)
                .setAutoCancel(true)
                .setShowWhen(true)
                .setContentTitle(title)
                .setColor(getResources().getColor(R.color.blue))
                .setStyle(new Notification.BigTextStyle().bigText(msg))
                .setContentText(msg);


        Notification n = builder.build();
        n.defaults = Notification.DEFAULT_SOUND |
                Notification.DEFAULT_VIBRATE;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(String.valueOf(NOTIFY_ID), NAME, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(R.color.blue);
            notificationChannel.enableVibration(true);
            builder.setChannelId(String.valueOf(NOTIFY_ID));
            nm.createNotificationChannel(notificationChannel);
        }

        nm.notify(NOTIFY_ID, n);
    }

    @Override
    public void onNewToken(String token) {
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        if(Constants.DEBUG) {
            Log.d("PushMessage", "Token " + token);
        }
    }

}

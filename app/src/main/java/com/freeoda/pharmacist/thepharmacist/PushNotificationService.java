package com.freeoda.pharmacist.thepharmacist;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by Lakna on 5/12/2016.
 */
public class PushNotificationService extends GcmListenerService {
    public static final int notifyID = 9001;
    NotificationCompat.Builder builder;
    final Context context=this;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        //super.onMessageReceived(from, data);
        String message = data.getString(ApplicationConstants.MSG_KEY);
        sendNotification(message);

    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onSendError(String msgId, String error) {
        super.onSendError(msgId, error);
        System.out.print("Error on gcm: " + error);
    }

    private void sendNotification(String msg) {


        Log.i("TAG", msg);
        Intent resultIntent = new Intent(this, ViewConfirmedOrdersActivity.class);
        resultIntent.putExtra("msg", msg);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        NotificationManager mNotificationManager;

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Log.i("TAg NOTIFY", "inside notify");
        //mNotifyBuilder = new NotificationCompat.Builder(this).setContentTitle("Alert").setContentText("You've received new message.").setSmallIcon(R.drawable.logo_small);
        mBuilder.setSmallIcon(R.drawable.logo_small);
        mBuilder.setContentTitle("The Pharmacist ALERT!!");
        mBuilder.setContentText("Your order has been confirmed!!" +
                "");
        // Set pending intent
        Log.i("TAg", "before intent");
        mBuilder.setContentIntent(resultPendingIntent);
        Log.i("TAg", "after intent");

        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;
        mBuilder.setDefaults(defaults);

        // Set autocancel
        mBuilder.setAutoCancel(true);
        // Post a notification
        mNotificationManager.notify(notifyID, mBuilder.build());
    }
}

package br.com.eduardomaxwell.beberagua;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationPublisher extends BroadcastReceiver {
    public static final String KEY_NOTIFICATION = "key_notification";
    public static final String KEY_NOTIFICATION_ID = "key_notification_id";

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent ii = new Intent(context.getApplicationContext(), MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, ii, 0);

        String message = intent.getStringExtra(KEY_NOTIFICATION);
        int id = intent.getIntExtra(KEY_NOTIFICATION_ID, 0);

        Notification notification = getNotification(message, context, notificationManager, pIntent);

        notificationManager.notify(id, notification);
    }

    private Notification getNotification(String content, Context context, NotificationManager manager, PendingIntent intent) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context.getApplicationContext())
                        .setContentText(content)
                        .setContentIntent(intent)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setTicker("Alerta")
                        .setAutoCancel(true)
                        .addAction(R.drawable.ic_drink, "OK", null)
                        .setSmallIcon(R.drawable.ic_drink);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "CHANNEL_ID";
            NotificationChannel channel = new NotificationChannel(channelId, "Channel", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }

        return builder.build();
    }
}

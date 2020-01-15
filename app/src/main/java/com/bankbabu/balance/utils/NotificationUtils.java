package com.bankbabu.balance.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.text.TextUtils;
import com.bankbabu.balance.R;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class NotificationUtils {

  public static void showNotificationMessage(String title, String message, String timeStamp,
      int icon, Intent intent, final Context context) {
    showNotificationMessage(title, message, timeStamp, intent, icon, context);
  }

  @SuppressWarnings("deprecation")
  private static void showNotificationMessage(final String title, final String message,
      final String timeStamp, Intent intent, final int imageUrl,
      final Context context) {
    if (TextUtils.isEmpty(message)) {
      return;
    }

    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    final PendingIntent resultPendingIntent =
        PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

    final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
        context);

    final Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    showSmallNotification(mBuilder, title, message, timeStamp, resultPendingIntent,
        alarmSound, imageUrl, context);
  }

  private static void showSmallNotification(Builder builder, String title, String message,
      String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound, int imageUrl,
      final Context context) {

    final NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

    final int icon = R.mipmap.ic_launcher_round;
    inboxStyle.addLine(message);

    Notification notification = builder.setSmallIcon(icon).setTicker(title).setWhen(0)
        .setAutoCancel(true)
        .setContentTitle(title)
        .setContentIntent(resultPendingIntent)
        .setSound(alarmSound)
        .setStyle(inboxStyle)
        .setWhen(getTimeMilliSec(timeStamp))
        .setSmallIcon(R.drawable.ic_category_locked)
        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), imageUrl))
        .setContentText(message)
        .build();

    final NotificationManager notificationManager = (NotificationManager) context
        .getSystemService(Context.NOTIFICATION_SERVICE);

    int id = new Random().nextInt(9999 - 1000) + 1000;
    if (notificationManager != null) {
      notificationManager.notify(id, notification);
    }
  }

  private static long getTimeMilliSec(String timeStamp) {
    final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    try {
      final Date date = format.parse(timeStamp);
      return date.getTime();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return 0;
  }
}

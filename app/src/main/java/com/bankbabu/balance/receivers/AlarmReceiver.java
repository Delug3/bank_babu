package com.bankbabu.balance.receivers;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.bankbabu.balance.R;
import com.bankbabu.balance.activities.HomeActivity;
import com.bankbabu.balance.application.Constants;
import com.bankbabu.balance.database.DatabaseHelper;
import com.bankbabu.balance.models.Bill;
import com.bankbabu.balance.utils.NotificationUtils;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AlarmReceiver extends BroadcastReceiver {

  @SuppressLint("UnsafeProtectedBroadcastReceiver")
  @Override
  public void onReceive(Context context, Intent intent) {
    final DatabaseHelper databaseHelper = new DatabaseHelper(context);

    long id = intent.getIntExtra(Constants.EXTRA_BILL_ID_ALARM, -1);
    final List<Bill> bills = databaseHelper.getBillsById(id);

    if (bills.isEmpty()) {
      return;
    }

    try {
      final Date currentDate = Calendar.getInstance().getTime();
      final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
      final Date dueDate = dateFormat.parse(bills.get(0).getDueDate());

      int date = currentDate.compareTo(dueDate);

      if (date <= 0) {
        final Intent alarmIntent = new Intent(context, HomeActivity.class);
        alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        final String title = context
            .getString(R.string.format_bill_to, bills.get(0).getPayee(),
                bills.get(0).getBillType());

        final String message = context
            .getString(R.string.format_due_on, bills.get(0).getAmount(), bills.get(0).getDueDate());
        final String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            .format(new Date());
        final int categoryIcon = bills.get(0).getCategoryIcon();

        NotificationUtils.showNotificationMessage(title, message, timeStamp, categoryIcon,
            alarmIntent, context);
      } else {
        String stringDate = null;
        if (bills.get(0).getCustomDueDate() == 1) {
          stringDate = bills.get(0).getNextDueDate();
          databaseHelper.updateBillDueDate(bills.get(0).getId(), bills.get(0).getNextDueDate());
        } else {
          if (bills.get(0).getRepeat() == 1) {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(dueDate);

            switch (bills.get(0).getRepeatByType()) {
              case DAY:
                calendar.add(Calendar.DATE, Integer.parseInt(bills.get(0).getRepeatEvery()));
                break;
              case WEEK:
                calendar
                    .add(Calendar.DATE, Integer.parseInt(bills.get(0).getRepeatEvery()) * 7);
                break;
              case MONTH:
                calendar.add(Calendar.MONTH, Integer.parseInt(bills.get(0).getRepeatEvery()));
                break;
              case YEAR:
                calendar.add(Calendar.YEAR, Integer.parseInt(bills.get(0).getRepeatEvery()));
                break;
            }

            stringDate = dateFormat.format(calendar.getTime());
            databaseHelper.updateBillDueDate(bills.get(0).getId(), stringDate);
          }
        }

        final int alarmId = intent.getIntExtra(Constants.EXTRA_ALARM_ID, -1);

        final Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmId,
            alarmIntent, 0);
        final AlarmManager alarmManager = (AlarmManager) context
            .getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
          alarmManager.cancel(pendingIntent);
        }

        triggerAlarmManager(pendingIntent, stringDate, bills.get(0).getNotification(), context);
      }
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  /**
   * Trigger alarm manager with entered time interval
   */
  public void triggerAlarmManager(PendingIntent pendingIntent, String dueDate, String notification,
      Context context) {
    final String dateArr[] = dueDate.split("-");
    final String day = dateArr[0];
    final String month = dateArr[1];
    final String year = dateArr[2];

    // get a Calendar object with current time
    final Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.MONTH, Integer.parseInt(month) - 1);
    calendar.set(Calendar.YEAR, Integer.parseInt(year));
    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
    calendar.set(Calendar.HOUR, 7);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    long eventTime = calendar.getTimeInMillis();

    long oneDay = AlarmManager.INTERVAL_DAY;
    // Converts 24 Hrs(1 Day) to milliseconds
    int noOfDays = Integer.parseInt(notification);
    long reminderTime = eventTime - (noOfDays * oneDay);

    final AlarmManager alarmManager = (AlarmManager) context
        .getSystemService(Context.ALARM_SERVICE);
    if (alarmManager != null) {
      alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, reminderTime, AlarmManager.INTERVAL_DAY,
          pendingIntent);
    }

    final Calendar calendarReminder = Calendar.getInstance();
    calendarReminder.setTimeInMillis(reminderTime);

    int yearReminder = calendarReminder.get(Calendar.YEAR);
    int monthReminder = calendarReminder.get(Calendar.MONTH);
    int dayReminder = calendarReminder.get(Calendar.DAY_OF_MONTH);

    Toast.makeText(context,
        context.getString(R.string.format_alarm_set_for, monthReminder, dayReminder, yearReminder),
        Toast.LENGTH_SHORT).show();
  }


}

package com.grayditch.netarea.presentation.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by gerard on 16/04/16.
 */
public class BootReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent schedulingIntent = new Intent(null, ScheduledQualificationsReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, schedulingIntent, 0);

//            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//            alarmManager.setAlarmClock();
//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, (uploadInterval * 1000), (uploadInterval * 1000), pendingIntent);
        }
    }
}

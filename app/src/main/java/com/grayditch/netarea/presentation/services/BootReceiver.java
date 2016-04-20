package com.grayditch.netarea.presentation.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by gerard on 16/04/16.
 */
public class BootReceiver extends BroadcastReceiver {
    private static Intent schedulingIntent = null;
    private static PendingIntent pendingIntent = null;
    private static AlarmManager alarmManager = null;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
//            schedulingIntent = new Intent(null, ScheduledQualificationsReceiver.class);
//            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, (uploadInterval * 1000), (uploadInterval * 1000), pendingIntent);
        }
    }
}

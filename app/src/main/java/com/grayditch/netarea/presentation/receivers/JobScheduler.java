package com.grayditch.netarea.presentation.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by gerard on 17/12/16.
 */

public class JobScheduler {

    private static final String TAG = JobScheduler.class.getName();

    private Context context;
    private SharedPreferences sharedPreferences;

    public JobScheduler(Context context, SharedPreferences sharedPreferences) {
        this.context = context;
        this.sharedPreferences = sharedPreferences;
    }

    public void schedule() {
        Log.v(TAG, "JobScheduler -> schedule");
        int updateInterval = getUpdateInterval();
        if (updateInterval > 0) {
            PendingIntent pendingIntent = buildIntent();
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, (updateInterval * 1000), (updateInterval * 1000), pendingIntent);
        }
    }


    private PendingIntent buildIntent() {
        Intent schedulingIntent = new Intent(context, ScheduledQualificationsReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, schedulingIntent, 0);
        return pendingIntent;
    }

    private int getUpdateInterval() {
        String value = this.sharedPreferences.getString("backgroundQualificationsPref", "0");
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

}

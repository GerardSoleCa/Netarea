package com.grayditch.netarea.presentation.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by gerard on 17/12/16.
 */

public class JobScheduler {

    private static final String TAG = JobScheduler.class.getName();
    private static final int INTENT_ID = 8928;

    private Context context;
    private SharedPreferences sharedPreferences;
    private AlarmManager alarmManager;

    public JobScheduler(Context context, SharedPreferences sharedPreferences, AlarmManager alarmManager) {
        this.context = context;
        this.sharedPreferences = sharedPreferences;
        this.alarmManager = alarmManager;
    }

    public void scheduleIfNotRunning() {
        if (buildIntentWithFlags(PendingIntent.FLAG_NO_CREATE) == null) {
            this.schedule();
        }
    }

    public void schedule() {
        Log.v(TAG, "JobScheduler -> schedule");
        this.cancelPreviousAlarms();
        int updateInterval = getUpdateInterval();
        if (updateInterval > 0) {
            PendingIntent pendingIntent = buildIntent();
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, (updateInterval * 1000), (updateInterval * 1000), pendingIntent);
        }
    }

    private void cancelPreviousAlarms() {
        PendingIntent pendingIntent = buildIntent();
        this.alarmManager.cancel(pendingIntent);
    }

    private PendingIntent buildIntent() {
        return buildIntentWithFlags(0);
    }

    private PendingIntent buildIntentWithFlags(int flags) {
        Intent schedulingIntent = new Intent(context, ScheduledQualificationsReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, INTENT_ID, schedulingIntent, flags);
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

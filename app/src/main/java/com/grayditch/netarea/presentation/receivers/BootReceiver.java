package com.grayditch.netarea.presentation.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.grayditch.netarea.presentation.App;

import javax.inject.Inject;

/**
 * Created by gerard on 16/04/16.
 */
public class BootReceiver extends BroadcastReceiver {

    @Inject
    JobScheduler jobScheduler;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            App.component(context).inject(this);
            jobScheduler.schedule();
        }
    }
}

package com.grayditch.netarea.presentation.internal.di.modules;

import android.app.AlarmManager;
import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.grayditch.netarea.R;
import com.grayditch.netarea.data.executor.JobExecutor;
import com.grayditch.netarea.domain.UserDetails;
import com.grayditch.netarea.domain.executor.PostExecutionThread;
import com.grayditch.netarea.domain.executor.ThreadExecutor;
import com.grayditch.netarea.presentation.UIThread;
import com.grayditch.netarea.presentation.receivers.JobScheduler;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by gerard on 19/04/16.
 */
@Module
public class DependenciesModule {

    @Provides
    @Singleton
    JobScheduler provideJobScheduler(Context context, SharedPreferences sharedPreferences, AlarmManager alarmManager){
        return new JobScheduler(context, sharedPreferences, alarmManager);
    }

    @Provides
    @Singleton
    ThreadExecutor provideThreadExecutor() {
        return new JobExecutor();
    }

    @Provides
    @Singleton
    PostExecutionThread providePostExecutionThread() {
        return new UIThread();
    }

    @Provides
    @Singleton
    NotificationCompat.Builder provideNotificationCompatBuilder(Context context){
        return new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
                .setContentTitle(context.getString(R.string.notifications_title))
                .setSubText(context.getString(R.string.notifications_subtitle))
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);
    }
}

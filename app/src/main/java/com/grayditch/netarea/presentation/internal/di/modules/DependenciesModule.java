package com.grayditch.netarea.presentation.internal.di.modules;

import android.content.Context;
import android.content.SharedPreferences;

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
    JobScheduler provideJobScheduler(Context context, SharedPreferences sharedPreferences){
        return new JobScheduler(context, sharedPreferences);
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
}

package com.grayditch.netarea.presentation.internal.di.modules;

import android.content.Context;

import com.grayditch.netarea.data.database.QualificationsDB;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by gerard on 20/04/16.
 */
@Module(includes = {AppModule.class})
public class DBModule {

    @Provides
    @Singleton
    QualificationsDB providesQualificationsDB(Context context) {
        return new QualificationsDB(context);
    }
}

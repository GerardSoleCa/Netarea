package com.grayditch.netarea.presentation.internal.di.modules;

import android.content.SharedPreferences;

import com.grayditch.netarea.data.database.QualificationsDB;
import com.grayditch.netarea.data.network.NetareaClient;
import com.grayditch.netarea.data.repository.QualificationsDataRepository;
import com.grayditch.netarea.data.repository.UserDataRepository;
import com.grayditch.netarea.domain.repository.QualificationsRepository;
import com.grayditch.netarea.domain.repository.UserRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by gerard on 19/04/16.
 */
@Module(includes = {NetworkModule.class, DBModule.class})
public class RepositoriesModule {

    @Provides
    @Singleton
    UserRepository provideUserDataRepository(SharedPreferences sharedPreferences) {
        return new UserDataRepository(sharedPreferences);
    }

    @Provides
    @Singleton
    QualificationsRepository provideQualificationsRepository(NetareaClient netareaClient, QualificationsDB qualificationsDB) {
        return new QualificationsDataRepository(netareaClient, qualificationsDB);
    }
}

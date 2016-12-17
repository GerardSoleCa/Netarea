package com.grayditch.netarea.presentation;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.grayditch.netarea.BuildConfig;
import com.grayditch.netarea.presentation.internal.di.components.AppComponent;
import com.grayditch.netarea.presentation.internal.di.components.DaggerAppComponent;
import com.grayditch.netarea.presentation.internal.di.modules.AppModule;

import io.fabric.sdk.android.Fabric;

/**
 * Created by gerard on 14/04/16.
 */
public class App extends Application {

    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
//        if (BuildConfig.USE_CRASHLYTICS) {
            Fabric.with(this, new Crashlytics());
//        }
        this.initializeInjector();
    }

    private void initializeInjector() {
        this.component = DaggerAppComponent.builder()
                .appModule(new AppModule(this)).build();
    }

    public static AppComponent component(Context app) {
        return ((App) app.getApplicationContext()).component;
    }
}

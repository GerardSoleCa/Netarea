package com.grayditch.netarea.presentation.internal.di.modules;

import com.grayditch.netarea.domain.UserDetails;
import com.grayditch.netarea.domain.interactor.GetQualificationsUseCase;
import com.grayditch.netarea.domain.interactor.LoginUseCase;
import com.grayditch.netarea.presentation.activities.login.LoginPresenter;
import com.grayditch.netarea.presentation.activities.login.LoginPresenterImpl;
import com.grayditch.netarea.presentation.activities.qualifications.QualificationsPresenter;
import com.grayditch.netarea.presentation.activities.qualifications.QualificationsPresenterImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by gerard on 12/04/16.
 */
@Module
public class PresentersModule {

    @Provides
    @Singleton
    LoginPresenter providesLoginPresenter(LoginUseCase loginUseCase) {
        return new LoginPresenterImpl(loginUseCase);
    }


    @Provides
    @Singleton
    QualificationsPresenter providesQualificationsPresenter(GetQualificationsUseCase getQualificationsUseCase) {
        return new QualificationsPresenterImpl(getQualificationsUseCase);
    }
}

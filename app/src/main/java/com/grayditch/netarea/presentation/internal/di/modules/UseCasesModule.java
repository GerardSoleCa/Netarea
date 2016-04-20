package com.grayditch.netarea.presentation.internal.di.modules;

import com.grayditch.netarea.domain.executor.PostExecutionThread;
import com.grayditch.netarea.domain.executor.ThreadExecutor;
import com.grayditch.netarea.domain.interactor.CheckAuthenticatedUseCase;
import com.grayditch.netarea.domain.interactor.CheckAuthenticatedUseCaseImpl;
import com.grayditch.netarea.domain.interactor.GetQualificationsUseCase;
import com.grayditch.netarea.domain.interactor.GetQualificationsUseCaseImpl;
import com.grayditch.netarea.domain.interactor.LoginUseCase;
import com.grayditch.netarea.domain.interactor.LoginUseCaseImpl;
import com.grayditch.netarea.domain.repository.QualificationsRepository;
import com.grayditch.netarea.domain.repository.UserRepository;

import dagger.Module;
import dagger.Provides;

/**
 * Created by gerard on 19/04/16.
 */
@Module(includes = {DependenciesModule.class})
public class UseCasesModule {
    @Provides
    LoginUseCase provideLoginUseCase(UserRepository userRepository, QualificationsRepository qualificationsRepository,
                                     ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new LoginUseCaseImpl(userRepository, qualificationsRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    GetQualificationsUseCase provideGetQualificationsUseCase(UserRepository userRepository,
                                                             QualificationsRepository qualificationsRepository,
                                                             ThreadExecutor threadExecutor,
                                                             PostExecutionThread postExecutionThread) {
        return new GetQualificationsUseCaseImpl(userRepository, qualificationsRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    CheckAuthenticatedUseCase provideCheckAuthenticatedUseCase(UserRepository userRepository, ThreadExecutor threadExecutor,
                                                               PostExecutionThread postExecutionThread) {
        return new CheckAuthenticatedUseCaseImpl(userRepository, threadExecutor, postExecutionThread);
    }
}

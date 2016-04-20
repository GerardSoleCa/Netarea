package com.grayditch.netarea.domain.interactor;

import com.grayditch.netarea.domain.Subject;
import com.grayditch.netarea.domain.UserDetails;
import com.grayditch.netarea.domain.executor.PostExecutionThread;
import com.grayditch.netarea.domain.executor.ThreadExecutor;
import com.grayditch.netarea.domain.interactor.interfaces.LoginUseCase;
import com.grayditch.netarea.domain.repository.QualificationsRepository;
import com.grayditch.netarea.domain.repository.UserRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by gerard on 18/04/16.
 */
public class LoginUseCaseImpl implements LoginUseCase {

    private Callback callback;

    private final UserRepository userRepository;
    private final QualificationsRepository qualificationsRepository;
    private final ThreadExecutor threadExecutor;
    private final PostExecutionThread postExecutionThread;
    private UserDetails userDetails;

    @Inject
    public LoginUseCaseImpl(UserRepository userRepository, QualificationsRepository qualificationsRepository,
                            ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        if (userRepository == null || qualificationsRepository == null || threadExecutor == null || postExecutionThread == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null!!!");
        }
        this.userRepository = userRepository;
        this.qualificationsRepository = qualificationsRepository;
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
    }


    @Override
    public void execute(UserDetails userDetails, Callback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Interactor callback cannot be null!!!");
        }
        this.userDetails = userDetails;
        this.callback = callback;
        this.threadExecutor.execute(this);
    }

    @Override
    public void run() {
        this.qualificationsRepository.getQualifications(this.userDetails, this.qualificationsRepositoryCallback);
    }

    private final QualificationsRepository.Callback qualificationsRepositoryCallback = new QualificationsRepository.Callback() {
        @Override
        public void onSuccess(List<Subject> subjects) {
            notifyGetUserListSuccessfully(subjects);
        }

        @Override
        public void onError(Throwable e) {
            notifyError(e);
        }
    };

    private void notifyGetUserListSuccessfully(final List<Subject> subjects) {
        this.postExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                LoginUseCaseImpl.this.userRepository.storeUserDetails(userDetails, new UserRepository.Callback() {
                    @Override
                    public void onSuccess(UserDetails userDetails) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
                callback.onSuccess();
            }
        });
    }

    private void notifyError(final Throwable e) {
        this.postExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(e);
            }
        });
    }
}

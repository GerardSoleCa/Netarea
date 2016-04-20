package com.grayditch.netarea.domain.interactor;

import com.grayditch.netarea.domain.Subject;
import com.grayditch.netarea.domain.UserDetails;
import com.grayditch.netarea.domain.executor.PostExecutionThread;
import com.grayditch.netarea.domain.executor.ThreadExecutor;
import com.grayditch.netarea.domain.repository.QualificationsRepository;
import com.grayditch.netarea.domain.repository.UserRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by gerard on 18/04/16.
 */
public class CheckAuthenticatedUseCaseImpl implements CheckAuthenticatedUseCase {

    private final UserRepository userRepository;
    private final ThreadExecutor threadExecutor;
    private final PostExecutionThread postExecutionThread;

    private Callback callback;

    @Inject
    public CheckAuthenticatedUseCaseImpl(UserRepository userRepository, ThreadExecutor threadExecutor,
                                         PostExecutionThread postExecutionThread) {
        this.userRepository = userRepository;
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
    }

    @Override
    public void execute(Callback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Interactor callback cannot be null!!!");
        }
        this.callback = callback;
        this.threadExecutor.execute(this);
    }

    @Override
    public void run() {
        this.postExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                boolean isAuthenticated = CheckAuthenticatedUseCaseImpl.this.userRepository.isAuthenticated();
                CheckAuthenticatedUseCaseImpl.this.callback.onSuccess(isAuthenticated);
            }
        });
    }


}

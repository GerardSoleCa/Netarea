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
public class GetQualificationsUseCaseImpl implements GetQualificationsUseCase {

    private final UserRepository userRepository;
    private final ThreadExecutor threadExecutor;
    private final PostExecutionThread postExecutionThread;
    private final QualificationsRepository qualificationsRepository;

    private GetQualificationsUseCase.Callback callback;

    @Inject
    public GetQualificationsUseCaseImpl(UserRepository userRepository, QualificationsRepository qualificationsRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        this.userRepository = userRepository;
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
        this.qualificationsRepository = qualificationsRepository;
    }

    @Override
    public void execute(GetQualificationsUseCase.Callback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Interactor callback cannot be null!!!");
        }
        this.callback = callback;
        this.threadExecutor.execute(this);
    }

    @Override
    public void run() {
        this.userRepository.getUserDetails(this.userRepositoryCallback);
    }

    private UserRepository.Callback userRepositoryCallback = new UserRepository.Callback() {
        @Override
        public void onSuccess(UserDetails userDetails) {
            qualificationsRepository.getQualifications(userDetails, qualificationsRepositoryCallback);
        }

        @Override
        public void onError(Throwable e) {
            callback.onError(e);
        }
    };

    private QualificationsRepository.Callback qualificationsRepositoryCallback = new QualificationsRepository.Callback() {
        @Override
        public void onSuccess(final List<Subject> subjects) {
            postExecutionThread.post(new Runnable() {
                @Override
                public void run() {
                    callback.onSuccess(subjects);
                }
            });

        }

        @Override
        public void onError(final Throwable e) {
           postExecutionThread.post(new Runnable() {
                @Override
                public void run() {
                    callback.onError(e);
                }
            });
        }
    };
}

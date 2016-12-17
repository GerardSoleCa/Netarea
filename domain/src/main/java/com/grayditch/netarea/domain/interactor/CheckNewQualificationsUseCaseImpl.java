package com.grayditch.netarea.domain.interactor;

import com.grayditch.netarea.domain.Subject;
import com.grayditch.netarea.domain.UserDetails;
import com.grayditch.netarea.domain.executor.PostExecutionThread;
import com.grayditch.netarea.domain.executor.ThreadExecutor;
import com.grayditch.netarea.domain.interactor.interfaces.CheckAuthenticatedUseCase;
import com.grayditch.netarea.domain.interactor.interfaces.CheckNewQualificationsUseCase;
import com.grayditch.netarea.domain.interactor.interfaces.GetQualificationsUseCase;
import com.grayditch.netarea.domain.repository.QualificationsRepository;
import com.grayditch.netarea.domain.repository.UserRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by gerard on 21/04/16.
 */
public class CheckNewQualificationsUseCaseImpl implements CheckNewQualificationsUseCase {

    private final UserRepository userRepository;
    private final ThreadExecutor threadExecutor;
    private final PostExecutionThread postExecutionThread;
    private final QualificationsRepository qualificationsRepository;

    private CheckNewQualificationsUseCase.Callback callback;

    @Inject
    public CheckNewQualificationsUseCaseImpl(UserRepository userRepository,
                                             QualificationsRepository qualificationsRepository,
                                             ThreadExecutor threadExecutor,
                                             PostExecutionThread postExecutionThread) {
        this.userRepository = userRepository;
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
        this.qualificationsRepository = qualificationsRepository;
    }

    @Override
    public void execute(CheckNewQualificationsUseCase.Callback callback) {
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
            qualificationsRepository.getNewQualifications(userDetails, qualificationsRepositoryCallback);
        }

        @Override
        public void onError(Throwable e) {       }
    };

    private QualificationsRepository.Callback qualificationsRepositoryCallback = new QualificationsRepository.Callback() {
        @Override
        public void onSuccess(final List<Subject> subjects) {
            postExecutionThread.post(new Runnable() {
                @Override
                public void run() {
                    callback.newQualifications(subjects);
                }
            });

        }

        @Override
        public void onError(Throwable e) {

        }

    };
}
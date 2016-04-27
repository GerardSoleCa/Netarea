package com.grayditch.netarea.presentation.views.mainactivity.fragments.qualifications;

import com.grayditch.netarea.domain.Subject;
import com.grayditch.netarea.domain.interactor.interfaces.GetQualificationsUseCase;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by gerard on 12/04/16.
 */
public class QualificationsPresenterImpl implements QualificationsPresenter {
    private final GetQualificationsUseCase getQualificationsUseCase;
    private QualificationsView view;

    @Inject
    public QualificationsPresenterImpl(GetQualificationsUseCase getQualificationsUseCase) {
        this.getQualificationsUseCase = getQualificationsUseCase;
    }

    @Override
    public void fetchQualifications() {
        this.getQualificationsUseCase.execute(this.getQualificationsUseCaseCallback);
    }

    @Override
    public void onCreate(QualificationsView view) {
        this.view = view;
    }

    @Override
    public void onDestroy() {
        this.view = null;
    }

    private final GetQualificationsUseCase.Callback getQualificationsUseCaseCallback = new GetQualificationsUseCase.Callback() {
        @Override
        public void onSuccess(List<Subject> subjects) {
            QualificationsPresenterImpl.this.view.showQualifications(subjects);
        }

        @Override
        public void onError(Throwable e) {

        }
    };
}

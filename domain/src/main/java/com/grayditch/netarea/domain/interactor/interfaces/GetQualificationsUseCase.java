package com.grayditch.netarea.domain.interactor.interfaces;

import com.grayditch.netarea.domain.Subject;

import java.util.List;

/**
 * Created by gerard on 18/04/16.
 */
public interface GetQualificationsUseCase extends Interactor {

    void execute(Callback callback);

    interface Callback {
        void onSuccess(List<Subject> subjects);

        void onError(Throwable e);
    }
}

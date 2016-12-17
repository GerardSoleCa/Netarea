package com.grayditch.netarea.domain.interactor.interfaces;

import com.grayditch.netarea.domain.Subject;

import java.util.List;

/**
 * Created by gerard on 21/04/16.
 */
public interface CheckNewQualificationsUseCase extends Interactor {
    void execute(Callback callback);

    interface Callback {
        void newQualifications(List<Subject> subjects);
    }
}

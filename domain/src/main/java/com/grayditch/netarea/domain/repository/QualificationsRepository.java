package com.grayditch.netarea.domain.repository;

import com.grayditch.netarea.domain.Subject;
import com.grayditch.netarea.domain.UserDetails;

import java.util.List;

import rx.Observable;

/**
 * Created by gerard on 13/04/16.
 */
public interface QualificationsRepository {

    void getQualifications(UserDetails userDetails, Callback callback);

    void getNewQualifications(UserDetails userDetails, Callback callback);

    void storeQualifications(List<Subject> subjects, Callback callback);

    interface Callback {
        void onSuccess(List<Subject> subjects);

        void onError(Throwable e);
    }
}

package com.grayditch.netarea.domain.repository;

import com.grayditch.netarea.domain.Subject;
import com.grayditch.netarea.domain.UserDetails;

import java.util.List;

import rx.Observable;

/**
 * Created by gerard on 15/04/16.
 */
public interface UserRepository {
    void getUserDetails(Callback callback);

    void storeUserDetails(UserDetails userDetails);

    boolean isAuthenticated();

    interface Callback {
        void onSuccess(UserDetails userDetails);

        void onError(Throwable e);
    }
}

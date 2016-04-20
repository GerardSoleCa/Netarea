package com.grayditch.netarea.domain.interactor;

import com.grayditch.netarea.domain.UserDetails;

/**
 * Created by gerard on 18/04/16.
 */
public interface CheckAuthenticatedUseCase extends Interactor {

    void execute(Callback callback);

    interface Callback{
        void onSuccess(Boolean isAuthenticated);
        void onError(Throwable e);
    }
}

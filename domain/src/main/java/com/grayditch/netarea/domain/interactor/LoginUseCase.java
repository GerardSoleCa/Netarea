package com.grayditch.netarea.domain.interactor;

import com.grayditch.netarea.domain.UserDetails;

/**
 * Created by gerard on 18/04/16.
 */
public interface LoginUseCase extends Interactor {

    void execute(UserDetails userDetails, Callback callback);

    interface Callback{
        void onSuccess();
        void onError(Throwable e);
    }
}

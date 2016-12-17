package com.grayditch.netarea.presentation.views.mainactivity.fragments.login;

import android.util.Log;

import com.grayditch.netarea.domain.UserDetails;
import com.grayditch.netarea.domain.interactor.interfaces.LoginUseCase;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

/**
 * Created by gerard on 9/04/16.
 */
public class LoginPresenterImpl implements LoginPresenter {
    private final LoginUseCase loginUseCase;

    private WeakReference<LoginView> view;

    @Inject
    public LoginPresenterImpl(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @Override
    public void onCreate(LoginView view) {
        this.view = new WeakReference<>(view);
    }

    @Override
    public void login(final String username, final String password) {
        if (LoginPresenterImpl.this.view.get() != null) {
            LoginPresenterImpl.this.view.get().showProgress();
        }
        UserDetails userDetails = new UserDetails();
        userDetails.setUsername(username);
        userDetails.setPassword(password);
        this.loginUseCase.execute(userDetails, this.loginUseCaseCallback);
    }

    @Override
    public void onDestroy() {
    }

    private final LoginUseCase.Callback loginUseCaseCallback = new LoginUseCase.Callback() {
        @Override
        public void onSuccess() {
            // Two onSuccess due to the way that works the storaging for quick catching
            if (LoginPresenterImpl.this.view != null) {
                LoginView v = LoginPresenterImpl.this.view.get();
                if (v != null) {
                    v.hideProgress();
                    v.navigateToQualifications();
                }
            }
        }

        @Override
        public void onError(Throwable e) {
            // Two onSuccess due to the way that works the storaging for quick catching
            LoginView v = LoginPresenterImpl.this.view.get();
            if (v != null) {
                v.hideProgress();
            }
        }
    };

}

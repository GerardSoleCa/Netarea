package com.grayditch.netarea.presentation.views.mainactivity.fragments.login;

import com.grayditch.netarea.domain.UserDetails;
import com.grayditch.netarea.domain.interactor.interfaces.LoginUseCase;

import javax.inject.Inject;

/**
 * Created by gerard on 9/04/16.
 */
public class LoginPresenterImpl implements LoginPresenter {
    private final LoginUseCase loginUseCase;

    private LoginView view;

    @Inject
    public LoginPresenterImpl(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @Override
    public void onCreate(LoginView view) {
        this.view = view;
    }

    @Override
    public void login(final String username, final String password) {
        this.view.showProgress();
        UserDetails userDetails = new UserDetails();
        userDetails.setUsername(username);
        userDetails.setPassword(password);
        this.loginUseCase.execute(userDetails, this.loginUseCaseCallback);
    }

    @Override
    public void onDestroy() {
        this.view = null;
    }

    private final LoginUseCase.Callback loginUseCaseCallback = new LoginUseCase.Callback() {
        @Override
        public void onSuccess() {
            LoginPresenterImpl.this.view.hideProgress();
            LoginPresenterImpl.this.view.navigateToQualifications();
        }

        @Override
        public void onError(Throwable e) {
            LoginPresenterImpl.this.view.hideProgress();
        }
    };

}

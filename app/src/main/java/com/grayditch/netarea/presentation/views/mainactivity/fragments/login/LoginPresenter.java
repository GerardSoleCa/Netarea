package com.grayditch.netarea.presentation.views.mainactivity.fragments.login;

/**
 * Created by gerard on 9/04/16.
 */
public interface LoginPresenter {
    void login(String username, String password);
    void onCreate(LoginView view);
    void onDestroy();
}

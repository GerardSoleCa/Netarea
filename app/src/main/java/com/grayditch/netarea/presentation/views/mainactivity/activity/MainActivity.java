package com.grayditch.netarea.presentation.views.mainactivity.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.grayditch.netarea.R;
import com.grayditch.netarea.domain.interactor.interfaces.CheckAuthenticatedUseCase;
import com.grayditch.netarea.presentation.App;
import com.grayditch.netarea.presentation.views.mainactivity.fragments.login.LoginFragment;
import com.grayditch.netarea.presentation.views.mainactivity.fragments.qualifications.QualificationsFragment;
import com.grayditch.netarea.presentation.views.mainactivity.fragments.settings.SettingsFragment;

import javax.inject.Inject;


public class MainActivity extends AppCompatActivity implements LoginFragment.OnLoginInteractionListener, QualificationsFragment.OnQualificationsInteractionListener {

    @Inject
    CheckAuthenticatedUseCase checkAuthenticatedUseCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.component(this).inject(this);
        this.checkAuthenticatedUseCase.execute(new CheckAuthenticatedUseCase.Callback() {
            @Override
            public void onSuccess(Boolean isAuthenticated) {
                if (isAuthenticated) {
                    MainActivity.this.navigateToFragment(QualificationsFragment.newInstance());
                } else {
                    MainActivity.this.navigateToFragment(LoginFragment.newInstance());
                }
            }

            @Override
            public void onError(Throwable e) {
                MainActivity.this.navigateToFragment(LoginFragment.newInstance());
            }
        });
        setContentView(R.layout.main_activity);
    }

    @Override
    public void onLoginFinished() {
        this.navigateToFragment(QualificationsFragment.newInstance());
    }

    private void navigateToFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_view, fragment);
        ft.commit();
    }

    private void showFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_view, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onShowSettings() {
        this.showFragment(SettingsFragment.newInstance());
    }
}

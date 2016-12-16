package com.grayditch.netarea.presentation.views.mainactivity.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.grayditch.netarea.R;
import com.grayditch.netarea.domain.interactor.interfaces.CheckAuthenticatedUseCase;
import com.grayditch.netarea.presentation.App;
import com.grayditch.netarea.presentation.views.mainactivity.fragments.login.LoginFragment;
import com.grayditch.netarea.presentation.views.mainactivity.fragments.qualifications.QualificationsFragment;
import com.grayditch.netarea.presentation.views.mainactivity.fragments.settings.SettingsFragment;

import javax.inject.Inject;


public class MainActivity extends AppCompatActivity implements LoginFragment.OnLoginInteractionListener, QualificationsFragment.OnQualificationsInteractionListener, SettingsFragment.OnSettingsInteractionListener {

    @Inject
    CheckAuthenticatedUseCase checkAuthenticatedUseCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.component(this).inject(this);
        setContentView(R.layout.main_activity);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        this.checkAuthenticatedUseCase.execute(new CheckAuthenticatedUseCase.Callback() {
            @Override
            public void isAuthenticated(boolean isAuthenticated) {
                if (isAuthenticated) {
                    MainActivity.this.navigateToFragment(QualificationsFragment.newInstance());
                } else {
                    MainActivity.this.navigateToFragment(LoginFragment.newInstance());
                }
            }
        });
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

    private void showFragment(Fragment fragment) {
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

    @Override
    public void onLogout() {
        this.showFragment(LoginFragment.newInstance());
    }
}

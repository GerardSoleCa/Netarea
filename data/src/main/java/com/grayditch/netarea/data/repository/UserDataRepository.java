package com.grayditch.netarea.data.repository;

import android.content.SharedPreferences;

import com.grayditch.netarea.domain.UserDetails;
import com.grayditch.netarea.domain.repository.UserRepository;

import javax.inject.Inject;

/**
 * Created by gerard on 19/04/16.
 */
public class UserDataRepository implements UserRepository {
    private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";
    private SharedPreferences sharedPreferences;

    @Inject
    public UserDataRepository(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void getUserDetails(Callback callback) {
        UserDetails userDetails = new UserDetails();
        userDetails.setUsername(this.sharedPreferences.getString(USERNAME, null));
        userDetails.setPassword(this.sharedPreferences.getString(PASSWORD, null));
        if (userDetails.getPassword() == null) {
            callback.onError(new Exception("User is not stored"));
        } else {
            callback.onSuccess(userDetails);
        }
    }

    @Override
    public void storeUserDetails(UserDetails userDetails, Callback callback) {
        this.storeMode().putString(USERNAME, userDetails.getUsername())
                .putString(PASSWORD, userDetails.getPassword())
                .apply();
    }

    @Override
    public boolean isAuthenticated() {
        return this.sharedPreferences.getString(USERNAME, null) != null;
    }

    private SharedPreferences.Editor storeMode() {
        return this.sharedPreferences.edit();
    }
}

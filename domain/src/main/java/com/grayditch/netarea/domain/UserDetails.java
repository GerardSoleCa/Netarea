package com.grayditch.netarea.domain;

import javax.inject.Inject;

/**
 * Created by gerard on 13/04/16.
 */
public class UserDetails {
//    private static final String SHAREDPREFS_NAMESPACE = "NETAREA_USERDETAILS";
//    private static final String USERNAME = "USERNAME";
//    private static final String PASSWORD = "PASSWORD";

    private String username;
    private String password;

    @Inject
    public UserDetails() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}

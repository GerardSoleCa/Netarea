package com.grayditch.netarea.presentation.views.mainactivity.fragments.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;

import com.grayditch.netarea.R;
import com.grayditch.netarea.presentation.views.mainactivity.activity.MainActivity;
import com.grayditch.netarea.presentation.views.mainactivity.fragments.login.LoginFragment;

import butterknife.OnClick;

/**
 * Created by gerard on 21/04/16.
 */
public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SharedPreferences sharedPreferences;


    private OnSettingsInteractionListener mListener;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }


    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        //add xml
        setPreferenceScreen(null);
        addPreferencesFromResource(R.xml.preferences_fragment_view);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        addListeners();
    }

    public void addListeners() {
        Preference reset = findPreference("logout");
        reset.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                sharedPreferences.edit().clear().apply();
                mListener.onLogout();
                return true;
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        //unregister the preferenceChange listener
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
    }

    @Override
    public void onPause() {
        super.onPause();
        //unregister the preference change listener
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnSettingsInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnSettingsInteractionListener");
        }
    }

    public interface OnSettingsInteractionListener {
        void onLogout();
    }
}

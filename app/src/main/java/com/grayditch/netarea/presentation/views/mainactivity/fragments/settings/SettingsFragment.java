package com.grayditch.netarea.presentation.views.mainactivity.fragments.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import com.grayditch.netarea.R;
import com.grayditch.netarea.presentation.App;
import com.grayditch.netarea.presentation.receivers.JobScheduler;

import javax.inject.Inject;

/**
 * Created by gerard on 21/04/16.
 */
public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = SettingsFragment.class.getName();

    @Inject
    JobScheduler jobScheduler;

    @Inject
    SharedPreferences sharedPreferences;

    private OnSettingsInteractionListener mListener;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }


    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        App.component(this.getContext()).inject(this);
        //add xml
        setPreferenceScreen(null);
        addPreferencesFromResource(R.xml.preferences_fragment_view);

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
        switch (key) {
            case "backgroundQualificationsPref":

                Log.v(TAG, "onSharedPreferenceChanged");
                jobScheduler.schedule();
                break;
        }
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

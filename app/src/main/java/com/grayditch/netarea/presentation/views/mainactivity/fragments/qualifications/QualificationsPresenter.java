package com.grayditch.netarea.presentation.views.mainactivity.fragments.qualifications;

/**
 * Created by gerard on 12/04/16.
 */
public interface QualificationsPresenter {
    void fetchQualifications();
    void onCreate(QualificationsView view);
    void onDestroy();
}

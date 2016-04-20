package com.grayditch.netarea.data.repository;

import com.grayditch.netarea.data.database.QualificationsDB;
import com.grayditch.netarea.data.network.NetareaClient;
import com.grayditch.netarea.domain.Subject;
import com.grayditch.netarea.domain.UserDetails;
import com.grayditch.netarea.domain.repository.QualificationsRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by gerard on 14/04/16.
 */
public class QualificationsDataRepository implements QualificationsRepository {

    private final NetareaClient netareaClient;
    private final QualificationsDB qualificationsDB;

    @Inject
    public QualificationsDataRepository(NetareaClient netareaClient, QualificationsDB qualificationsDB) {
        this.netareaClient = netareaClient;
        this.qualificationsDB = qualificationsDB;
    }

    @Override
    public void getQualifications(final UserDetails userDetails, final Callback callback) {

        this.qualificationsDB.qualifications(new QualificationsDB.Callback() {
            @Override
            public void onSuccess(List<Subject> subjects) {
                callback.onSuccess(subjects);
                if(netareaClient.isNetworkAvailable()) {
                    netareaClient.qualifications(userDetails, new NetareaClient.Callback() {
                        @Override
                        public void onSuccess(List<Subject> subjects) {
                            callback.onSuccess(subjects);
                            storeQualifications(subjects, callback);
                        }

                        @Override
                        public void onError(Throwable e) {
                            callback.onError(e);
                        }
                    });
                }
            }

            @Override
            public void onError(Throwable e) {
                callback.onError(e);
            }
        });
    }

    @Override
    public void storeQualifications(List<Subject> subjects, final Callback callback) {
        this.qualificationsDB.storeQualifications(subjects, new QualificationsDB.Callback() {
            @Override
            public void onSuccess(List<Subject> subjects) {
                callback.onSuccess(subjects);
            }

            @Override
            public void onError(Throwable e) {
                callback.onError(e);
            }
        });

    }
}

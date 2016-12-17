package com.grayditch.netarea.data.repository;

import com.grayditch.netarea.data.comparators.SubjectComparator;
import com.grayditch.netarea.data.database.QualificationsDB;
import com.grayditch.netarea.data.network.NetareaClient;
import com.grayditch.netarea.domain.Mark;
import com.grayditch.netarea.domain.Subject;
import com.grayditch.netarea.domain.UserDetails;
import com.grayditch.netarea.domain.repository.QualificationsRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by gerard on 14/04/16.
 */
public class QualificationsDataRepository implements QualificationsRepository {

    private final NetareaClient netareaClient;
    private final QualificationsDB qualificationsDB;
    private final SubjectComparator subjectComparator = new SubjectComparator();

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
                if (netareaClient.isNetworkAvailable()) {
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
    public void getNewQualifications(final UserDetails userDetails, final Callback callback) {
        this.qualificationsDB.qualifications(new QualificationsDB.Callback() {
            @Override
            public void onSuccess(final List<Subject> dbSubjects) {
                if (!netareaClient.isNetworkAvailable()) {
                    callback.onError(new Throwable("No internet connection"));
                } else {
                    netareaClient.qualifications(userDetails, new NetareaClient.Callback() {
                        @Override
                        public void onSuccess(List<Subject> nwSubjects) {
                            List<Subject> newQualifications = QualificationsDataRepository.this.getDisjunction(nwSubjects, dbSubjects);
//                            newQualifications.add(new Subject("NEW SUBJECT", Arrays.asList(new Mark(true, "Test mark", "2", "9.0"))));
                            if (newQualifications.size() > 0) {
                                callback.onSuccess(newQualifications);
                            }
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

    private List<Subject> getDisjunction(List<Subject> s1, List<Subject> s2) {
        Collections.sort(s1, subjectComparator);
        Collections.sort(s2, subjectComparator);
        List<Subject> subjectsToReturn = new ArrayList<>(s1);
        subjectsToReturn.removeAll(s2);
        for (int i = 0; i < s1.size() - 1; i++) {
            for (int j = 0; j < s2.size() - 1; j++) {
                if (s1.get(i).getName().equals(s2.get(j).getName())) {
                    List<Mark> newMarks = new ArrayList<>(s1.get(i).getMarks());
                    newMarks.removeAll(s2.get(j).getMarks());
                    if (newMarks.size() > 0) {
                        subjectsToReturn.add(new Subject(s1.get(i).getName(), newMarks));
                    }
                }
            }
        }
        Collections.sort(subjectsToReturn, subjectComparator);
        return subjectsToReturn;
    }
}

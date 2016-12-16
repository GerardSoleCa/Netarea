package com.grayditch.netarea.data.database;

import android.content.Context;

import com.grayditch.netarea.data.database.dao.MarkDAO;
import com.grayditch.netarea.data.database.dao.SubjectDAO;
import com.grayditch.netarea.domain.Subject;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by gerard on 20/04/16.
 */
public class QualificationsDB {

    private static final String NETAREA = "NETAREA";
    private Context context;
    private RealmConfiguration realmConfiguration;

    @Inject
    public QualificationsDB(Context context) {
        this.context = context;
        this.realmConfiguration = new RealmConfiguration.Builder(context)
                .name(NETAREA)
                .deleteRealmIfMigrationNeeded()
                .build();
    }

    public void storeQualifications(List<Subject> subjects, Callback callback) {
        this.clear(SubjectDAO.class);
        this.clear(MarkDAO.class);
        this.save(EntityMapper.transformToDao(subjects));
        callback.onSuccess(subjects);
    }

    public void qualifications(Callback callback) {
        List<SubjectDAO> subjectDAOs = this.getAll(SubjectDAO.class);
        callback.onSuccess(EntityMapper.transform(subjectDAOs));
    }

    private Realm openRealm() {
        return Realm.getInstance(this.realmConfiguration);
    }


    private <T extends RealmObject> void save(List<T> t) {
        Realm realm = this.openRealm();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(t);
        realm.commitTransaction();
        realm.close();
    }

    private <T extends RealmObject> List<T> getAll(Class<T> t) {
        Realm realm = this.openRealm();
        RealmResults<T> subjectDAORealmResults = realm.where(t).findAll();
        List<T> list = realm.copyFromRealm(subjectDAORealmResults);
        realm.close();
        return list;
    }

    private <T extends RealmObject> void clear(Class<T> t) {
        Realm realm = this.openRealm();
        realm.beginTransaction();
        realm.clear(t);
        realm.commitTransaction();
        realm.close();
    }

    public interface Callback {
        void onSuccess(List<Subject> subjects);

        void onError(Throwable e);
    }
}

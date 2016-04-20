package com.grayditch.netarea.presentation.activities.qualifications;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grayditch.netarea.R;
import com.grayditch.netarea.domain.Subject;
import com.grayditch.netarea.presentation.App;
import com.grayditch.netarea.presentation.activities.qualifications.adapter.SubjectAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;


public class QualificationsFragment extends Fragment implements QualificationsView {

    @Inject
    QualificationsPresenter presenter;

    @Bind(R.id.subjects_recyclerview)
    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public QualificationsFragment() {
    }

    public static QualificationsFragment newInstance() {
        return new QualificationsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.component(getContext()).inject(this);
        this.presenter.onCreate(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.qualifications_fragment, container, false);
        ButterKnife.bind(this, view);
        this.recyclerView.setHasFixedSize(true);
        this.layoutManager = new LinearLayoutManager(getContext());
        this.recyclerView.setLayoutManager(this.layoutManager);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.presenter.fetchQualifications();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.presenter.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void showQualifications(List<Subject> subjectList) {
        this.recyclerView.setAdapter(new SubjectAdapter(subjectList));
    }
}
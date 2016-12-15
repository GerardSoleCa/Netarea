package com.grayditch.netarea.presentation.views.mainactivity.fragments.qualifications;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.grayditch.netarea.R;
import com.grayditch.netarea.domain.Subject;
import com.grayditch.netarea.presentation.App;
import com.grayditch.netarea.presentation.views.mainactivity.fragments.qualifications.adapter.SubjectAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;


public class QualificationsFragment extends Fragment implements QualificationsView {

    @Inject
    QualificationsPresenter presenter;

    @Bind(R.id.subjects_recyclerview)
    RecyclerView recyclerView;


    private OnQualificationsInteractionListener mListener;

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
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
        }
        setHasOptionsMenu(true);
        this.presenter.onCreate(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.qualifications_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.open_settings:
                this.mListener.onShowSettings();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroy() {
        this.presenter.onDestroy();
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @Override
    public void onAttach(Context context) {
        try {
            mListener = (OnQualificationsInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        this.mListener = null;
        super.onDetach();
    }

    @Override
    public void showQualifications(List<Subject> subjectList) {
        this.recyclerView.setAdapter(new SubjectAdapter(subjectList));
    }

    public interface OnQualificationsInteractionListener {
        void onShowSettings();
    }
}

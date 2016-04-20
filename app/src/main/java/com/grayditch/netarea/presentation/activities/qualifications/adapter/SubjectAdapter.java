package com.grayditch.netarea.presentation.activities.qualifications.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grayditch.netarea.R;
import com.grayditch.netarea.domain.Subject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gerard on 12/04/16.
 */
public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {

    private final List<Subject> subjects;

    public SubjectAdapter(List<Subject> subjects) {
        this.subjects = subjects;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subject_recycler_view, parent, false);
        return new ViewHolder(v, parent.getContext());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Subject subject = this.subjects.get(position);
        holder.subjectName.setText(subject.getName());
        holder.recyclerView.setAdapter(new MarkAdapter(subject.getMarks()));
    }

    @Override
    public int getItemCount() {
        return this.subjects.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.qualifications_recyclerview)
        RecyclerView recyclerView;
        private LinearLayoutManager layoutManager;

        @Bind(R.id.subject_name_textview)
        TextView subjectName;

        public ViewHolder(View view, Context context) {
            super(view);
            ButterKnife.bind(this, view);
            this.recyclerView.setHasFixedSize(true);
            this.layoutManager = new LinearLayoutManager(context);
            this.recyclerView.setLayoutManager(this.layoutManager);
        }
    }
}

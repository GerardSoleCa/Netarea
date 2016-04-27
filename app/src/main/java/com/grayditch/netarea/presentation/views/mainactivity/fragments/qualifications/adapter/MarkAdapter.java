package com.grayditch.netarea.presentation.views.mainactivity.fragments.qualifications.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.grayditch.netarea.R;
import com.grayditch.netarea.domain.Mark;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gerard on 12/04/16.
 */
public class MarkAdapter extends RecyclerView.Adapter<MarkAdapter.ViewHolder> {
    private static final String NO_MARKS_YET = "No s'han publicat qualificacions d'aquesta assignatura";

    private final List<Mark> marks;

    public MarkAdapter(List<Mark> marks) {
        this.marks = marks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subject_mark_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Mark mark = this.marks.get(position);
        holder.description.setText(mark.getDescription());
        holder.qualification.setText(mark.getMark());
        holder.percentage.setText(mark.getPercentage());
        if (mark.isNew()) {
            holder.flag.setColorFilter(R.color.colorAccent);
        } else {
            holder.flag.setColorFilter(R.color.colorPrimaryDark);
        }
        if (mark.getDescription().equals(NO_MARKS_YET)){
            holder.flag.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return this.marks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.flag_imageview)
        ImageView flag;

        @Bind(R.id.info_textview)
        TextView description;

        @Bind(R.id.percentage_textview)
        TextView percentage;

        @Bind(R.id.mark_textview)
        TextView qualification;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

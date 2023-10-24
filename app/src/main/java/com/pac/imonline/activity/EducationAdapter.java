package com.pac.imonline.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pac.imonline.R;

import java.util.ArrayList;
import java.util.List;

public class EducationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private boolean showPlaceholder = true;

    private List<EducationActivity> educationActivityList;
    private EducationAdapterEventListener eventListener;
    public EducationAdapter(EducationAdapterEventListener eventListener) {
        this.educationActivityList = new ArrayList<>();
        this.eventListener = eventListener;
    }
    /**
     * Criar um novo ViewHolder sempre que for necessário
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return
     */


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.education_item, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.education_placeholder, parent, false);
            return new PlaceholderViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            final EducationActivity educationActivity = this.educationActivityList.get(position);

            viewHolder.uni_name.setText(educationActivity.getUniversity_name());
            viewHolder.date.setText(educationActivity.getDate());
            viewHolder.graduation.setText(educationActivity.getGraduation());
            viewHolder.description.setText(educationActivity.getDescription());

            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (eventListener != null) {
                        eventListener.onEducationLongClicked(educationActivity.getId());
                        return true;
                    } else {
                        return false;
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return educationActivityList.size();
    }



    @Override
    public int getItemViewType(int position) {
        if (position < educationActivityList.size()) {
            return VIEW_TYPE_ITEM;
        } else {
            return VIEW_TYPE_PLACEHOLDER;
        }
    }

    public void refreshList(List<EducationActivity> newEducationList) {
        educationActivityList.clear();
        educationActivityList.addAll(newEducationList);
        showPlaceholder = educationActivityList.isEmpty();
        notifyDataSetChanged();
    }

    public class PlaceholderViewHolder extends RecyclerView.ViewHolder {
        public PlaceholderViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView uni_name;
        public TextView date;
        public TextView graduation;
        public TextView description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            uni_name = itemView.findViewById(R.id.textViewCompanyName);
            date = itemView.findViewById(R.id.textViewDateWork);
            graduation = itemView.findViewById(R.id.textViewRole);
            description = itemView.findViewById(R.id.textViewDescriptionWork);
        }
    }

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_PLACEHOLDER = 1;

    public interface EducationAdapterEventListener {
        void onEducationLongClicked(long educationActivityId);

    }
}



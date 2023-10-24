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

public class WorkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private boolean showPlaceholder = true;
    private List<WorkActivity> listWork;
    private WorkAdapterEventListener eventListener;

    public WorkAdapter(WorkAdapterEventListener eventListener) {
        this.listWork = new ArrayList<>();
        this.eventListener = eventListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_experience_item, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_placeholder, parent, false);
            return new PlaceholderViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            final WorkActivity work = listWork.get(position);
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.textViewCompanyName.setText(work.getCompany_name());
            viewHolder.textViewDateWork.setText(work.getDate());
            viewHolder.textViewRole.setText(work.getRole());
            viewHolder.textViewDescriptionWork.setText(work.getDescription());

            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (eventListener != null) {
                        eventListener.onWorkLongClicked(work.getId());
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
        return showPlaceholder ? listWork.size() + 1 : listWork.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < listWork.size()) {
            return VIEW_TYPE_ITEM;
        } else {
            return VIEW_TYPE_PLACEHOLDER;
        }
    }

    public void refreshListWork(List<WorkActivity> newWorkList) {
        listWork = newWorkList;
        showPlaceholder = newWorkList.isEmpty();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewCompanyName;
        public TextView textViewDateWork;
        public TextView textViewRole;
        public TextView textViewDescriptionWork;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCompanyName = itemView.findViewById(R.id.textViewCompanyName);
            textViewDateWork = itemView.findViewById(R.id.textViewDateWork);
            textViewRole = itemView.findViewById(R.id.textViewRole);
            textViewDescriptionWork = itemView.findViewById(R.id.textViewDescriptionWork);
        }
    }

    public class PlaceholderViewHolder extends RecyclerView.ViewHolder {
        public PlaceholderViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public interface WorkAdapterEventListener {
        void onWorkLongClicked(long workId); // Change the parameter type to long
    }

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_PLACEHOLDER = 1;
}

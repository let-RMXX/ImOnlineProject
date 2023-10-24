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

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    private List<ProfileActivity> profileActivityList;
    private ProfileAdapterEventListener eventListener;

    public ProfileAdapter(ProfileAdapterEventListener eventListener) {
        this.profileActivityList = new ArrayList<>();
        this.eventListener = eventListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ProfileActivity profileActivity = this.profileActivityList.get(position);

        holder.textViewName.setText(profileActivity.getName());
        holder.textViewAbout.setText(profileActivity.getAbout());
        holder.textViewPhone.setText(profileActivity.getPhoneNumber());
        holder.textViewEmail.setText(profileActivity.getEmail());
        holder.textViewLocation.setText(profileActivity.getLocation());
    }

    @Override
    public int getItemCount() {
        return profileActivityList.size();
    }

    public void refreshListProfile(List<ProfileActivity> newProfileList){
        this.profileActivityList = newProfileList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewName;
        public TextView textViewAbout;
        public TextView textViewPhone;
        public TextView textViewEmail;
        public TextView textViewLocation;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewAbout = itemView.findViewById(R.id.textViewAbout);
            textViewPhone = itemView.findViewById(R.id.textViewPhone);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);
        }
    }

    public interface ProfileAdapterEventListener {
    }
}

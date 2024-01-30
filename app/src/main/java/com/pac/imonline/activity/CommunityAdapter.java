package com.pac.imonline.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pac.imonline.R;

import java.util.List;

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder> {

    private List<Community> communityList;

    public CommunityAdapter(List<Community> communityList) {
        this.communityList = communityList;
    }

    @NonNull
    @Override
    public CommunityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_community, parent, false);
        return new CommunityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityViewHolder holder, int position) {
        Community community = communityList.get(position);
        holder.bind(community);
    }

    @Override
    public int getItemCount() {
        return communityList.size();
    }

    public class CommunityViewHolder extends RecyclerView.ViewHolder {

        private ImageView photoImageView;
        private ImageView bannerImageView;
        private TextView nameTextView;

        public CommunityViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImageView = itemView.findViewById(R.id.photoImageView);
            bannerImageView = itemView.findViewById(R.id.bannerImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
        }

        public void bind(Community community) {
            nameTextView.setText(community.getName());

            // Load photo image using Glide library
            if (community.getPhotoUrl() != null) {
                Glide.with(itemView.getContext())
                        .load(community.getPhotoUrl())
                        .apply(new RequestOptions().override(500, 500)) // Adjust the dimensions as needed
                        .into(photoImageView);
            } else {
                photoImageView.setImageResource(R.drawable.defimgcommunities);
            }

            if (community.getBannerUrl() != null) {
                Glide.with(itemView.getContext())
                        .load(community.getBannerUrl())
                        .apply(new RequestOptions())
                        .into(bannerImageView);
            }
        }
    }
}

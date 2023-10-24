package com.pac.imonline.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.pac.imonline.R;

import java.util.ArrayList;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.MyViewHolder> {

    Context context;
    ArrayList<ModelFeed> modelFeedArrayList = new ArrayList<>();
    RequestManager glide;

    public FeedAdapter(Context context, ArrayList<ModelFeed> modelFeedArrayList){

        this.context = context;
        this.modelFeedArrayList = modelFeedArrayList;
        glide = Glide.with(context);
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_feed, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ModelFeed modelFeed = modelFeedArrayList.get(position);
        holder.tv_name.setText(modelFeed.getName());
        holder.tv_time.setText(modelFeed.getTime());
        holder.tv_like.setText(String.valueOf(modelFeed.getLikes()));
        holder.tv_comments.setText(modelFeed.getComments() + "comments");
        holder.tv_status.setText(modelFeed.getStatus());

        glide.load(modelFeed.getPropic()).into(holder.ImageViewProfPic);

        if (modelFeed.getPostpic() == 0){
            holder.imageView_postPic.setVisibility(View.GONE);

        }else{
            holder.imageView_postPic.setVisibility(View.VISIBLE);
            glide.load(modelFeed.getPostpic()).into(holder.imageView_postPic);

        }
    }

    @Override
    public int getItemCount() {
        return modelFeedArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name, tv_time, tv_like, tv_comments, tv_status;
        ImageView ImageViewProfPic, imageView_postPic;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ImageViewProfPic = itemView.findViewById(R.id.ImageViewProfPic);
            imageView_postPic = itemView.findViewById(R.id.imageView_postPic);

            tv_name = itemView.findViewById(R.id.tv_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_like = itemView.findViewById(R.id.tv_like);
            tv_comments = itemView.findViewById(R.id.tv_comment);
            tv_status = itemView.findViewById(R.id.tv_status);
        }
    }
}

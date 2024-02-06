package com.pac.imonline.activity.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pac.imonline.R;
import com.pac.imonline.activity.CommentActivity;
import com.pac.imonline.activity.Constant;
import com.pac.imonline.activity.EditPostActivity;
import com.pac.imonline.activity.HomeActivity;
import com.pac.imonline.activity.Api.ApiService;
import com.pac.imonline.activity.Api.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;

import com.pac.imonline.activity.Models.Posts;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsHolder> {

    private Context context;
    private ArrayList<Posts> list;
    private ArrayList<Posts> listAll;
    private SharedPreferences preferences;
    private ApiService apiService;
    private String token;

    public PostsAdapter(Context context, ArrayList<Posts> list) {
        this.context = context;
        this.list = list;
        this.listAll = new ArrayList<>(list);
        preferences = context.getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        // Initialize Retrofit
        apiService = RetrofitClient.createService();
        token = preferences.getString("token", "");  // Make sure to replace with your actual token key
    }

    @NonNull
    @Override
    public PostsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_home, parent, false);
        return new PostsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsHolder holder, int position) {
        Posts post = list.get(position);

        Picasso.get().load(Constant.URL + "storage/profiles/" + post.getUser()).into(holder.imgProfile);
        Picasso.get().load(Constant.URL + "storage/posts/" + post.getPhoto()).into(holder.imgPost);

        holder.txtName.setText(post.getUser().getUserName());
        holder.txtComments.setText("View all " + post.getComments());
        holder.txtLikes.setText(post.getLikes() + " Likes");
        holder.txtDate.setText(post.getDate());
        holder.txtDesc.setText(post.getDesc());

        holder.btnLike.setImageResource(
                post.isSelfLike() ? R.drawable.baseline_favorite_red : R.drawable.baseline_favorite_outline
        );

        // Like click
        holder.btnLike.setOnClickListener(v -> {
            holder.btnLike.setImageResource(
                    post.isSelfLike() ? R.drawable.baseline_favorite_outline : R.drawable.baseline_favorite_red
            );

            int currentPosition = holder.getAdapterPosition();

            // Use Retrofit to make the network request
            apiService.likePost("Bearer " + token, post.getId()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        // Update UI accordingly
                        post.setSelfLike(!post.isSelfLike());
                        post.setLikes(post.isSelfLike() ? post.getLikes() + 1 : post.getLikes() - 1);
                        list.set(currentPosition, post);
                        notifyItemChanged(currentPosition);
                        notifyDataSetChanged();
                    } else {
                        holder.btnLike.setImageResource(
                                post.isSelfLike() ? R.drawable.baseline_favorite_red : R.drawable.baseline_favorite_outline
                        );
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        });

        if (post.getUser().getId() == preferences.getInt("id", 0)) {
            holder.btnPostOption.setVisibility(View.VISIBLE);
        } else {
            holder.btnPostOption.setVisibility(View.GONE);
        }

        holder.btnPostOption.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.btnPostOption);
            popupMenu.inflate(R.menu.menu_post_options);
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.item_edit: {
                        Intent i = new Intent(((HomeActivity) context), EditPostActivity.class);
                        i.putExtra("postId", post.getId());
                        i.putExtra("position", position);
                        i.putExtra("text", post.getDesc());
                        context.startActivity(i);
                        return true;
                    }
                    case R.id.item_delete: {
                        deletePost(post.getId(), position);
                        return true;
                    }
                }
                return false;
            });
            popupMenu.show();
        });

        holder.txtComments.setOnClickListener(v -> {
            Intent i = new Intent(((HomeActivity) context), CommentActivity.class);
            i.putExtra("postId", post.getId());
            i.putExtra("postPosition", position);
            context.startActivity(i);
        });

        holder.btnComment.setOnClickListener(v -> {
            Intent i = new Intent(((HomeActivity) context), CommentActivity.class);
            i.putExtra("postId", post.getId());
            i.putExtra("postPosition", position);
            context.startActivity(i);
        });
    }

    private void deletePost(int postId, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm");
        builder.setMessage("Delete post?");
        builder.setPositiveButton("Delete", (dialog, which) -> {
            // Use Retrofit to delete the post
            apiService.deletePost("Bearer " + token, postId).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        list.remove(position);
                        notifyItemRemoved(position);
                        notifyDataSetChanged();
                        listAll.clear();
                        listAll.addAll(list);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        });

        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
            // Handle cancellation
        });

        builder.show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Posts> filteredList = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                filteredList.addAll(listAll);
            } else {
                for (Posts post : listAll) {
                    if (post.getDesc().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                            post.getUser().getUserName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredList.add(post);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((Collection<? extends Posts>) results.values);
            notifyDataSetChanged();
        }
    };

    public Filter getFilter() {
        return filter;
    }

    class PostsHolder extends RecyclerView.ViewHolder {
        private TextView txtName, txtDate, txtDesc, txtLikes, txtComments;
        private CircleImageView imgProfile;
        private ImageView imgPost;
        private ImageButton btnPostOption, btnLike, btnComment;

        public PostsHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtPostName);
            txtDate = itemView.findViewById(R.id.txtPostDate);
            txtDesc = itemView.findViewById(R.id.txtPostDesc);
            txtLikes = itemView.findViewById(R.id.txtPostLikes);
            txtComments = itemView.findViewById(R.id.txtPostComments);
            imgProfile = itemView.findViewById(R.id.imgPostProfile);
            imgPost = itemView.findViewById(R.id.imgPostPhoto);
            btnPostOption = itemView.findViewById(R.id.btnPostOption);
            btnPostOption.setVisibility(View.GONE);
            btnLike = itemView.findViewById(R.id.btnPostLike);
            btnComment = itemView.findViewById(R.id.btnPostComment);
        }
    }
}

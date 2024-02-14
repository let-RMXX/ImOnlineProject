package com.pac.imonline.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pac.imonline.R;
import com.pac.imonline.activity.CommentActivity;
import com.pac.imonline.activity.EditPostActivity;
import com.pac.imonline.activity.Models.Posts;
import com.pac.imonline.activity.Entities.PostEntity;
import com.pac.imonline.activity.Entities.UserEntity;
import com.pac.imonline.activity.Database.AppDatabase;

import java.util.ArrayList;
import java.util.Collection;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsHolder> implements Filterable {

    private Context context;
    private ArrayList<Posts> list;
    private ArrayList<Posts> listAll;
    private AppDatabase appDatabase;

    public PostsAdapter(Context context, ArrayList<Posts> list) {
        this.context = context;
        this.list = list;
        this.listAll = new ArrayList<>(list);
        appDatabase = AppDatabase.getAppDatabase(context);
    }

    @NonNull
    @Override
    public PostsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsHolder holder, int position) {
        Posts post = list.get(position);

        // Load profile image asynchronously
        loadProfileImage(post.getUser().getId(), holder);

        holder.txtDesc.setText(post.getDesc());

        // Set onClickListener for the comment button
        holder.btnComment.setOnClickListener(v -> {
            Intent commentIntent = new Intent(context, CommentActivity.class);
            context.startActivity(commentIntent);
        });

        // Set onClickListener for the options button
        holder.btnPostOption.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.btnPostOption);
            popupMenu.inflate(R.menu.menu_post_options);
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.item_edit:
                        Intent editIntent = new Intent(context, EditPostActivity.class);
                        context.startActivity(editIntent);
                        return true;
                    case R.id.item_delete:
                        deletePost(post.getId(), position);
                        return true;
                    default:
                        return false;
                }
            });
            popupMenu.show();
        });
    }

    private void loadProfileImage(int userId, PostsHolder holder) {
        // Use AsyncTask to perform database operation asynchronously
        class GetProfileImageTask extends AsyncTask<Void, Void, byte[]> {
            @Override
            protected byte[] doInBackground(Void... voids) {
                // Retrieve profile image data from the database in the background
                UserEntity user = appDatabase.userDao().getUserById(userId);
                if (user != null) {
                    return user.getPhotoData();
                }
                return null;
            }

            @Override
            protected void onPostExecute(byte[] profileImageData) {
                // Update the UI with the retrieved profile image data
                if (profileImageData != null) {
                    holder.imgProfile.setImageBitmap(BitmapFactory.decodeByteArray(profileImageData, 0, profileImageData.length));
                } else {
                    holder.imgProfile.setImageResource(R.drawable.default_profile_image);
                }
            }
        }

        // Execute the AsyncTask
        GetProfileImageTask task = new GetProfileImageTask();
        task.execute();
    }

    private void deletePost(int postId, int position) {
        PostEntity postEntityToDelete = getPostEntity(postId);
        if (postEntityToDelete != null) {
            appDatabase.postDao().deletePost(postEntityToDelete);

            // Remove post from recycler view
            list.remove(position);
            notifyItemRemoved(position);
        }
    }

    // Helper method to retrieve PostEntity object associated with postId
    private PostEntity getPostEntity(int postId) {
        for (Posts post : listAll) {
            if (post.getId() == postId) {
                return post.getPostEntity();
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Posts> filteredList = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                filteredList.addAll(listAll);
            } else {
                for (Posts post : listAll) {
                    if (post.getDesc().toLowerCase().contains(constraint.toString().toLowerCase())) {
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

    public void updateList(ArrayList<Posts> newPostsList) {
        list.clear();
        list.addAll(newPostsList);
        listAll.clear();
        listAll.addAll(newPostsList);
        notifyDataSetChanged();
    }

    static class PostsHolder extends RecyclerView.ViewHolder {
        private TextView txtDesc;
        private CircleImageView imgProfile;
        private ImageView imgPost;
        private ImageButton btnPostOption, btnComment;

        public PostsHolder(@NonNull View itemView) {
            super(itemView);
            txtDesc = itemView.findViewById(R.id.txtPostDesc);
            imgProfile = itemView.findViewById(R.id.imgPostProfile);
            imgPost = itemView.findViewById(R.id.imgPostPhoto);
            btnPostOption = itemView.findViewById(R.id.btnPostOption);
            btnComment = itemView.findViewById(R.id.btnPostComment);
        }
    }
}
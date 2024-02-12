package com.pac.imonline.activity.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.pac.imonline.R;
import com.pac.imonline.activity.Database.AppDatabase;
import com.pac.imonline.activity.Entities.CommentEntity;
import com.pac.imonline.activity.Models.Comment;
import com.pac.imonline.activity.Models.User;

import java.util.ArrayList;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsHolder> {

    private Context context;
    private List<CommentEntity> commentEntities;
    private AppDatabase appDatabase;

    public CommentsAdapter(Context context, List<CommentEntity> commentEntities) {
        this.context = context;
        this.commentEntities = commentEntities;
        appDatabase = AppDatabase.getAppDatabase(context);
    }

    @NonNull
    @Override
    public CommentsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment, parent, false);
        return new CommentsHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsHolder holder, int position) {
        CommentEntity commentEntity = commentEntities.get(position);
        holder.txtName.setText(commentEntity.getUserName());
        holder.txtDate.setText(commentEntity.getDate());
        holder.txtComment.setText(commentEntity.getComment());

        holder.btnDelete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Are you sure?");
            builder.setPositiveButton("Delete", (dialogInterface, which) -> {
                deleteComment(commentEntity, position);
            });
            builder.setNegativeButton("Cancel", (dialogInterface, which) -> {
                // Do nothing
            });
            builder.show();
        });
    }

    private void deleteComment(CommentEntity commentEntity, int position) {
        AsyncTask.execute(() -> {
            appDatabase.commentDao().deleteComment(commentEntity);
            commentEntities.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return commentEntities.size();
    }

    class CommentsHolder extends RecyclerView.ViewHolder {

        private TextView txtName, txtDate, txtComment;
        private ImageButton btnDelete;

        public CommentsHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtCommentName);
            txtDate = itemView.findViewById(R.id.txtCommentDate);
            txtComment = itemView.findViewById(R.id.txtCommentText);
            btnDelete = itemView.findViewById(R.id.btnDeleteComment);
        }
    }
}

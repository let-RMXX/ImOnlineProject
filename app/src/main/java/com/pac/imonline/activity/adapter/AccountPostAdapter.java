package com.pac.imonline.activity.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pac.imonline.activity.Models.Posts;
import com.pac.imonline.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AccountPostAdapter extends RecyclerView.Adapter<AccountPostAdapter.AccountPostHolder> {

    private Context context;
    private ArrayList<Posts> arrayList;

    public AccountPostAdapter(Context context, ArrayList<Posts> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public AccountPostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_account_post, parent, false);
        return new AccountPostHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountPostHolder holder, int position) {
        Picasso.get().load(arrayList.get(position).getPhoto()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class AccountPostHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public AccountPostHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgAccountPost);
        }
    }
}

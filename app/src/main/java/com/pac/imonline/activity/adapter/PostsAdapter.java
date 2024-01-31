package com.pac.imonline.activity.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pac.imonline.R;
import com.pac.imonline.activity.Constant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Models.Posts;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

import com.pac.imonline.activity.Constant;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsHolder> {

    private Context context;
    private ArrayList<Posts> list;
    private ArrayList<Posts> listAll;

    private Retrofit retrofit;
    private ApiService apiService;

    public PostsAdapter(Context context, ArrayList<Posts> list) {
        this.context = context;
        this.list = list;
        this.listAll = new ArrayList<>(list);

        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        // Call method to fetch data
        fetchData();
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
                    if (post.getDesc().toLowerCase().contains(constraint.toString().toLowerCase())
                            || post.getUser().getUserName().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
            list.addAll((ArrayList<Posts>) results.values);
            notifyDataSetChanged();
        }
    };

    public Filter getFilter() {
        return filter;
    }

    private void fetchData() {
        Call<ArrayList<Posts>> call = apiService.getPosts();
        call.enqueue(new Callback<ArrayList<Posts>>() {
            @Override
            public void onResponse(Call<ArrayList<Posts>> call, Response<ArrayList<Posts>> response) {
                if (response.isSuccessful()) {
                    list.addAll(response.body());
                    listAll.addAll(response.body());
                    notifyDataSetChanged();
                } else {
                    // Handle error
                    Log.e("PostsAdapter", "API call failed");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Posts>> call, Throwable t) {
                // Handle failure
                Log.e("PostsAdapter", "API call failed", t);
            }
        });
    }

    public interface ApiService {
        // Api endpoint
        @GET(Constant.POSTS)
        Call<ArrayList<Posts>> getPosts();
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
            btnLike = itemView.findViewById(R.id.btnPostLike);
            btnComment = itemView.findViewById(R.id.btnPostComment);
        }
    }
}

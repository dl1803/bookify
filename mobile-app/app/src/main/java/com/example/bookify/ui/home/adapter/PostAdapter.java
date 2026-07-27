package com.example.bookify.ui.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookify.R;
import com.example.bookify.data.model.PostModel;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<PostModel> postList;

    public PostAdapter(List<PostModel> postList) {
        this.postList = postList;
    }

    public void updatePosts(List<PostModel> newPosts) {
        this.postList = newPosts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostModel post = postList.get(position);
        holder.tvUserName.setText(post.getUserName());
        holder.tvPostTime.setText(post.getPostTime());
        holder.tvPostContent.setText(post.getPostContent());
        holder.tvBookTitle.setText(post.getBookTitle());
        holder.tvBookAuthor.setText(post.getBookAuthor());
        holder.imgAvatar.setImageResource(post.getUserAvatarResId());
        holder.imgBookCover.setImageResource(post.getBookCoverResId());
        holder.tvLikesCount.setText(String.valueOf(post.getLikesCount()));
        holder.tvCommentsCount.setText(String.valueOf(post.getCommentsCount()));
    }

    @Override
    public int getItemCount() {
        return postList != null ? postList.size() : 0;
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar, imgBookCover;
        TextView tvUserName, tvPostTime, tvPostContent, tvBookTitle, tvBookAuthor, tvLikesCount, tvCommentsCount;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            imgBookCover = itemView.findViewById(R.id.imgBookCover);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvPostTime = itemView.findViewById(R.id.tvPostTime);
            tvPostContent = itemView.findViewById(R.id.tvPostContent);
            tvBookTitle = itemView.findViewById(R.id.tvBookTitle);
            tvBookAuthor = itemView.findViewById(R.id.tvBookAuthor);
            tvLikesCount = itemView.findViewById(R.id.tvLikesCount);
            tvCommentsCount = itemView.findViewById(R.id.tvCommentsCount);
        }
    }
}

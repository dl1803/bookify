package com.example.bookify.ui.home.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookify.R;
import com.example.bookify.data.model.PostModel;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    public interface OnPostInteractionListener {
        void onLikeClick(int position, View likeImageView);
        void onCommentClick(int position);
        void onShareClick(int position);
        void onMoreClick(int position, View anchorView);
        void onBookClick(int position);
    }

    private List<PostModel> postList;
    private OnPostInteractionListener listener;

    public PostAdapter(List<PostModel> postList) {
        this.postList = postList;
    }

    public void setOnPostInteractionListener(OnPostInteractionListener listener) {
        this.listener = listener;
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

        int redColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.like_red);
        if (post.isLiked()) {
            holder.imgLike.setColorFilter(redColor);
            holder.tvLikesCount.setTextColor(redColor);
        } else {
            holder.imgLike.setColorFilter(Color.parseColor("#555555"));
            holder.tvLikesCount.setTextColor(Color.parseColor("#555555"));
        }

        holder.btnLike.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION && listener != null) {
                animateHeartPop(holder.imgLike);
                listener.onLikeClick(pos, holder.imgLike);
            }
        });

        holder.btnComment.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION && listener != null) {
                listener.onCommentClick(pos);
            }
        });

        holder.btnShare.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION && listener != null) {
                listener.onShareClick(pos);
            }
        });

        holder.btnMoreOptions.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION && listener != null) {
                listener.onMoreClick(pos, holder.btnMoreOptions);
            }
        });

        holder.layoutBookInfo.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION && listener != null) {
                listener.onBookClick(pos);
            }
        });
    }

    private void animateHeartPop(View target) {
        ObjectAnimator scaleXUp = ObjectAnimator.ofFloat(target, "scaleX", 1.0f, 1.35f);
        ObjectAnimator scaleYUp = ObjectAnimator.ofFloat(target, "scaleY", 1.0f, 1.35f);
        scaleXUp.setDuration(150);
        scaleYUp.setDuration(150);

        ObjectAnimator scaleXDown = ObjectAnimator.ofFloat(target, "scaleX", 1.35f, 1.0f);
        ObjectAnimator scaleYDown = ObjectAnimator.ofFloat(target, "scaleY", 1.35f, 1.0f);
        scaleXDown.setDuration(150);
        scaleYDown.setDuration(150);

        AnimatorSet scaleUpSet = new AnimatorSet();
        scaleUpSet.playTogether(scaleXUp, scaleYUp);

        AnimatorSet scaleDownSet = new AnimatorSet();
        scaleDownSet.playTogether(scaleXDown, scaleYDown);

        AnimatorSet fullAnimation = new AnimatorSet();
        fullAnimation.playSequentially(scaleUpSet, scaleDownSet);
        fullAnimation.start();
    }

    @Override
    public int getItemCount() {
        return postList != null ? postList.size() : 0;
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar, imgBookCover, imgLike, btnMoreOptions, btnShare;
        View btnLike, btnComment, layoutBookInfo;
        TextView tvUserName, tvPostTime, tvPostContent, tvBookTitle, tvBookAuthor, tvLikesCount, tvCommentsCount;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            imgBookCover = itemView.findViewById(R.id.imgBookCover);
            imgLike = itemView.findViewById(R.id.imgLike);
            btnLike = itemView.findViewById(R.id.btnLike);
            btnComment = itemView.findViewById(R.id.btnComment);
            btnShare = itemView.findViewById(R.id.btnShare);
            btnMoreOptions = itemView.findViewById(R.id.btnMoreOptions);
            layoutBookInfo = itemView.findViewById(R.id.layoutBookInfo);
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

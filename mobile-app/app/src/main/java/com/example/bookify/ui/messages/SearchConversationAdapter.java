package com.example.bookify.ui.messages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookify.R;
import com.example.bookify.data.model.ActiveFriend;

import java.util.ArrayList;
import java.util.List;

public class SearchConversationAdapter extends RecyclerView.Adapter<SearchConversationAdapter.ViewHolder> {

    private List<ActiveFriend> friends = new ArrayList<>();
    private final OnFriendClickListener listener;

    public interface OnFriendClickListener {
        void onFriendClick(ActiveFriend friend);
    }

    public SearchConversationAdapter(OnFriendClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<ActiveFriend> newFriends) {
        friends.clear();
        if (newFriends != null) {
            friends.addAll(newFriends);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friend_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ActiveFriend friend = friends.get(position);
        holder.bind(friend, listener);
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivAvatar;
        private final TextView tvName;
        private final View vOnlineStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            tvName = itemView.findViewById(R.id.tvName);
            vOnlineStatus = itemView.findViewById(R.id.vOnlineStatus);
        }

        public void bind(ActiveFriend friend, OnFriendClickListener listener) {
            tvName.setText(friend.getName());
            
            if (friend.getAvatarUrl() != null && !friend.getAvatarUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(friend.getAvatarUrl())
                        .placeholder(R.drawable.ic_menu_avatar_placeholder)
                        .error(R.drawable.ic_menu_avatar_placeholder)
                        .into(ivAvatar);
            } else {
                ivAvatar.setImageResource(R.drawable.ic_menu_avatar_placeholder);
            }

            vOnlineStatus.setVisibility(friend.isOnline() ? View.VISIBLE : View.GONE);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onFriendClick(friend);
                }
            });
        }
    }
}

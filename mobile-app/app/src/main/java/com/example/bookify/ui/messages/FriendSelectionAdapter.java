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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java.util.ArrayList;
import java.util.List;

public class FriendSelectionAdapter extends RecyclerView.Adapter<FriendSelectionAdapter.ViewHolder> {

    private final List<ActiveFriend> friends = new ArrayList<>();
    private final Set<String> selectedFriendIds = new HashSet<>();
    private final OnSelectionChangedListener listener;

    public interface OnSelectionChangedListener {
        void onSelectionChanged(Set<String> selectedIds);
    }

    public FriendSelectionAdapter(OnSelectionChangedListener listener) {
        this.listener = listener;
    }

    public void submitList(List<ActiveFriend> newFriends) {
        friends.clear();
        if (newFriends != null) {
            friends.addAll(newFriends);
        }
        notifyDataSetChanged();
    }

    public Set<String> getSelectedIds() {
        return selectedFriendIds;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friend_selection, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ActiveFriend friend = friends.get(position);
        boolean isSelected = selectedFriendIds.contains(friend.getId());
        holder.bind(friend, isSelected, listener, selectedFriendIds, this);
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivAvatar;
        private final TextView tvName;
        private final View vOnlineStatus;
        private final View flCheckContainer;
        private final ImageView ivCheck;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            tvName = itemView.findViewById(R.id.tvName);
            vOnlineStatus = itemView.findViewById(R.id.vOnlineStatus);
            flCheckContainer = itemView.findViewById(R.id.flCheckContainer);
            ivCheck = itemView.findViewById(R.id.ivCheck);
        }

        public void bind(ActiveFriend friend, boolean isSelected, OnSelectionChangedListener listener, Set<String> selectedFriendIds, FriendSelectionAdapter adapter) {
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
            
            if (isSelected) {
                flCheckContainer.setBackgroundResource(R.drawable.bg_circle_primary);
                ivCheck.setVisibility(View.VISIBLE);
            } else {
                flCheckContainer.setBackgroundResource(R.drawable.bg_circle_outline);
                ivCheck.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(v -> {
                int position = adapter.getAdapterPosition(this); // Safe position lookup
                if (position != RecyclerView.NO_POSITION) {
                    if (selectedFriendIds.contains(friend.getId())) {
                        selectedFriendIds.remove(friend.getId());
                    } else {
                        selectedFriendIds.add(friend.getId());
                    }
                    adapter.notifyItemChanged(position);
                    if (listener != null) {
                        listener.onSelectionChanged(selectedFriendIds);
                    }
                }
            });
        }
    }
    
    // Helper to get safe position
    private int getAdapterPosition(ViewHolder holder) {
        return holder.getAdapterPosition();
    }
}

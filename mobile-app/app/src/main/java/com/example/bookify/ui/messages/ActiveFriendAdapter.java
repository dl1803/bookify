package com.example.bookify.ui.messages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookify.R;
import com.example.bookify.data.model.ActiveFriend;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class ActiveFriendAdapter extends RecyclerView.Adapter<ActiveFriendAdapter.ViewHolder> {

    private final List<ActiveFriend> friendList;

    public ActiveFriendAdapter(List<ActiveFriend> friendList) {
        this.friendList = friendList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_active_friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ActiveFriend friend = friendList.get(position);
        holder.bind(friend);
    }

    @Override
    public int getItemCount() {
        return friendList == null ? 0 : friendList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView ivAvatar;
        TextView tvName;
        View vOnlineDot;
        View vAvatarRing;
        View vAvatarGap;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            tvName = itemView.findViewById(R.id.tvName);
            vOnlineDot = itemView.findViewById(R.id.vOnlineDot);
            vAvatarRing = itemView.findViewById(R.id.vAvatarRing);
            vAvatarGap = itemView.findViewById(R.id.vAvatarGap);
        }

        public void bind(ActiveFriend friend) {
            tvName.setText(friend.getName());
            
            Glide.with(itemView.getContext())
                .load(friend.getAvatarUrl())
                .centerCrop()
                .into(ivAvatar);

            if (friend.isOnline()) {
                vOnlineDot.setVisibility(View.VISIBLE);
                vAvatarRing.setVisibility(View.VISIBLE);
                vAvatarGap.setVisibility(View.VISIBLE);
            } else {
                vOnlineDot.setVisibility(View.GONE);
                vAvatarRing.setVisibility(View.INVISIBLE);
                vAvatarGap.setVisibility(View.INVISIBLE);
            }
        }
    }
}

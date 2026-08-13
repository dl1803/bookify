package com.example.bookify.ui.messages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.bookify.R;
import com.example.bookify.data.model.ChatMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ChatMessage> messages = new ArrayList<>();

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ChatMessage.TYPE_DATE:
                return new DateViewHolder(inflater.inflate(R.layout.item_chat_date, parent, false));
            case ChatMessage.TYPE_SENT_TEXT:
                return new SentTextViewHolder(inflater.inflate(R.layout.item_chat_sent_text, parent, false));
            case ChatMessage.TYPE_RECEIVED_TEXT:
                return new ReceivedTextViewHolder(inflater.inflate(R.layout.item_chat_received_text, parent, false));
            case ChatMessage.TYPE_RECEIVED_IMAGE:
                return new ReceivedImageViewHolder(inflater.inflate(R.layout.item_chat_received_image, parent, false));
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        switch (holder.getItemViewType()) {
            case ChatMessage.TYPE_DATE:
                ((DateViewHolder) holder).bind(message);
                break;
            case ChatMessage.TYPE_SENT_TEXT:
                ((SentTextViewHolder) holder).bind(message);
                break;
            case ChatMessage.TYPE_RECEIVED_TEXT:
                ((ReceivedTextViewHolder) holder).bind(message);
                break;
            case ChatMessage.TYPE_RECEIVED_IMAGE:
                ((ReceivedImageViewHolder) holder).bind(message);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class DateViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate;
        DateViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
        void bind(ChatMessage message) {
            tvDate.setText(message.getDateLabel());
        }
    }

    static class SentTextViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage, tvTime;
        ImageView ivAvatar;
        SentTextViewHolder(View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
        }
        void bind(ChatMessage message) {
            tvMessage.setText(message.getText());
            tvTime.setText(message.getTime());
            Glide.with(itemView.getContext())
                    .load("https://i.pravatar.cc/150?img=12") // Placeholder for current user avatar
                    .placeholder(R.drawable.ic_person)
                    .into(ivAvatar);
        }
    }

    static class ReceivedTextViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage, tvTime;
        ImageView ivAvatar;
        ReceivedTextViewHolder(View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
        }
        void bind(ChatMessage message) {
            tvMessage.setText(message.getText());
            tvTime.setText(message.getTime());
            if (message.getAvatarUrl() != null && !message.getAvatarUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(message.getAvatarUrl())
                        .placeholder(R.drawable.ic_person)
                        .into(ivAvatar);
            }
        }
    }

    static class ReceivedImageViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar, ivAttachment;
        ReceivedImageViewHolder(View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            ivAttachment = itemView.findViewById(R.id.ivAttachment);
        }
        void bind(ChatMessage message) {
            if (message.getAvatarUrl() != null && !message.getAvatarUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(message.getAvatarUrl())
                        .placeholder(R.drawable.ic_person)
                        .into(ivAvatar);
            }
            if (message.getImageUrl() != null && !message.getImageUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(message.getImageUrl())
                        .transform(new CenterCrop())
                        .into(ivAttachment);
            } else {
                Glide.with(itemView.getContext())
                        .load(R.drawable.book_cover_2)
                        .transform(new CenterCrop())
                        .into(ivAttachment);
            }
        }
    }
}

package com.example.bookify.ui.alerts.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookify.R;
import com.example.bookify.data.model.NotificationModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private final List<NotificationModel> notificationList;
    private OnNotificationClickListener clickListener;

    public interface OnNotificationClickListener {
        void onNotificationClick(NotificationModel item, int position);
        void onNotificationLongClick(NotificationModel item, int position);
    }

    public NotificationAdapter(List<NotificationModel> notificationList) {
        this.notificationList = notificationList;
    }

    public void setOnNotificationClickListener(OnNotificationClickListener listener) {
        this.clickListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (notificationList.get(position).getType() == NotificationModel.NotificationType.HEADER) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_card, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NotificationModel model = notificationList.get(position);

        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.tvSectionTitle.setText(model.getSectionTitle());
        } else if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemHolder = (ItemViewHolder) holder;
            Context context = itemHolder.itemView.getContext();

            if (model.getAvatarResId() != 0) {
                itemHolder.imgAvatar.setImageResource(model.getAvatarResId());
            } else {
                itemHolder.imgAvatar.setImageResource(R.drawable.profile_avatar);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                itemHolder.tvContent.setText(Html.fromHtml(model.getContentText(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                itemHolder.tvContent.setText(Html.fromHtml(model.getContentText()));
            }

            itemHolder.tvTimeAgo.setText(model.getTimeAgo());

            if (model.isUnread()) {
                itemHolder.viewUnreadDot.setVisibility(View.VISIBLE);
                itemHolder.cardContainer.setCardBackgroundColor(Color.parseColor("#FFF7F5"));
            } else {
                itemHolder.viewUnreadDot.setVisibility(View.GONE);
                itemHolder.cardContainer.setCardBackgroundColor(Color.WHITE);
            }

            if (model.getType() == NotificationModel.NotificationType.FOLLOW) {
                itemHolder.btnFollowAction.setVisibility(View.VISIBLE);
                if (model.isFollowing()) {
                    itemHolder.btnFollowAction.setText("Following ✓");
                } else {
                    itemHolder.btnFollowAction.setText("Follow Back");
                }

                itemHolder.btnFollowAction.setOnClickListener(v -> {
                    int curPos = itemHolder.getAdapterPosition();
                    if (curPos == RecyclerView.NO_POSITION) return;
                    triggerHaptic(context);
                    NotificationModel curModel = notificationList.get(curPos);
                    curModel.setFollowing(!curModel.isFollowing());
                    notifyItemChanged(curPos);
                    String msg = curModel.isFollowing() ? "Now following " + curModel.getUserName() : "Unfollowed " + curModel.getUserName();
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                });
            } else {
                itemHolder.btnFollowAction.setVisibility(View.GONE);
            }

            itemHolder.cardContainer.setOnClickListener(v -> {
                int curPos = itemHolder.getAdapterPosition();
                if (curPos == RecyclerView.NO_POSITION) return;
                triggerHaptic(context);
                NotificationModel curModel = notificationList.get(curPos);
                if (curModel.isUnread()) {
                    curModel.setUnread(false);
                    notifyItemChanged(curPos);
                }
                if (clickListener != null) {
                    clickListener.onNotificationClick(curModel, curPos);
                }
            });

            itemHolder.cardContainer.setOnLongClickListener(v -> {
                int curPos = itemHolder.getAdapterPosition();
                if (curPos == RecyclerView.NO_POSITION) return false;
                triggerHaptic(context);
                NotificationModel curModel = notificationList.get(curPos);
                if (clickListener != null) {
                    clickListener.onNotificationLongClick(curModel, curPos);
                } else {
                    Toast.makeText(context, "Notification Options: Marked as read", Toast.LENGTH_SHORT).show();
                }
                return true;
            });
        }
    }

    @Override
    public int getItemCount() {
        return notificationList != null ? notificationList.size() : 0;
    }

    private void triggerHaptic(Context context) {
        try {
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null && vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(35, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(35);
                }
            }
        } catch (Exception ignored) {}
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvSectionTitle;

        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSectionTitle = itemView.findViewById(R.id.tvSectionTitle);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardContainer;
        View viewUnreadDot;
        ImageView imgAvatar;
        TextView tvContent;
        TextView tvTimeAgo;
        MaterialButton btnFollowAction;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            cardContainer = itemView.findViewById(R.id.cardNotificationItem);
            viewUnreadDot = itemView.findViewById(R.id.viewUnreadDot);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvTimeAgo = itemView.findViewById(R.id.tvTimeAgo);
            btnFollowAction = itemView.findViewById(R.id.btnFollowAction);
        }
    }
}

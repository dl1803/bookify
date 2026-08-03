package com.example.bookify.utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;

import com.example.bookify.R;
import com.example.bookify.data.model.PostModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;

public class BookifyDialogHelper {

    public interface OnProfileUpdateListener {
        void onProfileUpdated(String newFirstName, String newLastName, String newDob, String newCity, String newBio);
    }

    public interface OnBookSelectedListener {
        void onBookSelected(String title, String author, int coverResId);
    }

    public static void showBookDetailBottomSheet(Context context, String title, String author, int coverResId, String rating) {
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_book_detail_bottom_sheet, null);
        dialog.setContentView(view);

        ImageView imgCover = view.findViewById(R.id.dialogImgBookCover);
        TextView tvTitle = view.findViewById(R.id.dialogTvBookTitle);
        TextView tvAuthor = view.findViewById(R.id.dialogTvBookAuthor);
        TextView tvRating = view.findViewById(R.id.dialogTvRating);
        MaterialButton btnSaveBook = view.findViewById(R.id.dialogBtnSaveBook);
        MaterialButton btnShareBook = view.findViewById(R.id.dialogBtnShareBook);

        imgCover.setImageResource(coverResId);
        tvTitle.setText(title);
        tvAuthor.setText(author);
        tvRating.setText(rating != null ? rating.replace("⭐", "").trim() : "4.8");

        btnSaveBook.setOnClickListener(v -> {
            btnSaveBook.setText("Saved ✓");
            btnSaveBook.setEnabled(false);
            Toast.makeText(context, "Added '" + title + "' to your Saved Books library!", Toast.LENGTH_SHORT).show();
        });

        btnShareBook.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out this amazing book on Bookify: '" + title + "' by " + author);
            sendIntent.setType("text/plain");
            context.startActivity(Intent.createChooser(sendIntent, "Share Book via"));
            dialog.dismiss();
        });

        dialog.show();
    }

    public static void showPostMoreOptionsBottomSheet(Context context, PostModel post) {
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_more_options_bottom_sheet, null);
        dialog.setContentView(view);

        TextView tvTitle = view.findViewById(R.id.dialogTvMoreHeaderTitle);
        TextView tvSubtitle = view.findViewById(R.id.dialogTvMoreHeaderSubtitle);
        tvTitle.setText("Post Options");
        tvSubtitle.setText("Post by " + post.getUserName());

        view.findViewById(R.id.optionItemSave).setOnClickListener(v -> {
            Toast.makeText(context, "Saved post to your library!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        view.findViewById(R.id.optionItemShare).setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out " + post.getUserName() + "'s post on Bookify: \"" + post.getPostContent() + "\"");
            sendIntent.setType("text/plain");
            context.startActivity(Intent.createChooser(sendIntent, "Share Post via"));
            dialog.dismiss();
        });

        view.findViewById(R.id.optionItemAction).setOnClickListener(v -> {
            Toast.makeText(context, "Viewing " + post.getUserName() + "'s profile", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        view.findViewById(R.id.optionItemReport).setOnClickListener(v -> {
            Toast.makeText(context, "Post hidden from feed", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }

    public static void showProfileMoreOptionsBottomSheet(Context context, Runnable onEditProfile, Runnable onShareProfile) {
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_more_options_bottom_sheet, null);
        dialog.setContentView(view);

        TextView tvTitle = view.findViewById(R.id.dialogTvMoreHeaderTitle);
        TextView tvSubtitle = view.findViewById(R.id.dialogTvMoreHeaderSubtitle);
        tvTitle.setText("Profile Options");
        tvSubtitle.setText("Manage your profile settings and sharing");

        TextView tvTitle1 = view.findViewById(R.id.tvOptionTitle1);
        TextView tvSub1 = view.findViewById(R.id.tvOptionSubtitle1);
        ImageView imgIcon1 = view.findViewById(R.id.imgOptionIcon1);
        tvTitle1.setText("Edit Profile");
        tvSub1.setText("Update name, avatar, and bio");
        imgIcon1.setImageResource(R.drawable.ic_profile);

        TextView tvTitle2 = view.findViewById(R.id.tvOptionTitle2);
        TextView tvSub2 = view.findViewById(R.id.tvOptionSubtitle2);
        tvTitle2.setText("Share Profile Link");
        tvSub2.setText("Send your profile link to friends");

        view.findViewById(R.id.optionItemAction).setVisibility(View.GONE);
        view.findViewById(R.id.optionItemReport).setVisibility(View.GONE);

        view.findViewById(R.id.optionItemSave).setOnClickListener(v -> {
            dialog.dismiss();
            if (onEditProfile != null) onEditProfile.run();
        });

        view.findViewById(R.id.optionItemShare).setOnClickListener(v -> {
            dialog.dismiss();
            if (onShareProfile != null) onShareProfile.run();
        });

        dialog.show();
    }

    public static void showCommentsBottomSheet(Context context, PostModel post) {
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_comments_bottom_sheet, null);
        dialog.setContentView(view);

        TextView tvHeader = view.findViewById(R.id.dialogTvCommentsHeader);
        EditText etCommentInput = view.findViewById(R.id.dialogEtCommentInput);
        ImageView btnSendComment = view.findViewById(R.id.dialogBtnSendComment);
        LinearLayout layoutCommentsList = view.findViewById(R.id.layoutCommentsList);
        NestedScrollView scrollComments = view.findViewById(R.id.dialogScrollComments);

        final int[] commentsCount = {post.getCommentsCount()};
        tvHeader.setText("Comments (" + commentsCount[0] + ")");

        btnSendComment.setOnClickListener(v -> {
            String commentText = etCommentInput.getText().toString().trim();
            if (commentText.isEmpty()) {
                Toast.makeText(context, "Please enter a comment!", Toast.LENGTH_SHORT).show();
                return;
            }

            View commentLineView = LayoutInflater.from(context).inflate(R.layout.item_comment_line, layoutCommentsList, false);
            TextView tvAuthor = commentLineView.findViewById(R.id.tvCommentAuthor);
            TextView tvTime = commentLineView.findViewById(R.id.tvCommentTime);
            TextView tvText = commentLineView.findViewById(R.id.tvCommentText);

            tvAuthor.setText("Emily Reader");
            tvTime.setText("Just now");
            tvText.setText(commentText);

            layoutCommentsList.addView(commentLineView);
            commentsCount[0]++;
            tvHeader.setText("Comments (" + commentsCount[0] + ")");
            etCommentInput.setText("");

            if (scrollComments != null) {
                scrollComments.post(() -> scrollComments.fullScroll(View.FOCUS_DOWN));
            }
        });

        dialog.show();
    }

    public static void showEditProfileDialog(Context context, com.example.bookify.data.model.UserModel currentUser, String currentBio, OnProfileUpdateListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_profile, null);
        builder.setView(view);

        EditText etUsername = view.findViewById(R.id.dialogEtUsername);
        EditText etEmail = view.findViewById(R.id.dialogEtEmail);
        EditText etFirstName = view.findViewById(R.id.dialogEtFirstName);
        EditText etLastName = view.findViewById(R.id.dialogEtLastName);
        EditText etDob = view.findViewById(R.id.dialogEtDob);
        EditText etCity = view.findViewById(R.id.dialogEtCity);
        EditText etBio = view.findViewById(R.id.dialogEtBio);
        MaterialButton btnSave = view.findViewById(R.id.dialogBtnSaveProfile);
        MaterialButton btnCancel = view.findViewById(R.id.dialogBtnCancelProfile);

        if (currentUser != null) {
            etUsername.setText(currentUser.getUsername());
            etEmail.setText(currentUser.getEmail());
            etFirstName.setText(currentUser.getFirstName());
            etLastName.setText(currentUser.getLastName());
            etDob.setText(currentUser.getDob());
            etCity.setText(currentUser.getCity());
        }
        etBio.setText(currentBio);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        btnSave.setOnClickListener(v -> {
            String newFirstName = etFirstName.getText().toString().trim();
            String newLastName = etLastName.getText().toString().trim();
            String newDob = etDob.getText().toString().trim();
            String newCity = etCity.getText().toString().trim();
            String newBio = etBio.getText().toString().trim();

            if (newFirstName.isEmpty()) {
                Toast.makeText(context, "First Name cannot be empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (listener != null) {
                listener.onProfileUpdated(newFirstName, newLastName, newDob, newCity, newBio);
            }
            Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    public static void showAttachBookDialog(Context context, OnBookSelectedListener listener) {
        String[] bookTitles = {"The Silent Echo", "Roots of Thought", "Currents", "Lost Time"};
        String[] bookAuthors = {"Arthur Pendelton", "Elena Vance", "Marcus Thorne", "Sarah Jenkins"};
        int[] coverResIds = {R.drawable.book_cover_1, R.drawable.book_cover_2, R.drawable.book_cover_3, R.drawable.book_cover_4};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select a Book to Attach");
        builder.setItems(bookTitles, (dialog, which) -> {
            if (listener != null) {
                listener.onBookSelected(bookTitles[which], bookAuthors[which], coverResIds[which]);
            }
            Toast.makeText(context, "Attached '" + bookTitles[which] + "' to your post!", Toast.LENGTH_SHORT).show();
        });
        builder.show();
    }
}

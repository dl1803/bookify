package com.example.bookify.ui.messages;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Window;

import com.bumptech.glide.Glide;
import com.example.bookify.R;
import com.example.bookify.data.model.ChatMessage;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView rvMessages;
    private ChatAdapter chatAdapter;
    private ImageButton btnBack, btnCall, btnMenu, btnAddAttachment, btnEmoji, btnSend;
    private EditText etMessage;
    private TextView tvHeaderName;
    private ShapeableImageView ivHeaderAvatar;

    // Optional Intent keys
    public static final String EXTRA_USER_NAME = "extra_user_name";
    public static final String EXTRA_USER_AVATAR = "extra_user_avatar";

    public static Intent newIntent(Context context, String userName, String avatarUrl) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EXTRA_USER_NAME, userName);
        intent.putExtra(EXTRA_USER_AVATAR, avatarUrl);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupEdgeToEdge();
        setContentView(R.layout.activity_chat);

        initViews();
        setupListeners();
        setupRecyclerView();
        loadDummyData();
    }

    private void setupEdgeToEdge() {
        Window window = getWindow();
        androidx.core.view.WindowCompat.setDecorFitsSystemWindows(window, false);
        window.setStatusBarColor(android.graphics.Color.TRANSPARENT);
        window.setNavigationBarColor(android.graphics.Color.TRANSPARENT);
        window.addFlags(android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    }

    private void initViews() {
        rvMessages = findViewById(R.id.rvMessages);
        btnBack = findViewById(R.id.btnBack);
        btnCall = findViewById(R.id.btnCall);
        btnMenu = findViewById(R.id.btnMenu);
        btnAddAttachment = findViewById(R.id.btnAddAttachment);
        btnEmoji = findViewById(R.id.btnEmoji);
        btnSend = findViewById(R.id.btnSend);
        etMessage = findViewById(R.id.etMessage);
        tvHeaderName = findViewById(R.id.tvHeaderName);
        ivHeaderAvatar = findViewById(R.id.ivHeaderAvatar);

        View layoutHeaderBar = findViewById(R.id.layoutHeaderBar);
        View layoutInputBar = findViewById(R.id.layoutInputBar);
        
        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            androidx.core.graphics.Insets systemBars = insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars());
            
            // Apply top inset to header
            layoutHeaderBar.setPadding(
                layoutHeaderBar.getPaddingLeft(),
                systemBars.top,
                layoutHeaderBar.getPaddingRight(),
                layoutHeaderBar.getPaddingBottom()
            );
            
            // Apply bottom inset to footer
            layoutInputBar.setPadding(
                layoutInputBar.getPaddingLeft(),
                layoutInputBar.getPaddingTop(),
                layoutInputBar.getPaddingRight(),
                systemBars.bottom + (int)(12 * getResources().getDisplayMetrics().density) // 12dp original padding
            );
            
            return androidx.core.view.WindowInsetsCompat.CONSUMED;
        });

        String name = getIntent().getStringExtra(EXTRA_USER_NAME);
        if (name != null) tvHeaderName.setText(name);
        String avatar = getIntent().getStringExtra(EXTRA_USER_AVATAR);
        if (avatar != null && !avatar.isEmpty()) {
            Glide.with(this).load(avatar).placeholder(R.drawable.ic_person).into(ivHeaderAvatar);
        }
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
        
        btnCall.setOnClickListener(v -> Toast.makeText(this, "Calling " + tvHeaderName.getText(), Toast.LENGTH_SHORT).show());
        btnMenu.setOnClickListener(v -> Toast.makeText(this, "Menu coming soon", Toast.LENGTH_SHORT).show());
        
        btnSend.setOnClickListener(v -> {
            String text = etMessage.getText().toString().trim();
            if (!text.isEmpty()) {
                ChatMessage newMsg = ChatMessage.createSentText(
                        String.valueOf(System.currentTimeMillis()),
                        text,
                        "Just now"
                );
                // add to adapter
                // clear text
                etMessage.setText("");
            }
        });

        // Add keyboard visibility listener to scroll to bottom
        rvMessages.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (bottom < oldBottom) {
                rvMessages.postDelayed(() -> {
                    if (chatAdapter.getItemCount() > 0) {
                        rvMessages.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
                    }
                }, 100);
            }
        });
    }

    private void setupRecyclerView() {
        chatAdapter = new ChatAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true); // Optional: starts from bottom
        rvMessages.setLayoutManager(layoutManager);
        rvMessages.setAdapter(chatAdapter);
    }

    private void loadDummyData() {
        List<ChatMessage> dummy = new ArrayList<>();
        String avatarUrl = getIntent().getStringExtra(EXTRA_USER_AVATAR); // use provided avatar or null
        
        dummy.add(ChatMessage.createDate("1", "Today"));
        dummy.add(ChatMessage.createReceivedText("2", "Hey! Did you finish reading the book I recommended?", "10:14 AM", avatarUrl));
        dummy.add(ChatMessage.createSentText("3", "Yes! I loved the plot twist. Should we discuss it in the group?", "10:16 AM"));
        dummy.add(ChatMessage.createReceivedText("4", "Definitely. By the way, check out this cover I found for the sequel:", "10:18 AM", avatarUrl));
        // Add image message
        dummy.add(ChatMessage.createReceivedImage("5", "", "10:18 AM", avatarUrl));

        chatAdapter.setMessages(dummy);
        rvMessages.scrollToPosition(dummy.size() - 1);
    }
}

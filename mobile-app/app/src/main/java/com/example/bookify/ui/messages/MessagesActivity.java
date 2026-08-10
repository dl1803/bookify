package com.example.bookify.ui.messages;

import android.graphics.Color;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bookify.R;
import com.example.bookify.data.model.ActiveFriend;
import com.example.bookify.data.model.MessageThread;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessagesActivity extends AppCompatActivity {

    private androidx.recyclerview.widget.RecyclerView rvActiveFriends;
    private androidx.recyclerview.widget.RecyclerView rvMessages;
    private com.google.android.material.appbar.MaterialToolbar toolbar;
    private android.widget.ImageButton fabNewMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        toolbar = findViewById(R.id.toolbar);
        rvActiveFriends = findViewById(R.id.rvActiveFriends);
        rvMessages = findViewById(R.id.rvMessages);
        fabNewMessage = findViewById(R.id.fabNewMessage);

        setupEdgeToEdge();
        setupToolbar();
        setupRecyclerViews();
        setupFab();
    }

    private void setupEdgeToEdge() {
        Window window = getWindow();
        WindowCompat.setDecorFitsSystemWindows(window, false);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.setNavigationBarColor(Color.TRANSPARENT);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    }

    private void setupToolbar() {
        toolbar.setNavigationOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            onBackPressed();
        });
    }

    private void setupRecyclerViews() {
        // Active Friends (Mock Data - 2 items)
        List<ActiveFriend> activeFriends = new ArrayList<>();
        activeFriends.add(new ActiveFriend("1", "Emma", "https://randomuser.me/api/portraits/women/44.jpg", true));
        activeFriends.add(new ActiveFriend("2", "Liam", "https://randomuser.me/api/portraits/men/32.jpg", false));

        ActiveFriendAdapter activeFriendAdapter = new ActiveFriendAdapter(activeFriends);
        rvActiveFriends.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvActiveFriends.setAdapter(activeFriendAdapter);

        // Message Threads (Mock Data - 2 items: 1 single, 1 group)
        List<MessageThread> messageThreads = new ArrayList<>();
        messageThreads.add(new MessageThread(
                "t1", 
                "Emma", 
                "Did you finish chapter 4? I can't believe what happened!", 
                "2m ago", 
                1, 
                false, 
                Arrays.asList("https://randomuser.me/api/portraits/women/44.jpg")
        ));
        
        messageThreads.add(new MessageThread(
                "t2", 
                "Books Group", 
                "Emma: Hey guys, are we still meeting tonight?", 
                "1h ago", 
                0, 
                true, 
                Arrays.asList(
                    "https://randomuser.me/api/portraits/men/32.jpg", 
                    "https://randomuser.me/api/portraits/women/44.jpg", 
                    "https://randomuser.me/api/portraits/women/68.jpg"
                )
        ));

        MessageThreadAdapter threadAdapter = new MessageThreadAdapter(messageThreads);
        rvMessages.setLayoutManager(new LinearLayoutManager(this));
        rvMessages.setAdapter(threadAdapter);
    }

    private void setupFab() {
        fabNewMessage.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            Toast.makeText(this, getString(R.string.msg_developing), Toast.LENGTH_SHORT).show();
        });
    }
}

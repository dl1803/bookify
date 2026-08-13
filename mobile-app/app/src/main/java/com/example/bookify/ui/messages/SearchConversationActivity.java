package com.example.bookify.ui.messages;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.Window;
import android.view.WindowManager;

import com.example.bookify.R;
import com.example.bookify.data.model.ActiveFriend;

import java.util.ArrayList;
import java.util.List;

public class SearchConversationActivity extends AppCompatActivity {

    private EditText etSearch;
    private ImageButton btnBack;
    private RecyclerView rvFriends;
    private TextView tvSectionLabel;
    
    private SearchConversationAdapter adapter;
    private List<ActiveFriend> allFriends;
    private boolean isSearching = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_conversation);

        setupEdgeToEdge();
        initViews();
        setupRecyclerView();
        loadDummyData();
        setupListeners();

        // Request focus and show keyboard
        etSearch.requestFocus();
        etSearch.postDelayed(() -> {
            android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(etSearch, android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT);
            }
        }, 100);
    }

    private void setupEdgeToEdge() {
        Window window = getWindow();
        WindowCompat.setDecorFitsSystemWindows(window, false);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.setNavigationBarColor(Color.TRANSPARENT);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    }

    private void initViews() {
        etSearch = findViewById(R.id.etSearch);
        btnBack = findViewById(R.id.btnBack);
        rvFriends = findViewById(R.id.rvFriends);
        tvSectionLabel = findViewById(R.id.tvSectionLabel);
    }

    private void setupRecyclerView() {
        adapter = new SearchConversationAdapter(friend -> {
            android.content.Intent intent = ChatActivity.newIntent(this, friend.getName(), friend.getAvatarUrl());
            startActivity(intent);
            finish();
        });
        rvFriends.setLayoutManager(new LinearLayoutManager(this));
        rvFriends.setAdapter(adapter);
    }

    private void loadDummyData() {
        allFriends = new ArrayList<>();
        allFriends.add(new ActiveFriend("1", "Emma", "https://i.pravatar.cc/150?img=1", true));
        allFriends.add(new ActiveFriend("2", "Liam", "https://i.pravatar.cc/150?img=11", false));
        allFriends.add(new ActiveFriend("3", "Olivia", "https://i.pravatar.cc/150?img=5", true));
        allFriends.add(new ActiveFriend("4", "Noah", "https://i.pravatar.cc/150?img=12", false));
        allFriends.add(new ActiveFriend("5", "Ava", "https://i.pravatar.cc/150?img=9", true));

        // Default: Show active friends
        updateList(allFriends, false);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().toLowerCase().trim();
                if (query.isEmpty()) {
                    isSearching = false;
                    updateList(allFriends, false);
                } else {
                    isSearching = true;
                    List<ActiveFriend> filtered = new ArrayList<>();
                    for (ActiveFriend friend : allFriends) {
                        if (friend.getName().toLowerCase().contains(query)) {
                            filtered.add(friend);
                        }
                    }
                    updateList(filtered, true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void updateList(List<ActiveFriend> friends, boolean searching) {
        if (searching) {
            tvSectionLabel.setText(R.string.label_suggested);
        } else {
            tvSectionLabel.setText(R.string.label_active_friends_search);
        }
        adapter.submitList(friends);
    }
}

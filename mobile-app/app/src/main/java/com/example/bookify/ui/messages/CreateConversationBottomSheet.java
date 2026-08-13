package com.example.bookify.ui.messages;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookify.R;
import com.example.bookify.data.model.ActiveFriend;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CreateConversationBottomSheet extends BottomSheetDialogFragment {

    private EditText etSearch;
    private RecyclerView rvFriends;
    private FrameLayout flActionContainer;
    private MaterialButton btnStartChat;

    private FriendSelectionAdapter adapter;
    private List<ActiveFriend> allFriends;

    public static CreateConversationBottomSheet newInstance() {
        return new CreateConversationBottomSheet();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        
        // Ensure the bottom sheet background is transparent so we can see our custom rounded corners
        dialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog d = (BottomSheetDialog) dialogInterface;
            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                bottomSheet.setBackgroundResource(android.R.color.transparent);
                BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setSkipCollapsed(true);
            }
        });
        
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_create_conversation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupRecyclerView();
        loadDummyData();
        setupListeners();
    }

    private void initViews(View view) {
        etSearch = view.findViewById(R.id.etSearch);
        rvFriends = view.findViewById(R.id.rvFriends);
        flActionContainer = view.findViewById(R.id.flActionContainer);
        btnStartChat = view.findViewById(R.id.btnStartChat);
    }

    private void setupRecyclerView() {
        adapter = new FriendSelectionAdapter(selectedIds -> {
            updateActionButton(selectedIds);
        });
        rvFriends.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvFriends.setAdapter(adapter);
    }

    private void loadDummyData() {
        allFriends = new ArrayList<>();
        allFriends.add(new ActiveFriend("1", "Emma", "https://i.pravatar.cc/150?img=1", true));
        allFriends.add(new ActiveFriend("2", "Liam", "https://i.pravatar.cc/150?img=11", false));
        allFriends.add(new ActiveFriend("3", "Olivia", "https://i.pravatar.cc/150?img=5", true));
        allFriends.add(new ActiveFriend("4", "Noah", "https://i.pravatar.cc/150?img=12", false));
        allFriends.add(new ActiveFriend("5", "Ava", "https://i.pravatar.cc/150?img=9", true));

        updateList(allFriends);
    }

    private void setupListeners() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().toLowerCase().trim();
                if (query.isEmpty()) {
                    updateList(allFriends);
                } else {
                    List<ActiveFriend> filtered = new ArrayList<>();
                    for (ActiveFriend friend : allFriends) {
                        if (friend.getName().toLowerCase().contains(query)) {
                            filtered.add(friend);
                        }
                    }
                    updateList(filtered);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        
        btnStartChat.setOnClickListener(v -> {
            Set<String> selectedIds = adapter.getSelectedIds();
            if (!selectedIds.isEmpty()) {
                String firstId = selectedIds.iterator().next();
                ActiveFriend selectedFriend = null;
                for (ActiveFriend friend : allFriends) {
                    if (friend.getId().equals(firstId)) {
                        selectedFriend = friend;
                        break;
                    }
                }
                if (selectedFriend != null) {
                    android.content.Intent intent = ChatActivity.newIntent(requireContext(), selectedFriend.getName(), selectedFriend.getAvatarUrl());
                    startActivity(intent);
                }
            }
            dismiss();
        });
    }

    private void updateList(List<ActiveFriend> friends) {
        adapter.submitList(friends);
    }

    private void updateActionButton(Set<String> selectedIds) {
        int count = selectedIds.size();
        if (count == 0) {
            flActionContainer.setVisibility(View.GONE);
        } else if (count == 1) {
            flActionContainer.setVisibility(View.VISIBLE);
            btnStartChat.setText("Start Chat");
        } else {
            flActionContainer.setVisibility(View.VISIBLE);
            btnStartChat.setText("Start a group chat (" + count + ")");
        }
    }
}

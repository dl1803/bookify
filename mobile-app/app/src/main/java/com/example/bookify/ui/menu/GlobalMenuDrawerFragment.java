package com.example.bookify.ui.menu;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.bookify.R;

public class GlobalMenuDrawerFragment extends DialogFragment {

    public static GlobalMenuDrawerFragment newInstance() {
        return new GlobalMenuDrawerFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Apply Translucent NoTitleBar style to allow edge-to-edge span vertically and proper dimming
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_global_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Setup click listeners for each menu item
        setupClickListeners(view);
    }

    private void setupClickListeners(View view) {
        view.findViewById(R.id.btnMenuMessages).setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            showToast(getString(R.string.menu_message));
            dismiss();
        });

        view.findViewById(R.id.btnMenuFriends).setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            showToast(getString(R.string.menu_friends));
            dismiss();
        });

        view.findViewById(R.id.btnMenuGroups).setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            showToast(getString(R.string.menu_groups));
            dismiss();
        });

        view.findViewById(R.id.btnMenuSettings).setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            showToast(getString(R.string.menu_account_settings));
            dismiss();
        });

        view.findViewById(R.id.btnMenuLogout).setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            if (getActivity() != null) {
                com.example.bookify.data.repository.AuthRepository.getInstance().logout();
                showToast(getString(R.string.menu_logout) + " successful");
                android.content.Intent intent = new android.content.Intent(getActivity(), com.example.bookify.ui.auth.LoginActivity.class);
                intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK | android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            }
            dismiss();
        });
        
        // Close menu if user taps on the empty space of the header or root
        view.setOnClickListener(v -> {
            dismiss();
        });
    }

    private void showToast(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.setCanceledOnTouchOutside(true); // Close when touching outside
            
            Window window = dialog.getWindow();
            
            // Background transparent for the dialog window itself to remove black borders
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            
            // Enable Dim behind
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setDimAmount(0.6f); // 60% opacity dim for better contrast
            
            // Draw edge-to-edge and fix black status/navigation bars
            androidx.core.view.WindowCompat.setDecorFitsSystemWindows(window, false);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);

            // Set position to Left, height match_parent, width wrap_content (280dp as defined in xml)
            window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            window.setGravity(Gravity.START | Gravity.TOP);
            
            // Custom sliding animation
            window.setWindowAnimations(R.style.DialogAnimationSlideLeft);
        }
    }
}

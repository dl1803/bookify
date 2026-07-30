package com.example.bookify.ui.profile;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;

import com.example.bookify.R;
import com.example.bookify.ui.home.HomeActivity;
import com.example.bookify.utils.BookifyDialogHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class ProfileActivity extends AppCompatActivity {

    private ProfileViewModel viewModel;
    private MaterialButton btnFollow;
    private NestedScrollView scrollViewContent;
    private TextView tvProfileName;
    private TextView tvProfileBio;
    private View layoutBooksGrid;
    private ShapeableImageView imgAvatar;

    private ActivityResultLauncher<String> avatarPickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_profile_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        scrollViewContent = findViewById(R.id.scroll_view_content);
        btnFollow = findViewById(R.id.btn_follow);
        tvProfileName = findViewById(R.id.tv_profile_name);
        tvProfileBio = findViewById(R.id.tv_profile_bio);
        layoutBooksGrid = findViewById(R.id.layout_books_grid);
        imgAvatar = findViewById(R.id.img_avatar);

        setupAvatarPicker();
        setupViewModel();
        setupProfileInteractions();
        setupTabs();
        setupBookCards();
        setupBottomNavigationAndFab();
    }

    private void setupAvatarPicker() {
        avatarPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null && imgAvatar != null) {
                        imgAvatar.setImageURI(uri);
                        Toast.makeText(this, "Avatar updated successfully!", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        // Observe follow status
        viewModel.getIsFollowing().observe(this, isFollowing -> {
            if (btnFollow != null) {
                if (isFollowing) {
                    btnFollow.setText("Following ✓");
                    btnFollow.setBackgroundTintList(ColorStateList.valueOf(
                            ContextCompat.getColor(this, R.color.surface_container)
                    ));
                    btnFollow.setTextColor(ContextCompat.getColor(this, R.color.brown_primary));
                } else {
                    btnFollow.setText("Follow");
                    btnFollow.setBackgroundTintList(ColorStateList.valueOf(
                            ContextCompat.getColor(this, R.color.brown_primary)
                    ));
                    btnFollow.setTextColor(ContextCompat.getColor(this, R.color.white));
                }
            }
        });
    }

    private void setupProfileInteractions() {
        // Avatar Click -> Launch Avatar Image Picker
        if (imgAvatar != null) {
            imgAvatar.setOnClickListener(v -> {
                try {
                    avatarPickerLauncher.launch("image/*");
                } catch (Exception e) {
                    BookifyDialogHelper.showEditProfileDialog(
                            this,
                            tvProfileName.getText().toString(),
                            tvProfileBio.getText().toString(),
                            (newName, newBio) -> {
                                tvProfileName.setText(newName);
                                tvProfileBio.setText(newBio);
                            }
                    );
                }
            });
        }

        // Follow Button Click
        if (btnFollow != null) {
            btnFollow.setOnClickListener(v -> {
                viewModel.toggleFollowStatus();
                Boolean isFollowing = viewModel.getIsFollowing().getValue();
                if (isFollowing != null && isFollowing) {
                    Toast.makeText(this, "You are now following Emily Reader!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Unfollowed Emily Reader", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // More Options Button with Modern Bottom Sheet
        findViewById(R.id.btn_more).setOnClickListener(v -> 
            BookifyDialogHelper.showProfileMoreOptionsBottomSheet(
                    this,
                    () -> BookifyDialogHelper.showEditProfileDialog(
                            this,
                            tvProfileName.getText().toString(),
                            tvProfileBio.getText().toString(),
                            (newName, newBio) -> {
                                tvProfileName.setText(newName);
                                tvProfileBio.setText(newBio);
                            }
                    ),
                    () -> {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out Emily Reader's profile on Bookify!");
                        sendIntent.setType("text/plain");
                        startActivity(Intent.createChooser(sendIntent, "Share Profile via"));
                    }
            )
        );

        // Stats Click
        findViewById(R.id.layout_stats).setOnClickListener(v -> 
            Toast.makeText(this, "Followers: 1.2k | Following: 350", Toast.LENGTH_SHORT).show()
        );
    }

    private void setupTabs() {
        TabLayout tabLayout = findViewById(R.id.tab_layout_profile);
        if (tabLayout != null) {
            // Select 3rd tab "Saved Books" as default
            TabLayout.Tab savedBooksTab = tabLayout.getTabAt(2);
            if (savedBooksTab != null) {
                savedBooksTab.select();
            }

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    int position = tab.getPosition();
                    viewModel.selectTab(position);
                    if (layoutBooksGrid != null) {
                        layoutBooksGrid.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {}

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    if (scrollViewContent != null) {
                        scrollViewContent.smoothScrollTo(0, 0);
                    }
                }
            });
        }
    }

    private void setupBookCards() {
        findViewById(R.id.card_book_1).setOnClickListener(v -> 
            BookifyDialogHelper.showBookDetailBottomSheet(this, "The Silent Echo", "Arthur Pendelton", R.drawable.book_cover_1, "4.8")
        );

        findViewById(R.id.card_book_2).setOnClickListener(v -> 
            BookifyDialogHelper.showBookDetailBottomSheet(this, "Roots of Thought", "Elena Vance", R.drawable.book_cover_2, "4.5")
        );

        findViewById(R.id.card_book_3).setOnClickListener(v -> 
            BookifyDialogHelper.showBookDetailBottomSheet(this, "Currents", "Marcus Thorne", R.drawable.book_cover_3, "4.9")
        );

        findViewById(R.id.card_book_4).setOnClickListener(v -> 
            BookifyDialogHelper.showBookDetailBottomSheet(this, "Lost Time", "Sarah Jenkins", R.drawable.book_cover_4, "4.2")
        );

        findViewById(R.id.card_add_new).setOnClickListener(v -> 
            BookifyDialogHelper.showAttachBookDialog(this, (title, author, coverResId) -> {
                Toast.makeText(this, "Added '" + title + "' to your Saved Books!", Toast.LENGTH_SHORT).show();
            })
        );
    }

    private void setupBottomNavigationAndFab() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(R.id.nav_profile);
            bottomNav.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    if (scrollViewContent != null) {
                        scrollViewContent.smoothScrollTo(0, 0);
                    }
                    return true;
                } else if (itemId == R.id.nav_explore) {
                    Toast.makeText(this, "Explore feature coming soon!", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (itemId == R.id.nav_alerts) {
                    Intent intent = new Intent(ProfileActivity.this, com.example.bookify.ui.alerts.AlertsActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                    return true;
                }
                return false;
            });
        }

        FloatingActionButton fabCreate = findViewById(R.id.fabCreate);
        if (fabCreate != null) {
            fabCreate.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            });
        }
    }
}

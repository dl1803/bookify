package com.example.bookify.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.bookify.R;
import com.example.bookify.ui.home.HomeActivity;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private ImageButton btnTogglePassword;
    private Button btnLogin;
    private LinearLayout btnGoogleLogin;
    private TextView tvForgotPassword;
    private TextView tvGoToRegister;

    private AuthViewModel authViewModel;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        initViewModel();
        setupListeners();
        checkRegisteredIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        checkRegisteredIntent(intent);
    }

    private void checkRegisteredIntent(Intent intent) {
        if (intent != null && intent.hasExtra("REGISTERED_USERNAME")) {
            String regUser = intent.getStringExtra("REGISTERED_USERNAME");
            if (regUser != null && !regUser.isEmpty()) {
                etUsername.setText(regUser);
                etPassword.setText("");
                etPassword.requestFocus();
            }
        }
    }

    private void initViews() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnTogglePassword = findViewById(R.id.btnTogglePassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoogleLogin = findViewById(R.id.btnGoogleLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvGoToRegister = findViewById(R.id.tvGoToRegister);

        etUsername.setText("admin");
        etPassword.setText("123456");
    }

    private void initViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        authViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                btnLogin.setEnabled(false);
                btnLogin.setText("Signing in...");
            } else {
                btnLogin.setEnabled(true);
                btnLogin.setText("Sign In");
            }
        });

        authViewModel.getErrorMessage().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                triggerHapticFeedback();
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });

        authViewModel.getAuthSuccessUser().observe(this, user -> {
            if (user != null) {
                triggerHapticFeedback();
                Toast.makeText(this, "Welcome " + user.getFullName() + "!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setupListeners() {
        etUsername.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                clearFieldError(etUsername);
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                clearFieldError(etPassword);
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        btnTogglePassword.setOnClickListener(v -> {
            isPasswordVisible = !isPasswordVisible;
            if (isPasswordVisible) {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                btnTogglePassword.setImageResource(R.drawable.ic_visibility_off);
            } else {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                btnTogglePassword.setImageResource(R.drawable.ic_visibility);
            }
            etPassword.setSelection(etPassword.getText().length());
        });

        btnLogin.setOnClickListener(v -> {
            clearFieldError(etUsername);
            clearFieldError(etPassword);

            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty()) {
                showFieldError(etUsername, "Username cannot be empty");
                return;
            }
            if (username.length() < 4) {
                showFieldError(etUsername, "Username must be at least 4 characters");
                return;
            }

            if (password.isEmpty()) {
                showFieldError(etPassword, "Password cannot be empty");
                return;
            }
            if (password.length() < 6) {
                showFieldError(etPassword, "Password must be at least 6 characters");
                return;
            }

            triggerHapticFeedback();
            authViewModel.login(username, password);
        });

        btnGoogleLogin.setOnClickListener(v -> {
            triggerHapticFeedback();
            Toast.makeText(this, "Connecting to Google Sign-In...", Toast.LENGTH_SHORT).show();
            authViewModel.login("google_user", "123456");
        });

        tvForgotPassword.setOnClickListener(v -> {
            Toast.makeText(this, "Forgot password feature will be available soon", Toast.LENGTH_SHORT).show();
        });

        tvGoToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }

    private void showFieldError(EditText field, String message) {
        field.setError(message);
        if (field.getParent() instanceof View) {
            ((View) field.getParent()).setBackgroundResource(R.drawable.bg_input_field_error);
        }
        field.requestFocus();
        triggerHapticFeedback();
    }

    private void clearFieldError(EditText field) {
        field.setError(null);
        if (field.getParent() instanceof View) {
            ((View) field.getParent()).setBackgroundResource(R.drawable.bg_input_field);
        }
    }

    private void triggerHapticFeedback() {
        try {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            if (vibrator != null && vibrator.hasVibrator()) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(30);
                }
            }
        } catch (Exception ignored) {
        }
    }
}

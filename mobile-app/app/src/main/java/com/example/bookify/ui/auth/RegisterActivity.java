package com.example.bookify.ui.auth;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.bookify.R;

import java.util.Calendar;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etDob;
    private EditText etCity;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private ImageButton btnTogglePassword;
    private ImageButton btnToggleConfirmPassword;
    private CheckBox cbTerms;
    private Button btnRegister;
    private TextView tvGoToLogin;

    private AuthViewModel authViewModel;
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        initViewModel();
        setupListeners();
    }

    private void initViews() {
        etUsername = findViewById(R.id.etUsername);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etDob = findViewById(R.id.etDob);
        etCity = findViewById(R.id.etCity);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnTogglePassword = findViewById(R.id.btnTogglePassword);
        btnToggleConfirmPassword = findViewById(R.id.btnToggleConfirmPassword);
        cbTerms = findViewById(R.id.cbTerms);
        btnRegister = findViewById(R.id.btnRegister);
        tvGoToLogin = findViewById(R.id.tvGoToLogin);
    }

    private void initViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        authViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                btnRegister.setEnabled(false);
                btnRegister.setText("Creating account...");
            } else {
                btnRegister.setEnabled(true);
                btnRegister.setText("Register");
            }
        });

        authViewModel.getErrorMessage().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                triggerHapticFeedback();
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });

        authViewModel.getRegisterSuccess().observe(this, isSuccess -> {
            if (Boolean.TRUE.equals(isSuccess)) {
                triggerHapticFeedback();
                Toast.makeText(this, "Registration successful! Please sign in.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.putExtra("REGISTERED_USERNAME", etUsername.getText().toString().trim());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setupListeners() {
        // Clear error text & red border when user edits field
        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                clearAllFieldErrors();
            }
            @Override public void afterTextChanged(Editable s) {}
        };

        etUsername.addTextChangedListener(watcher);
        etFirstName.addTextChangedListener(watcher);
        etLastName.addTextChangedListener(watcher);
        etDob.addTextChangedListener(watcher);
        etCity.addTextChangedListener(watcher);
        etPassword.addTextChangedListener(watcher);
        etConfirmPassword.addTextChangedListener(watcher);

        // DOB Date Picker
        etDob.setOnClickListener(v -> showDatePicker());

        // Password visibility toggle
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

        // Confirm Password visibility toggle
        btnToggleConfirmPassword.setOnClickListener(v -> {
            isConfirmPasswordVisible = !isConfirmPasswordVisible;
            if (isConfirmPasswordVisible) {
                etConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                btnToggleConfirmPassword.setImageResource(R.drawable.ic_visibility_off);
            } else {
                etConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                btnToggleConfirmPassword.setImageResource(R.drawable.ic_visibility);
            }
            etConfirmPassword.setSelection(etConfirmPassword.getText().length());
        });

        // Submit Register
        btnRegister.setOnClickListener(v -> {
            clearAllFieldErrors();

            String username = etUsername.getText().toString().trim();
            String firstName = etFirstName.getText().toString().trim();
            String lastName = etLastName.getText().toString().trim();
            String dob = etDob.getText().toString().trim();
            String city = etCity.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();
            boolean termsAccepted = cbTerms.isChecked();

            // 1. Username Validation (@Size(min = 4))
            if (username.isEmpty()) {
                showFieldError(etUsername, "Username cannot be empty");
                return;
            }
            if (username.length() < 4) {
                showFieldError(etUsername, "Username must be at least 4 characters");
                return;
            }

            // 2. First Name Validation
            if (firstName.isEmpty()) {
                showFieldError(etFirstName, "First name cannot be empty");
                return;
            }

            // 3. Last Name Validation
            if (lastName.isEmpty()) {
                showFieldError(etLastName, "Last name cannot be empty");
                return;
            }

            // 4. DOB Validation (@DobConstraint(min = 18))
            if (dob.isEmpty()) {
                showFieldError(etDob, "Please select date of birth");
                return;
            }
            if (!isAgeAtLeast18(dob)) {
                showFieldError(etDob, "You must be 18 years or older");
                return;
            }

            // 5. City Validation
            if (city.isEmpty()) {
                showFieldError(etCity, "City cannot be empty");
                return;
            }

            // 6. Password Validation (@Size(min = 6))
            if (password.isEmpty()) {
                showFieldError(etPassword, "Password cannot be empty");
                return;
            }
            if (password.length() < 6) {
                showFieldError(etPassword, "Password must be at least 6 characters");
                return;
            }

            // 7. Confirm Password Validation
            if (confirmPassword.isEmpty()) {
                showFieldError(etConfirmPassword, "Confirm password cannot be empty");
                return;
            }
            if (!password.equals(confirmPassword)) {
                showFieldError(etConfirmPassword, "Passwords do not match");
                return;
            }

            // 8. Terms Checkbox Validation
            if (!termsAccepted) {
                triggerHapticFeedback();
                Toast.makeText(this, "You must agree to the Terms of Service and Privacy Policy", Toast.LENGTH_SHORT).show();
                cbTerms.requestFocus();
                return;
            }

            triggerHapticFeedback();
            authViewModel.register(username, firstName, lastName, dob, city, password, confirmPassword, termsAccepted);
        });

        // Go to Login Screen
        tvGoToLogin.setOnClickListener(v -> {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }

    private void showDatePicker() {
        clearFieldError(etDob);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR) - 20; // default 20 years ago
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String dateStr = String.format(Locale.getDefault(), "%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                    etDob.setText(dateStr);
                    clearFieldError(etDob);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private boolean isAgeAtLeast18(String dobStr) {
        if (dobStr == null || dobStr.trim().isEmpty()) return false;
        try {
            String[] parts = dobStr.split("/");
            if (parts.length != 3) return false;
            int day = Integer.parseInt(parts[0].trim());
            int month = Integer.parseInt(parts[1].trim()) - 1;
            int year = Integer.parseInt(parts[2].trim());

            Calendar today = Calendar.getInstance();
            Calendar dob = Calendar.getInstance();
            dob.set(year, month, day);

            int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
            if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }
            return age >= 18;
        } catch (Exception e) {
            return false;
        }
    }

    private void showFieldError(EditText field, String message) {
        field.setError(message);
        if (field.getParent() instanceof View && ((View) field.getParent()).getId() != R.id.cardContainer) {
            View parent = (View) field.getParent();
            if (parent.getBackground() != null) {
                parent.setBackgroundResource(R.drawable.bg_input_field_error);
            } else {
                field.setBackgroundResource(R.drawable.bg_input_field_error);
            }
        } else {
            field.setBackgroundResource(R.drawable.bg_input_field_error);
        }
        field.requestFocus();
        triggerHapticFeedback();
    }

    private void clearFieldError(EditText field) {
        field.setError(null);
        field.setBackgroundResource(R.drawable.bg_input_field);
        if (field.getParent() instanceof View && ((View) field.getParent()).getId() != R.id.cardContainer) {
            View parent = (View) field.getParent();
            if (parent.getBackground() != null) {
                parent.setBackgroundResource(R.drawable.bg_input_field);
            }
        }
    }

    private void clearAllFieldErrors() {
        clearFieldError(etUsername);
        clearFieldError(etFirstName);
        clearFieldError(etLastName);
        clearFieldError(etDob);
        clearFieldError(etCity);
        clearFieldError(etPassword);
        clearFieldError(etConfirmPassword);
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

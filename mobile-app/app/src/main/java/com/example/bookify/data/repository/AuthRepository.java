package com.example.bookify.data.repository;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bookify.data.model.UserModel;

public class AuthRepository {

    private static AuthRepository instance;
    private final MutableLiveData<UserModel> currentUserLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> authLoadingState = new MutableLiveData<>(false);
    private final MutableLiveData<String> authErrorState = new MutableLiveData<>(null);

    private AuthRepository() {
        // No session stored initially
    }

    public static synchronized AuthRepository getInstance() {
        if (instance == null) {
            instance = new AuthRepository();
        }
        return instance;
    }

    public LiveData<UserModel> getCurrentUser() {
        return currentUserLiveData;
    }

    public LiveData<Boolean> getAuthLoadingState() {
        return authLoadingState;
    }

    public LiveData<String> getAuthErrorState() {
        return authErrorState;
    }

    public interface AuthCallback {
        void onSuccess(UserModel user);
        void onError(String errorMessage);
    }

    public void login(String username, String password, AuthCallback callback) {
        authLoadingState.setValue(true);
        authErrorState.setValue(null);

        // Simulate network delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            authLoadingState.setValue(false);
            if (username == null || username.trim().isEmpty()) {
                String err = "Tên đăng nhập không được để trống";
                authErrorState.setValue(err);
                if (callback != null) callback.onError(err);
                return;
            }
            if (password == null || password.length() < 4) {
                String err = "Mật khẩu phải có ít nhất 4 ký tự";
                authErrorState.setValue(err);
                if (callback != null) callback.onError(err);
                return;
            }

            // Success mock user on login
            UserModel user = new UserModel(
                    username,
                    "Emily",
                    "Reader",
                    "15/08/1998",
                    "Hà Nội"
            );
            currentUserLiveData.setValue(user);
            if (callback != null) callback.onSuccess(user);
        }, 1000);
    }

    public void register(UserModel userModel, String password, AuthCallback callback) {
        authLoadingState.setValue(true);
        authErrorState.setValue(null);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            authLoadingState.setValue(false);
            if (userModel.getUsername() == null || userModel.getUsername().trim().isEmpty()) {
                String err = "Tên người dùng không được để trống";
                authErrorState.setValue(err);
                if (callback != null) callback.onError(err);
                return;
            }
            if (password == null || password.length() < 4) {
                String err = "Mật khẩu phải có ít nhất 4 ký tự";
                authErrorState.setValue(err);
                if (callback != null) callback.onError(err);
                return;
            }

            // Do not save registered user to session memory
            if (callback != null) callback.onSuccess(userModel);
        }, 1200);
    }

    public void logout() {
        currentUserLiveData.setValue(null);
    }
}

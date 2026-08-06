package com.example.bookify.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bookify.data.model.UserModel;
import com.example.bookify.data.remote.api.IdentityApiService;
import com.example.bookify.data.remote.dto.ApiResponse;
import com.example.bookify.data.remote.dto.UserCreationRequest;
import com.example.bookify.data.remote.dto.UserResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Single;

@Singleton
public class AuthRepository {

    private final IdentityApiService identityApiService;
    private final MutableLiveData<UserModel> currentUserLiveData = new MutableLiveData<>();

    @Inject
    public AuthRepository(IdentityApiService identityApiService) {
        this.identityApiService = identityApiService;
    }

    public LiveData<UserModel> getCurrentUser() {
        return currentUserLiveData;
    }

    // Keep the mock login for now until we implement the real login API
    public Single<UserModel> login(String username, String password) {
        return Single.create(emitter -> {
            try {
                Thread.sleep(1000); // Simulate network delay
                if (username == null || username.trim().isEmpty()) {
                    emitter.onError(new Exception("Username cannot be empty"));
                    return;
                }
                if (password == null || password.length() < 4) {
                    emitter.onError(new Exception("Password must be at least 4 characters"));
                    return;
                }
                UserModel user = new UserModel(username, "emily@example.com", "Emily", "Reader", "15/08/1998", "Hanoi");
                currentUserLiveData.postValue(user);
                emitter.onSuccess(user);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    public Single<ApiResponse<UserResponse>> register(UserCreationRequest request) {
        return identityApiService.registerUser(request);
    }

    public void logout() {
        currentUserLiveData.setValue(null);
    }
}

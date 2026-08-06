package com.example.bookify.data.remote.api;

import com.example.bookify.data.remote.dto.ApiResponse;
import com.example.bookify.data.remote.dto.UserCreationRequest;
import com.example.bookify.data.remote.dto.UserResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IdentityApiService {
    @POST("/api/identity/users/registration")
    Single<ApiResponse<UserResponse>> registerUser(@Body UserCreationRequest request);
}

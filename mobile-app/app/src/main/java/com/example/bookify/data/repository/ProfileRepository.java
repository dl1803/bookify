package com.example.bookify.data.repository;

import com.example.bookify.data.remote.api.ProfileApiService;
import com.example.bookify.data.remote.dto.ApiResponse;
import com.example.bookify.data.remote.dto.UpdateProfileRequest;
import com.example.bookify.data.remote.dto.UserProfileResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Single;
import okhttp3.MultipartBody;

@Singleton
public class ProfileRepository {

    private final ProfileApiService profileApiService;

    @Inject
    public ProfileRepository(ProfileApiService profileApiService) {
        this.profileApiService = profileApiService;
    }

    public Single<ApiResponse<UserProfileResponse>> getMyProfile() {
        return profileApiService.getMyProfile();
    }

    public Single<ApiResponse<UserProfileResponse>> updateProfile(UpdateProfileRequest request) {
        return profileApiService.updateMyProfile(request);
    }

    public Single<ApiResponse<UserProfileResponse>> uploadAvatar(MultipartBody.Part file) {
        return profileApiService.updateAvatar(file);
    }
}

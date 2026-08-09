package com.dl1803.profile.service;

import java.util.List;

import com.dl1803.profile.dto.request.UpdateProfileRequest;
import com.dl1803.profile.repository.httpClient.FileClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dl1803.profile.dto.request.ProfileCreationRequest;
import com.dl1803.profile.dto.response.UserProfileResponse;
import com.dl1803.profile.entity.UserProfile;
import com.dl1803.profile.exception.AppException;
import com.dl1803.profile.exception.ErrorCode;
import com.dl1803.profile.mapper.UserProfileMapper;
import com.dl1803.profile.repository.UserProfileRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserProfileService {
    UserProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;
    FileClient fileClient;

    public UserProfileResponse createProfile(ProfileCreationRequest request) {
        UserProfile userProfile = userProfileMapper.toUserProfile(request);

        userProfile = userProfileRepository.save(userProfile);
        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    public UserProfileResponse getByUserId(String userId) {
        UserProfile userProfile = userProfileRepository
                .findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    public UserProfileResponse getProfile(String id) {
        UserProfile userProfile =
                userProfileRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserProfileResponse> getAllProfiles() {
        return userProfileMapper.toListUserProfileResponse(userProfileRepository.findAll());
    }

    public UserProfileResponse getMyProfile(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        var profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userProfileMapper.toUserProfileResponse(profile);
    }

    public UserProfileResponse updateMyProfile(UpdateProfileRequest request){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();;
        var  profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userProfileMapper.update(profile, request);
        return userProfileMapper.toUserProfileResponse(userProfileRepository.save(profile));
    }

    public UserProfileResponse updateAvatar(MultipartFile file){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();;
        var  profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Upload file - invoke file service api
        var response = fileClient.uploadMedia(file);

        profile.setAvatar(response.getResult().getUrl());

        return userProfileMapper.toUserProfileResponse(userProfileRepository.save(profile));
    }
}

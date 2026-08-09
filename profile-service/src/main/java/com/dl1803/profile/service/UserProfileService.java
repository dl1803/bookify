package com.dl1803.profile.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
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

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserProfileService {
    UserProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;

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
}

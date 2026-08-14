package com.dl1803.profile.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.dl1803.profile.dto.request.ProfileCreationRequest;
import com.dl1803.profile.dto.request.UpdateProfileRequest;
import com.dl1803.profile.dto.response.UserProfileResponse;
import com.dl1803.profile.entity.UserProfile;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toUserProfile(ProfileCreationRequest request);

    UserProfileResponse toUserProfileResponse(UserProfile entity);

    List<UserProfileResponse> toListUserProfileResponse(List<UserProfile> list);

    void update(@MappingTarget UserProfile entity, UpdateProfileRequest request);
}

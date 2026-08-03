package com.dl1803.profile.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.dl1803.profile.dto.request.ProfileCreationRequest;
import com.dl1803.profile.dto.response.UserProfileResponse;
import com.dl1803.profile.entity.UserProfile;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toUserProfile(ProfileCreationRequest request);

    UserProfileResponse toUserProfileResponse(UserProfile entity);

    List<UserProfileResponse> toListUserProfileResponse(List<UserProfile> list);
}

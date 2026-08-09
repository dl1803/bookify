package com.dl1803.identity.mapper;

import org.mapstruct.Mapper;

import com.dl1803.identity.dto.request.ProfileCreationRequest;
import com.dl1803.identity.dto.request.UserCreationRequest;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileCreationRequest toProfileCreationRequest(UserCreationRequest request);
}

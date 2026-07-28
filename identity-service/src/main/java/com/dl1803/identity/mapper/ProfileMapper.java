package com.dl1803.identity.mapper;

import com.dl1803.identity.dto.request.ProfileCreationRequest;
import com.dl1803.identity.dto.request.UserCreationRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileCreationRequest toProfileCreationRequest(UserCreationRequest request);
}

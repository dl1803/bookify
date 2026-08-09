package com.dl1803.identity.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.dl1803.identity.dto.request.UserCreationRequest;
import com.dl1803.identity.dto.request.UserUpdateRequest;
import com.dl1803.identity.dto.response.UserResponse;
import com.dl1803.identity.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    List<UserResponse> toUserResponseList(List<User> users);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}

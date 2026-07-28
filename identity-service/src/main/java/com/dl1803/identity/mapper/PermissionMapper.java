package com.dl1803.identity.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.dl1803.identity.dto.request.PermissionRequest;
import com.dl1803.identity.dto.response.PermissionResponse;
import com.dl1803.identity.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);

    List<PermissionResponse> toListPermissionResponse(List<Permission> permissions);
}

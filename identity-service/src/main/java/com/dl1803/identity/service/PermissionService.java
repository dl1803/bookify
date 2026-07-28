package com.dl1803.identity.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dl1803.identity.dto.request.PermissionRequest;
import com.dl1803.identity.dto.response.PermissionResponse;
import com.dl1803.identity.entity.Permission;
import com.dl1803.identity.mapper.PermissionMapper;
import com.dl1803.identity.repository.PermissionRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class PermissionService {
    PermissionRepository permissionRepository;

    PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll() {
        var permissions = permissionRepository.findAll();
        return permissionMapper.toListPermissionResponse(permissions);
    }

    public void delete(String permission) {
        permissionRepository.deleteById(permission);
    }
}

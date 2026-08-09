package com.dl1803.file.dto.response;

import org.springframework.core.io.Resource;

// support tạo data obj nhanh gọn thông qua constructor
// immutable(không thể thay đổi attributes)
public record FileData(String contentType, Resource resource) {}

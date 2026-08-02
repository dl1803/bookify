package com.dl1803.identity.repository.httpclient;

import com.dl1803.identity.configuration.AuthenticationRequestInterceptor;
import com.dl1803.identity.dto.request.ProfileCreationRequest;
import com.dl1803.identity.dto.response.ApiResponse;
import com.dl1803.identity.dto.response.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient (name = "profile-service", url = "${app.services.profile}", /*// name, root endpoint (phần chung của api profile service)*/
 configuration = {AuthenticationRequestInterceptor.class}) // cấu hình chỉ sử dụng interceptor ở những FeignClient cụ thể (không dùng cho all request)
public interface ProfileClient {
    @PostMapping(value = "/internal/users", produces = MediaType.APPLICATION_JSON_VALUE) // produces(RequestMapping) tạo ra 1 request với content-type là Json
    ApiResponse<UserProfileResponse> createProfile(
            @RequestBody ProfileCreationRequest request);
}

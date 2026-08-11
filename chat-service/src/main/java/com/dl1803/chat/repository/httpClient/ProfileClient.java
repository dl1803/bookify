package com.dl1803.chat.repository.httpClient;


import com.dl1803.chat.dto.response.ApiResponse;
import com.dl1803.chat.dto.response.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "profile-service", url = "${app.services.profile.url}")
public interface ProfileClient {
    @GetMapping("/internal/users/{userId}")
    ApiResponse<UserProfileResponse> getProfile(@PathVariable String userId);

    @GetMapping("/internal/users/bulk")
    ApiResponse<List<UserProfileResponse>> getUsersProfiles(@RequestParam("userIds") List<String> userIds);
}

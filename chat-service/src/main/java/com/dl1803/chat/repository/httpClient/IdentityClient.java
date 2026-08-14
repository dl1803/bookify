package com.dl1803.chat.repository.httpClient;


import com.dl1803.chat.dto.request.IntrospectRequest;
import com.dl1803.chat.dto.response.ApiResponse;
import com.dl1803.chat.dto.response.IntrospectResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient (name = "identity-service", url = "${app.services.identity.url}")
public interface IdentityClient {
    @PostMapping("/auth/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request);
}

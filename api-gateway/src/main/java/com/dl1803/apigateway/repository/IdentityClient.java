package com.dl1803.apigateway.repository;

import com.dl1803.apigateway.dto.request.IntrospectRequest;
import com.dl1803.apigateway.dto.response.ApiResponse;
import com.dl1803.apigateway.dto.response.IntrospectResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

public interface IdentityClient {
    // http interfaces của spring6
    @PostExchange(url = "/auth/introspect", contentType = MediaType.APPLICATION_JSON_VALUE)
    Mono<ApiResponse<IntrospectResponse>> introspect(@RequestBody IntrospectRequest request);
}

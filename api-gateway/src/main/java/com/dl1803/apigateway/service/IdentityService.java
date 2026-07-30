package com.dl1803.apigateway.service;

import com.dl1803.apigateway.dto.request.IntrospectRequest;
import com.dl1803.apigateway.dto.response.ApiResponse;
import com.dl1803.apigateway.dto.response.IntrospectResponse;
import com.dl1803.apigateway.repository.IdentityClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityService {
    IdentityClient identityClient;

    public Mono<ApiResponse<IntrospectResponse>> introspect(String token){
        return identityClient.introspect(IntrospectRequest.builder()
                        .token(token)
                .build());
    }

}

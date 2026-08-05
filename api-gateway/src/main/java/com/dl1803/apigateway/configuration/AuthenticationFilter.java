package com.dl1803.apigateway.configuration;

import com.dl1803.apigateway.dto.response.ApiResponse;
import com.dl1803.apigateway.service.IdentityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerResponse;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class AuthenticationFilter implements GlobalFilter, Ordered {
    // Interface GlobalFilter hỗ trợ filter cho all request đi qua gateway
    // Interface Ordered hỗ trợ quyết định thứ tự của các filter (chạy trước/sau so với các filter khác - số càng nhỏ thứ tự càng lớn)

    IdentityService identityService;

    ObjectMapper objectMapper;

    @NonFinal
    String[] publicEndpoints = {"/identity/auth/.*",
            "/identity/users/registration",
            "/notification/email/send",

            "/swagger-ui/.*",
            "/.*/swagger-ui/.*",
            "/.*/v3/api-docs.*"
    };

    @Value("${app.api-prefix}")
    @NonFinal
    private String apiPrefix;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange /*chứa all thông tin của request/response(url, header)*/, GatewayFilterChain chain /*đối tượng quản lí list filter còn lại*/) {
        log.info("Enter authentication filter....");

        if (isPublicEndPoint(exchange.getRequest())){
            return chain.filter(exchange);
        }

        //Lấy token từ authorization header
        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (CollectionUtils.isEmpty(authHeader)) return unthenticatied(exchange.getResponse());

        String token = authHeader.getFirst().replace("Bearer ", "");
        log.info("Token: {}", token);


        // Verify token delegate(ủy quyền) thông qua identity service(web client)
        // Do KDL yêu cầu trả về là Mono<void> -> dùng flagMap
        return identityService.introspect(token).flatMap(introspectResponseApiResponse -> {
            if (introspectResponseApiResponse.getResult().isValid()){
                return chain.filter(exchange);  // lấy data chuyển đi xuồng các filter phía dưới -> trả về Mono<void>
            } else {
                return unthenticatied(exchange.getResponse()); //  -> trả về Mono<void>
            }
        }).onErrorResume(throwable -> unthenticatied(exchange.getResponse()));
        // flagMap: lấy kết quả của method khi Mono hoàn thành -> sau cùng trả về 1 Mono mới
        // subcribe: tương tự flagMap nhưng sau cùng sẽ kết thúc(void)
        // onErrorResume: xử lí các lỗi khác nếu có

    }

    @Override
    public int getOrder() {
        return -1; // ưu tiên nhất (hiện tại các order của các filter của cloud gateway mặc định > 0)
    }


    private boolean isPublicEndPoint(ServerHttpRequest httpRequest){
        return Arrays.stream(publicEndpoints).anyMatch(s -> httpRequest.getURI().getPath().matches(apiPrefix + s));
    }


    // Mono<T> biểu diễn một tác vụ bất đồng bộ sẽ phát ra tối đa 1 giá trị(có thể k có kq) kiểu T trong tương lai.
    // Có thể hiểu như một "phiếu hẹn": khi tác vụ hoàn thành sẽ có kết quả.
    Mono<Void> unthenticatied (ServerHttpResponse response /*đại diện toàn bộ http res mà server trả về client,
    // chứa status, header, cookie và body của HTTP Response*/){

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(1401)
                .message("Unauthenticatied")
                .build();

        String body = null;
        try {
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);


        // body.getBytes() chuyển data String thành byte[] vì dữ liệu truyền trên HTTP cuối cùng đều được gửi dưới dạng byte
        // Spring WebFlux sử dụng DataBuffer để biểu diễn dữ liệu sẽ ghi vào HTTP Response.
        // response.bufferFactory() lấy ra DataBufferFactory dùng để tạo DataBuffer(1 vùng nhớ chứa dữ liệu sẽ gửi qua mạng)
        // wrap(byte[]) bọc mảng byte thành DataBuffer để WebFlux ghi ra Response
        // Mono.just(T) bọc một đối tượng T thành Mono<T>
        // Vì để ghi data vào Body response dùng writewith mà write chỉ nhận Publisher<DataBuffer> (hay Mono<DataBuffer> vì Mono là 1 publisher)
        // writeWith() ghi DataBuffer vào Body của HTTP Response và trả về Mono<Void>
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }
}

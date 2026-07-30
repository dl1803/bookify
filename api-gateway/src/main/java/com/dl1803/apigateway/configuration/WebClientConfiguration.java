package com.dl1803.apigateway.configuration;


import com.dl1803.apigateway.repository.IdentityClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebClientConfiguration {

// webclient: hỗ trợ gửi HTTP request non-blocking
    @Bean
    WebClient webClient(){
        return  WebClient.builder()
                .baseUrl("http://localhost:8080/identity")  // root endpoint
                .build();
    }

    // register bean cho identity client
    @Bean
    IdentityClient identityClient(WebClient webClient){
        // HttpServiceProxyFactory đối tượng tạo proxy (lớp ảo imple interface)
        // WebClientAdapter đổi tượng giúp chuyển đổi các request từ proxy thành các luồng xử lí tương thích webclient
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient)).build();

        return httpServiceProxyFactory.createClient(IdentityClient.class); // tạo bean
    }
}

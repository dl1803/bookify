package com.dl1803.identity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients // hỗ trợ gọi api giữa các microservice (blocking), Spring tự động quét các interface có @FeignClient và tạo bean trong container
public class IdentityServiceApplication {
   public static void main(String[] args) {
        SpringApplication.run(IdentityServiceApplication.class, args);
    }
}

package com.dl1803.profile.configuration;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// Spring cần inject bean obj SpringFormEncoder để FeignClient biết cách encode multipart/form-data khi gửi request
public class FeignConfiguration {
    @Bean
    public Encoder multipartFormEncoder(){
        return new SpringFormEncoder();
    }
}

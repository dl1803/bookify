package com.dl1803.identity.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Slf4j
public class AuthenticationRequestInterceptor implements RequestInterceptor {
    // RequestInterceptor : interface của Feign cho phép chỉnh sửa request trước khi fiegn gửi đi

    // Feign sẽ gọi apply() trước khi gửi request đi
    // RequestTemplate là obj đại diện cho request (chứa url, http method, header, body...)
    @Override
    public void apply(RequestTemplate template) {

        // RequestContextHolder : class hỗ trợ lấy request hiện tại kiểu RequestAttributes(kiểu tổng quát, k có hàm getRequest())
        // ServletRequestAttributes là 1 class imple RequestAttributes dành cho http servlet(nên cần ép kiểu), có thể getRequest() để lấy HttpServletRequest
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes)  RequestContextHolder.getRequestAttributes();

        var authHeader = servletRequestAttributes.getRequest().getHeader("Authorization");

        log.info("Header: {}", authHeader);

        if (StringUtils.hasText(authHeader)){
            template.header("Authorization", authHeader);
        }
    }
}

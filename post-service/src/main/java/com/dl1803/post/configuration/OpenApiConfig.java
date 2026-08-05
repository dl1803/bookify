package com.dl1803.post.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;


@Configuration

@OpenAPIDefinition(

        info = @Info(

                title = "Post API",

                version = "1.0",

                description = "REST API"
        ),
        servers = {
                @Server(url = "http://localhost:8888/api/post",
                        description = "Gateway Server")
        },
        security = {@SecurityRequirement(name = "bearerAuth")}
)


@SecurityScheme(

        name = "bearerAuth",

        type = SecuritySchemeType.HTTP,

        scheme = "bearer",

        bearerFormat = "JWT"
)

public class OpenApiConfig {

}
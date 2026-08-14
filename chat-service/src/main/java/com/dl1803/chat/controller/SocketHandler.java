package com.dl1803.chat.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.dl1803.chat.dto.request.IntrospectRequest;
import com.dl1803.chat.dto.response.IntrospectResponse;
import com.dl1803.chat.service.IdentityService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

//nhận message từ socket

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class SocketHandler {
    SocketIOServer server;
    IdentityService identityService;

    @OnConnect // khi 1 tbi connect vào port của server -> @ run -> cấp một mã đinh danh trong SocketIOClient
    // SocketIOClient là 1 obj đại diện cho client connect vào server (chứa các info: Id, handshakeData khi FE đính kèm(như query params, headers, cookies ,... ), ...)
    public void clientConnected(SocketIOClient client) {
        // Get token from request param FE
        String token = client.getHandshakeData().getSingleUrlParam("token");

        // Verify token
        var introspectResponse = identityService.introspect(
                IntrospectRequest.builder()
                        .token(token)
                        .build()
        );

        // If token is invalid -> disconnect
        if (introspectResponse.isValid()) {
            log.info("Client connected: {}", client.getSessionId());
        } else {
            log.error("Authentication failed: {}", client.getSessionId());
            client.disconnect(); // ngắt knoi tói socket và SocketIOServer sẽ goi @OnDisconnect
        }
    }

    @OnDisconnect // khi 1 tbi disconnect ...
    public void clientDisconnected(SocketIOClient client) {
        log.info("Client disconnected: " + client.getSessionId());
    }

    // start server khi SpringBoot run (run sau khi bean được init toàn bộ)
    @PostConstruct
    public void startServer() {
        server.start(); // mở ở port 8099
        server.addListeners(this); // gắn chính class SocketHandler -> lắng nghe events
        log.info("Socket server started");
    }


    // stop server khi application bị dừng
    @PreDestroy
    public void stopServer() {
        server.stop();
        log.info("Socket server stopped");
    }

}

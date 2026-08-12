package com.dl1803.chat.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

//nhận message từ socket

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class SocketHandler {
    SocketIOServer server;

    @OnConnect // khi 1 tbi connect vào port của server -> @ run -> cấp một mã đinh danh
    public void clientConnected(SocketIOClient client) {
        log.info("Client connected: " + client.getSessionId());
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

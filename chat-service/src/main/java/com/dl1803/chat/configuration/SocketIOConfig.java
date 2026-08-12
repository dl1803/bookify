package com.dl1803.chat.configuration;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration // tạo obj cho class và xử lí(inject) các method @Bean
public class SocketIOConfig {
    // config socketio server
    @Bean
    public SocketIOServer socketIOServer(){
        Configuration config = new Configuration();
        config.setPort(8099);
        config.setOrigin("*"); // cho phép connect từ bất kì đâu(app/web...) đến websocket server

        return new SocketIOServer(config);

    }

}

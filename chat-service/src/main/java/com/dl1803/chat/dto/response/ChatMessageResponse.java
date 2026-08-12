package com.dl1803.chat.dto.response;

import com.dl1803.chat.entity.ParticipantInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessageResponse {
    String id;
    String conversationId;
    boolean me; // target:
    // 1. dùng để FE biết vẽ UI message theo hướng nào
    // 2. giúp tối ưu getMessages
    String message;
    ParticipantInfo sender;
    Instant createdDate;
}

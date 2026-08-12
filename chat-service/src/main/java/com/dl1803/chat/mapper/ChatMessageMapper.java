package com.dl1803.chat.mapper;

import com.dl1803.chat.dto.request.ChatMessageRequest;
import com.dl1803.chat.dto.response.ChatMessageResponse;
import com.dl1803.chat.entity.ChatMessage;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper {
    ChatMessageResponse toChatMessageResponse(ChatMessage chatMessage);

    ChatMessage toChatMessage(ChatMessageRequest request);

    List<ChatMessageResponse> toListChatMessageResponse(List<ChatMessage> chatMessages);
}

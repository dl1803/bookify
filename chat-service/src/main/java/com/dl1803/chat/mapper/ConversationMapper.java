package com.dl1803.chat.mapper;

import com.dl1803.chat.dto.response.ConversationResponse;
import com.dl1803.chat.entity.Conversation;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConversationMapper {
    ConversationResponse toConversationResponse(Conversation conversation);

    List<ConversationResponse> toConversationResponseList(List<Conversation> conversations);

}

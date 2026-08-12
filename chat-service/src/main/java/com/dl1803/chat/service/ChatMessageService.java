package com.dl1803.chat.service;

import com.corundumstudio.socketio.SocketIOServer;
import com.dl1803.chat.dto.request.ChatMessageRequest;
import com.dl1803.chat.dto.response.ChatMessageResponse;
import com.dl1803.chat.entity.ChatMessage;
import com.dl1803.chat.entity.ParticipantInfo;
import com.dl1803.chat.exception.AppException;
import com.dl1803.chat.exception.ErrorCode;
import com.dl1803.chat.mapper.ChatMessageMapper;
import com.dl1803.chat.repository.ChatMessageRepository;
import com.dl1803.chat.repository.ConversationRepository;
import com.dl1803.chat.repository.httpClient.ProfileClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatMessageService {
    ChatMessageRepository chatMessageRepository;
    ProfileClient profileClient;
    ConversationRepository conversationRepository;
    SocketIOServer socketIOServer;

    ChatMessageMapper chatMessageMapper;

    public List<ChatMessageResponse> getMessages(String conversationId) {
        // validate conversation (check user is a member of the conversation)
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        conversationRepository.findById(conversationId)
                .orElseThrow(() -> new AppException(ErrorCode.CONVERSATION_NOT_FOUND))
                .getParticipants().stream()
                .filter(p -> userId.equals(p.getUserId()))
                .findAny().orElseThrow(() -> new AppException(ErrorCode.CONVERSATION_NOT_FOUND));

        var messages = chatMessageRepository.findAllByConversationIdOrderByCreatedDateDesc(conversationId);

        return messages.stream().map(this::toChatMessageResponse).toList();
    }

    public ChatMessageResponse create(ChatMessageRequest request) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        //validate conversationId (tìm cuộc chat và info của sender(userId))
        var conversation = conversationRepository.findById(request.getConversationId())
                .orElseThrow(() -> new AppException(ErrorCode.CONVERSATION_NOT_FOUND))
                .getParticipants().stream()
                .filter(p -> userId.equals(p.getUserId()))
                // findAny trả về 1 ptu bất kì thỏa đkiện và trả về Optional<T>
                .findAny().orElseThrow(() -> new AppException(ErrorCode.CONVERSATION_NOT_FOUND));

        // Get userinfo from profile service -> build chat message
        var userResponse = profileClient.getProfile(userId);
        if(Objects.isNull(userResponse) || Objects.isNull(userResponse.getResult())) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        var userInfo = userResponse.getResult();

        ChatMessage chatMessage = chatMessageMapper.toChatMessage(request);
        chatMessage.setSender(ParticipantInfo.builder()
                        .userId(userId)
                        .username(userInfo.getUsername())
                        .firstName(userInfo.getFirstName())
                        .lastName(userInfo.getLastName())
                        .nickname(conversation.getNickname()) // nickname của sender do targetUser đặt
                        .avatar(userInfo.getAvatar())
                .build());

        chatMessage.setCreatedDate(Instant.now());

        chatMessage = chatMessageRepository.save(chatMessage);

        // mock message
        String message = chatMessage.getMessage();

        // Publish socket event to Clients
        socketIOServer.getAllClients() // get all clients ở port 8099 (unthenticated -> test socket)
                .forEach(client -> {
            client.sendEvent("message", message); // gửi 1 event kèm name và content
        });

        // Convert to Response
        return toChatMessageResponse(chatMessage);
    }

    private ChatMessageResponse toChatMessageResponse(ChatMessage chatMessage){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        var chatMessageResponse = chatMessageMapper.toChatMessageResponse(chatMessage);

        chatMessageResponse.setMe(userId.equals(chatMessage.getSender().getUserId()));

        return chatMessageResponse;
    }
}

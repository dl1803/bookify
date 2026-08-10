package com.dl1803.chat.service;

import com.dl1803.chat.dto.request.ConversationRequest;
import com.dl1803.chat.dto.response.ApiResponse;
import com.dl1803.chat.dto.response.ConversationResponse;
import com.dl1803.chat.entity.Conversation;
import com.dl1803.chat.entity.ParticipantInfo;
import com.dl1803.chat.exception.AppException;
import com.dl1803.chat.exception.ErrorCode;
import com.dl1803.chat.mapper.ConversationMapper;
import com.dl1803.chat.repository.ConversationRepository;
import com.dl1803.chat.repository.httpClient.ProfileClient;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ConversationService {
    ConversationRepository conversationRepository;
    ProfileClient profileClient;
    ConversationMapper conversationMapper;


    public List<ConversationResponse> getMyConversations() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Conversation> conversations = conversationRepository.findAllByParticipantIdsContains(userId);
        return conversations.stream()
                .map(this::toConversationResponse)
                .toList();
    }

    public ConversationResponse createConversation(ConversationRequest request) {
        // Fetch user infos
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        var userInfoResponse = profileClient.getProfile(userId);
        var participantInfoResponse = profileClient.getProfile(
                request.getParticipantIds().getFirst()); // chỉ sử dụng cho direct
        if (Objects.isNull(userInfoResponse) || Objects.isNull(participantInfoResponse)){
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
        var userInfo = userInfoResponse.getResult();
        var participantInfo = participantInfoResponse.getResult();

        List<String> userIds = new ArrayList<>();
        userIds.add(userId);
        userIds.add(participantInfo.getUserId());

        String userIdHash = generateParticipantHash(userIds);


        List<ParticipantInfo> participantInfos = List.of(
            ParticipantInfo.builder()
                    .userId(userInfo.getUserId())
                    .username(userInfo.getUsername())
                    .firstName(userInfo.getFirstName())
                    .lastName(userInfo.getLastName())
                    .avatar(userInfo.getAvatar())
                    .build(),
            ParticipantInfo.builder()
                    .userId(participantInfo.getUserId())
                    .username(participantInfo.getUsername())
                    .firstName(participantInfo.getFirstName())
                    .lastName(participantInfo.getLastName())
                    .avatar(participantInfo.getAvatar())
                    .build()
        );

        // Build conversation info
        Conversation conversation = Conversation.builder()
                .type(request.getType())
                .participantsHash(userIdHash)
                .createdDate(Instant.now())
                .modifiedDate(Instant.now())
                .participants(participantInfos)
                .build();

        conversation = conversationRepository.save(conversation);
        return toConversationResponse(conversation);
    }


    private  String generateParticipantHash(List<String> ids){
        // Sắp xếp danh sách ID theo thứ tự A-Z trước khi nối
        List<String> sortedIds = ids.stream().sorted().toList();;

        StringJoiner stringJoiner = new StringJoiner("_");
        sortedIds.forEach(stringJoiner::add);
        return stringJoiner.toString();
    }

    private ConversationResponse toConversationResponse(Conversation conversation) {
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();

        ConversationResponse conversationResponse = conversationMapper.toConversationResponse(conversation);

        conversation.getParticipants().stream()
                // Lọc bỏ chính mình ra (chỉ giữ lại người có userId KHÁC currentUserId)
                .filter(participantInfo -> !participantInfo.getUserId().equals(currentUserId))
                // Lấy người đầu tiên tìm thấy (chính là đối phương trong direct chat)
                .findFirst()
                // Nếu tìm thấy thì gán thông tin của họ vào Response
                .ifPresent(participantInfo -> {
                    conversationResponse.setConversationName(participantInfo.getLastName() + participantInfo.getFirstName());
                    conversationResponse.setConversationAvatar(participantInfo.getAvatar());
                });
        return conversationResponse;
    }
}

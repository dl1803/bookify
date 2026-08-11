package com.dl1803.chat.service;

import com.dl1803.chat.dto.request.ConversationRequest;
import com.dl1803.chat.dto.request.UpdateNicknameRequest;
import com.dl1803.chat.dto.response.ConversationResponse;
import com.dl1803.chat.dto.response.UserProfileResponse;
import com.dl1803.chat.entity.Conversation;
import com.dl1803.chat.entity.ParticipantInfo;
import com.dl1803.chat.exception.AppException;
import com.dl1803.chat.exception.ErrorCode;
import com.dl1803.chat.mapper.ConversationMapper;
import com.dl1803.chat.repository.ConversationRepository;
import com.dl1803.chat.repository.httpClient.ProfileClient;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ConversationService {
    ConversationRepository conversationRepository;
    ProfileClient profileClient;
    ConversationMapper conversationMapper;


    public List<ConversationResponse> getMyConversations() {
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Conversation> conversations = conversationRepository.findAllByParticipantIdsContains(currentUserId);
        if (conversations.isEmpty()) {
            return Collections.emptyList();
        }

        Set<String> targetUserIds = conversations.stream()
                // flatMap : biến ds chứa các ds con thành 1 ds duy nhất
                .flatMap(c -> c.getParticipants().stream())
                // map : đi qua từng phần tử và lấy ra field cần thiết
                .map(ParticipantInfo::getUserId)
                // collect : gom các phần tử lại thành 1 collection (Set, List, Map...)
                .collect(Collectors.toSet());

        Map<String, UserProfileResponse> profileMap = new HashMap<>();
        if (!targetUserIds.isEmpty()) {
            var profilesResponse = profileClient.getUsersProfiles(new ArrayList<>(targetUserIds));
            if (Objects.nonNull(profilesResponse) && Objects.nonNull(profilesResponse.getResult())) {
                profileMap = profilesResponse.getResult().stream() // duyệt từng UserProfileResponse trong result
                        // para1 : lấy userId trong từng obj làm key
                        // para2 : Function.identity() tức lấy nguyên obj(UserProfileResponse) làm value
                        .collect(Collectors.toMap(UserProfileResponse::getUserId, Function.identity()));
            }
        }

        Map<String, UserProfileResponse> finalProfileMap = profileMap;
        return conversations.stream()
                .map(conversation -> toConversationResponseWithMap(conversation, currentUserId, finalProfileMap))
                .toList();
    }

    public ConversationResponse getConversation(String conversationId) {
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new AppException(ErrorCode.CONVERSATION_NOT_FOUND));

        boolean isParticipant = conversation.getParticipants().stream()
                .anyMatch(p -> p.getUserId().equals(currentUserId));

        if (!isParticipant) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        return toSingleConversationResponse(conversation);
    }


    public ConversationResponse createConversation(ConversationRequest request) {
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        String targetUserId = request.getParticipantIds().getFirst();

        List<String> userIds = List.of(currentUserId, targetUserId);
        String userIdHash = generateParticipantHash(userIds);
        var conversation = conversationRepository.findByParticipantsHash(userIdHash)
                .orElseGet(() -> { // nếu chưa có idHash
                    List<ParticipantInfo> participantInfos = List.of(
                            ParticipantInfo.builder().userId(currentUserId).build(),
                            ParticipantInfo.builder().userId(targetUserId).build()
                    );

                            Conversation newConversation = Conversation.builder()
                                    .type(request.getType())
                                    .participantsHash(userIdHash)
                                    .createdDate(Instant.now())
                                    .modifiedDate(Instant.now())
                                    .participants(participantInfos)
                                    .build();

                            return conversationRepository.save(newConversation);
                        }
                );

        return toSingleConversationResponse(conversation);
    }



    public ConversationResponse updateNickname(String conversationId, UpdateNicknameRequest request) {
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();

        // Tìm cuộc trò chuyện
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new AppException(ErrorCode.CONVERSATION_NOT_FOUND));

        // Tìm thông tin của target trong danh sách participants để đặt nickname
        boolean isUpdated = false;
        for (ParticipantInfo participant : conversation.getParticipants()) {
            // Chỉ cập nhật nickname cho targetUserId và kh phải là chính mình
            if (participant.getUserId().equals(request.getTargetUserId())
                    && !participant.getUserId().equals(currentUserId)) {

                participant.setNickname(request.getNickname());
                isUpdated = true;
                break;
            }
        }

        if (!isUpdated) {
            throw new AppException(ErrorCode.CONVERSATION_NOT_FOUND);
        }

        conversation.setModifiedDate(Instant.now());
        conversation = conversationRepository.save(conversation);

        return toSingleConversationResponse(conversation);
    }


    private ConversationResponse toConversationResponseWithMap(Conversation conversation, String currentUserId, Map<String, UserProfileResponse> profileMap) {
        ConversationResponse response = conversationMapper.toConversationResponse(conversation);

        for (ParticipantInfo participant : response.getParticipants()) {
            UserProfileResponse profile = profileMap.get(participant.getUserId());

            if (Objects.nonNull(profile)) {
                participant.setUsername(profile.getUsername());
                participant.setFirstName(profile.getFirstName());
                participant.setLastName(profile.getLastName());
                participant.setAvatar(profile.getAvatar());
            }

            if (!participant.getUserId().equals(currentUserId)) {
                enrichConversationNameAndAvatar(response, participant, profile);
            }
        }

        return response;
    }

    private ConversationResponse toSingleConversationResponse(Conversation conversation) {
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();

        List<String> participantIds = conversation.getParticipants().stream()
                .map(ParticipantInfo::getUserId)
                .toList();

        Map<String, UserProfileResponse> profileMap = new HashMap<>();
        if (!participantIds.isEmpty()) {
            var profilesResponse = profileClient.getUsersProfiles(participantIds);
            if (Objects.nonNull(profilesResponse) && Objects.nonNull(profilesResponse.getResult())) {
                profileMap = profilesResponse.getResult().stream()
                        .collect(Collectors.toMap(UserProfileResponse::getUserId, Function.identity()));
            }
        }

        return toConversationResponseWithMap(conversation, currentUserId, profileMap);
    }


    private void enrichConversationNameAndAvatar(ConversationResponse response, ParticipantInfo participantInfo, UserProfileResponse profile) {
        String name;
        if (participantInfo.getNickname() != null && !participantInfo.getNickname().isBlank()) {
            name = participantInfo.getNickname();
        } else {
            String firstName = Objects.nonNull(profile.getFirstName()) ? profile.getFirstName() : "";
            String lastName = Objects.nonNull(profile.getLastName()) ? profile.getLastName() : "";
            name = (lastName + " " + firstName).trim();
        }

        response.setConversationName(name);
        response.setConversationAvatar(profile.getAvatar());
    }

    private  String generateParticipantHash(List<String> ids){
        // Sắp xếp danh sách ID theo thứ tự A-Z trước khi nối
        List<String> sortedIds = ids.stream().sorted().toList();

        StringJoiner stringJoiner = new StringJoiner("_");
        sortedIds.forEach(stringJoiner::add);
        return stringJoiner.toString();
    }


}

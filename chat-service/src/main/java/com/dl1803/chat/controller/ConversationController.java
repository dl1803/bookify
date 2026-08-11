package com.dl1803.chat.controller;

import com.dl1803.chat.dto.request.ConversationRequest;
import com.dl1803.chat.dto.request.UpdateNicknameRequest;
import com.dl1803.chat.dto.response.ApiResponse;
import com.dl1803.chat.dto.response.ConversationResponse;
import com.dl1803.chat.service.ConversationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("conversations")
public class ConversationController {
    ConversationService conversationService;

    @PostMapping("/create")
    ApiResponse<ConversationResponse> createConversation(@RequestBody @Valid ConversationRequest request) {
        return ApiResponse.<ConversationResponse>builder()
                .result(conversationService.createConversation(request))
                .build();

    }

    @GetMapping("/my-conversations")
    ApiResponse<List<ConversationResponse>> getMyConversations() {
        return ApiResponse.<List<ConversationResponse>>builder()
                .result(conversationService.getMyConversations())
                .build();
    }

    @PutMapping("/{conversationId}/nickname")
    ApiResponse<ConversationResponse> updateNickname(
            @PathVariable("conversationId") String conversationId,
            @RequestBody @Valid UpdateNicknameRequest request) {
        return ApiResponse.<ConversationResponse>builder()
                .result(conversationService.updateNickname(conversationId, request))
                .build();
    }

    @GetMapping("/{conversationId}")
    ApiResponse<ConversationResponse> getConversation(@PathVariable("conversationId") String conversationId) {
        return ApiResponse.<ConversationResponse>builder()
                .result(conversationService.getConversation(conversationId))
                .build();
    }

}

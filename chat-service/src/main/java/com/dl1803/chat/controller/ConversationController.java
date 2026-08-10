package com.dl1803.chat.controller;

import com.dl1803.chat.dto.request.ConversationRequest;
import com.dl1803.chat.dto.response.ApiResponse;
import com.dl1803.chat.dto.response.ConversationResponse;
import com.dl1803.chat.service.ConversationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
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

}

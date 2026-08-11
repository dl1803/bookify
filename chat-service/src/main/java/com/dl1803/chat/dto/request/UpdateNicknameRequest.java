package com.dl1803.chat.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateNicknameRequest {
    @NotBlank(message = "Target userId is required")
    String targetUserId;

    String nickname; // Nickname mới / Rỗng, Null -> gỡ Nickname
}
package com.dl1803.identity.dto.request;

import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequest {
    @Size(min = 6, message = "UNAUTHENTICATED")
    String currentPassword;

    @Size(min = 6, message = "PASSWORD_INVALID")
    String newPassword;
}

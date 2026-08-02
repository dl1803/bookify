package com.dl1803.notification.controller;

import com.dl1803.notification.dto.request.SendEmailRequest;
import com.dl1803.notification.dto.response.ApiResponse;
import com.dl1803.notification.dto.response.EmailResponse;
import com.dl1803.notification.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailController {
    EmailService emailService;
    @PostMapping("/email/send")
    public ApiResponse<EmailResponse> sendEmail(@RequestBody SendEmailRequest sendEmailRequest){
        return ApiResponse.<EmailResponse>builder()
                .result(emailService.sendEmail(sendEmailRequest))
                .build();
    }
}

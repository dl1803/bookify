package com.dl1803.notification.service;

import com.dl1803.notification.dto.request.EmailRequest;
import com.dl1803.notification.dto.request.SendEmailRequest;
import com.dl1803.notification.dto.request.Sender;
import com.dl1803.notification.dto.response.EmailResponse;
import com.dl1803.notification.exception.AppException;
import com.dl1803.notification.exception.ErrorCode;
import com.dl1803.notification.repository.httpClient.EmailClient;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {
    EmailClient emailClient;

    @Value("${brevo.api-key}")
    @NonFinal
    String apiKey;

    public EmailResponse sendEmail(SendEmailRequest request ){
        EmailRequest emailRequest = EmailRequest.builder()
                .sender(Sender.builder()
                        .name("dl1803")
                        .email("loitran102030@gmail.com")
                        .build())
                .to(List.of((request.getTo())))
                .subject(request.getSubject())
                .htmlContent(request.getHtmlContent())
                .build();
        try {
            return emailClient.sendEmail(apiKey,emailRequest);
        } catch (FeignException e){
            throw new AppException(ErrorCode.CANNOT_SEND_EMAIL);
        }
    }
}

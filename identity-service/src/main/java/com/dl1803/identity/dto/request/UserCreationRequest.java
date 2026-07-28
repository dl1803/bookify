package com.dl1803.identity.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Size;

import com.dl1803.identity.validator.DobContraint;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 4, message = "USERNAME_INVALID")
    String username;

    @Size(min = 6, message = "PASSWORD_INVALID")
    String password;

    String firstName;
    String lastName;

    @DobContraint(
            min = 18,
            message = "INVALID_DOB")
    LocalDate dob;

    String city;
}

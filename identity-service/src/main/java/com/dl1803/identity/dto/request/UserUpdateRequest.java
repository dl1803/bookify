package com.dl1803.identity.dto.request;

import java.time.LocalDate;
import java.util.List;

import com.dl1803.identity.validator.DobContraint;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String password;
    String firstName;
    String lastName;

    @DobContraint(min = 18, message = "INVALID_DOB")
    LocalDate dob;

    List<String> roles;
}

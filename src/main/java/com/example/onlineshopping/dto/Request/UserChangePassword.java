package com.example.onlineshopping.dto.Request;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserChangePassword {

    String oldPassword;
    @Size(min = 8)
    String newPassword;
}

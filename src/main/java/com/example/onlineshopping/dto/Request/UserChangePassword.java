package com.example.onlineshopping.dto.Request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserChangePassword {

    private String oldPassword;
    @Size(min = 8)
    private String newPassword;
}

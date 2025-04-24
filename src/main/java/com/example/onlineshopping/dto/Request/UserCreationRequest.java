package com.example.onlineshopping.dto.Request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserCreationRequest {
    private String email;

    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String password;
    private String fullname;
    private String status;
    private String role;

}

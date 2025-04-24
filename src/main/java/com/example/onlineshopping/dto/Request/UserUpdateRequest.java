package com.example.onlineshopping.dto.Request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserUpdateRequest {
    private String email;
    private String fullname;
    private String status;

}

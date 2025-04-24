package com.example.onlineshopping.dto.Response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private String email;
    private String fullname;
    private String status;
    private String role;
}

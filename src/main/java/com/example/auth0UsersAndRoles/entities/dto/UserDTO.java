package com.example.auth0UsersAndRoles.entities.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private String email;
    private String password;
    private String connection;
    private String name;
    private String nickName;
    private List<String> roles;
    private String id;
}

package com.banking.user.dto;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserResponseDto {

    private Long id;

    private String email;

    private String name;

    private boolean isActive;

    private boolean isBlocked;
}

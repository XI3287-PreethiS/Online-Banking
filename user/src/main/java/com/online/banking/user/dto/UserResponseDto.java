package com.online.banking.user.dto;


import lombok.*;

//@Builder
@Getter
@Setter
@RequiredArgsConstructor
public class UserResponseDto {

    private Long userId;

    private String email;

    private String name;

    private boolean isActive;

    private boolean isBlocked;


}

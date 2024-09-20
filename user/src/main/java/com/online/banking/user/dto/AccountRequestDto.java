package com.online.banking.user.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequestDto {
    private Long userId;
    private String userName;
    private String email;
    private String accountType;
}

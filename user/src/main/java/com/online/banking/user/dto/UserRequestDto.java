package com.online.banking.user.dto;

import jakarta.validation.constraints.*;
import lombok.*;


@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class UserRequestDto {

	@NotBlank(message = "Email is mandatory")
	@Email(message = "Invalid email format")
	private String email;

	@NotBlank(message = "Password is mandatory")
	@Size(min = 6, message = "Password should have at least 6 characters")
	private String password;

	private String newPassword;

	@NotBlank(message = "Name is mandatory")
	private String name;

	private boolean isActive;

	private boolean isBlocked;

}

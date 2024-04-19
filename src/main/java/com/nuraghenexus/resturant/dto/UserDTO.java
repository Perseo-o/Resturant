package com.nuraghenexus.resturant.dto;

import com.nuraghenexus.resturant.model.enumerations.Roles;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

	Long id;

	String firstName;

	String lastName;

	String email;

	String password;

	Roles role;

	LocalDateTime createdAt;

	LocalDateTime updatedAt;
}
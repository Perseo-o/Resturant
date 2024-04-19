package com.nuraghenexus.resturant.converter;

import com.nuraghenexus.resturant.dto.UserDTO;
import com.nuraghenexus.resturant.model.User;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Component;


/**
 * This component class converts User objects to UserDTO objects and vice versa, and also provides conversion for User objects to UserDTO objects.
 */
@Component
public class UserConverter extends AbstractConverter<User, UserDTO> {

	private Argon2PasswordEncoder encoder;

	@Override
	public User toEntity(UserDTO userDTO) {
		User user = null;
		if (userDTO != null) {
			user = new User(
					userDTO.getId(),
					userDTO.getFirstName(),
					userDTO.getLastName(),
					userDTO.getEmail(),
					userDTO.getPassword(),
					userDTO.getRole(),
					userDTO.getCreatedAt(),
					userDTO.getUpdatedAt());
		}
		return user;
	}

	@Override
	public UserDTO toDTO(User user) {
		UserDTO userDTO = null;
		if (user != null) {
			userDTO = new UserDTO(
					user.getId(),
					user.getFirstName(),
					user.getLastName(),
					user.getEmail(),
					user.getPassword(),
					user.getRole(),
					user.getCreatedAt(),
					user.getUpdatedAt());
		}
		return userDTO;
	}
}

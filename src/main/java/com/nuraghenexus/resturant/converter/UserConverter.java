package com.nuraghenexus.resturant.converter;

import com.nuraghenexus.resturant.dto.UserDTO;
import com.nuraghenexus.resturant.dto.utils.FindByEoURequest;
import com.nuraghenexus.resturant.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
					userDTO.getEmail(),
					userDTO.getUsername(),
					userDTO.getPassword(),
					userDTO.isAccountNonLocked(),
					userDTO.isEnabled(),
					userDTO.getCreatedAt(),
					userDTO.getRole());
		}
		return user;
	}

	@Override
	public UserDTO toDTO(User user) {
		UserDTO userDTO = null;
		if (user != null) {
			userDTO = new UserDTO(
					user.getId(),
					user.getEmail(),
					user.getUsername(),
					user.getPassword(),
					user.isAccountNonLocked(),
					user.isEnabled(),
					user.getCreatedAt(),
					user.getRole());
		}
		return userDTO;
	}

	public FindByEoURequest toUserForVT(User user) {
		FindByEoURequest findByEoURequest = null;
		if (user != null) {
			findByEoURequest = new FindByEoURequest(
					user.getId(),
					user.getEmail(),
					user.getUsername(),
					user.isEnabled(),
					user.isAccountNonLocked()
			);
		}
		return findByEoURequest;
	}
}

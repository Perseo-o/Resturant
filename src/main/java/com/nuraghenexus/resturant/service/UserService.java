package com.nuraghenexus.resturant.service;

import com.nuraghenexus.resturant.constants.JWT;
import com.nuraghenexus.resturant.dto.UserDTO;
import com.nuraghenexus.resturant.model.User;
import com.nuraghenexus.resturant.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService extends AbstractService<User, UserDTO>  {

	private final UserRepository userRepository;

	public UserDetailsService userDetailsService() {
		return new UserDetailsService() {
			@Override
			public UserDetails loadUserByUsername(String username) {
				return userRepository.findByEmail(username)
						.orElseThrow(() -> new UsernameNotFoundException("User not found"));
			}
		};
	}

	public User save(User newUser) {
		if (newUser.getId() == null) {
			newUser.setCreatedAt(LocalDateTime.now());
		}

		newUser.setUpdatedAt(LocalDateTime.now());
		return userRepository.save(newUser);
	}

}
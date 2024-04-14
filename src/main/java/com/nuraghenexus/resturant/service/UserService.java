package com.nuraghenexus.resturant.service;

import com.nuraghenexus.resturant.constants.API;
import com.nuraghenexus.resturant.converter.UserConverter;
import com.nuraghenexus.resturant.dto.UserDTO;
import com.nuraghenexus.resturant.dto.utils.AuthResponse;
import com.nuraghenexus.resturant.dto.utils.LoginRequest;
import com.nuraghenexus.resturant.dto.utils.RegisterRequest;
import com.nuraghenexus.resturant.model.User;
import com.nuraghenexus.resturant.model.enumerations.RegistrationValidities;
import com.nuraghenexus.resturant.model.enumerations.Roles;
import com.nuraghenexus.resturant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService extends AbstractService<User, UserDTO> implements UserDetailsService {

	@Autowired
	private UserConverter converter;

	@Autowired
	private UserRepository userRepo;

	private BCryptPasswordEncoder encoder= new BCryptPasswordEncoder();


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepo.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(API.usernameExc(username)));
	}


	public Map<String, Object> register(RegisterRequest request) {
		Map<String, Object> map = new LinkedHashMap<>();
		switch (checkRegistrationValidity(request)) {
			// Handling cases where registration fails due to conflicts or invalid data
			case USERNAME_AND_EMAIL_EXISTS:
				map.put(API.GEN_MSG, API.AUTH_USERNAME_AND_EMAIL_CONFLICT);
				map.put(API.GEN_STATUS, HttpStatus.CONFLICT);
				break;
			case USERNAME_EXISTS:
				map.put(API.GEN_MSG, API.AUTH_USERNAME_CONFLICT);
				map.put(API.GEN_STATUS, HttpStatus.CONFLICT);
				break;
			case EMAIL_EXISTS:
				map.put(API.GEN_MSG, API.AUTH_EMAIL_CONFLICT);
				map.put(API.GEN_STATUS, HttpStatus.CONFLICT);
				break;
			case INVALID_EMAIL:
				map.put(API.GEN_MSG, API.AUTH_INVALID_EMAIL);
				map.put(API.GEN_STATUS, HttpStatus.BAD_REQUEST);
				break;
			case INVALID_USERNAME:
				map.put(API.GEN_MSG, API.AUTH_INVALID_USERNAME);
				map.put(API.GEN_STATUS, HttpStatus.BAD_REQUEST);
				break;
			case INVALID_PASSWORD:
				map.put(API.GEN_MSG, API.AUTH_INVALID_PASSWORD);
				map.put(API.GEN_STATUS, HttpStatus.BAD_REQUEST);
				break;
			// If registration data is valid, proceed with registration
			case VALID:
				return registerValidCase(map, request);
			// Handling other cases such as internal server errors
			default:
				map.put(API.GEN_STATUS, HttpStatus.INTERNAL_SERVER_ERROR);
				map.put(API.GEN_MSG, API.AUTH_SOMETHING_WENT_WRONG);
				break;
		}
		return map;
	}


	private Map<String, Object> registerValidCase(Map<String, Object> map, RegisterRequest request) {
		User userToRegister = toUserForRegister(request);
		try {
			repository.save(userToRegister);
		} catch (Exception ex) {
			map.put(API.GEN_STATUS, HttpStatus.NOT_ACCEPTABLE);
			map.put(API.GEN_MSG, API.AUTH_INVALID_EMAIL);
			return map;
		}

		return map;
	}

	/**
	 * Authenticates a user based on the provided login credentials.
	 *
	 * @param userToCheck The login credentials of the user.
	 * @return A map containing the authentication response.
	 */

	public Map<String, Object> authenticate(LoginRequest userToCheck) {
		Map<String, Object> map = new LinkedHashMap<>();
		if (doesEmailExists(userToCheck.getUsername())) {
			Optional<User> userResultToConvert = userRepo.findByUsername(userToCheck.getUsername());
			return userChecksForLog(userToCheck, map, userResultToConvert);
		}
		map.put(API.GEN_MSG, API.AUTH_USERNAME_ERR);
		map.put(API.GEN_STATUS, HttpStatus.NOT_ACCEPTABLE);
		return map;
	}

	/**
	 * Checks the authentication process for a user based on the provided credentials.
	 *
	 * @param userToCheck         The login credentials of the user.
	 * @param map                 The map to store the authentication response.
	 * @param userResultToConvert The optional user data retrieved from the repository.
	 * @return A map containing the authentication response.
	 */
	private Map<String, Object> userChecksForLog(LoginRequest userToCheck, Map<String, Object> map, Optional<User> userResultToConvert) {
		if (userResultToConvert.isPresent()) {
			User userConverted = userResultToConvert.get();
			AuthResponse authResponse = convertToAuthResponseDTO(userConverted);
			if (encoder.matches(userToCheck.getPassword(), userConverted.getPassword())) {
				if (userConverted.isEnabled()) {
					map.put(API.GEN_DATA, authResponse);
					map.put(API.GEN_MSG, API.AUTH_LOGGED_IN);
					map.put(API.GEN_STATUS, HttpStatus.ACCEPTED);
					return map;
				} else {
					map.put(API.GEN_MSG, API.AUTH_GO_TO_MAIL);
					map.put(API.GEN_STATUS, HttpStatus.NOT_ACCEPTABLE);
					return map;
				}
			} else {
				map.put(API.GEN_MSG, API.AUTH_PASS_ERR);
				map.put(API.GEN_STATUS, HttpStatus.NOT_ACCEPTABLE);
				return map;
			}
		}
		map.put(API.GEN_MSG, API.AUTH_ACCOUNT_NOT_EXISTS);
		map.put(API.GEN_STATUS, HttpStatus.NOT_ACCEPTABLE);
		return map;
	}

	/**
	 * Converts the provided authentication request to a User object for registration.
	 *
	 * @param request The authentication request containing user details.
	 * @return The User object created for registration.
	 */
	public User toUserForRegister(RegisterRequest request) {
		User user = null;
		if (request != null) {
			user = User.builder()
					.email(request.getEmail())
					.username(request.getUsername())
					.password(encoder.encode(request.getPassword()))
					.createdAt(LocalDateTime.now())
					.isAccountNonLocked(true)
					.isEnabled(true)
					.role(Roles.USER)
					.build();
		}
		return user;
	}

	/**
	 * Checks the validity of the registration data provided in the authentication request.
	 *
	 * @param request The authentication request containing user details.
	 * @return The registration validity status.
	 */
	private RegistrationValidities checkRegistrationValidity(RegisterRequest request) {
		/*if (isValidEmail(request.getEmail())) {
			if (isValidUsername(request.getUsername())) {
				if (isValidPassword(request.getPassword())) {
					if (doesEmailExists(request.getEmail())) {
						return RegistrationValidities.EMAIL_EXISTS;
					}
					return RegistrationValidities.VALID;
				}
				return RegistrationValidities.INVALID_PASSWORD;
			}
			return RegistrationValidities.INVALID_USERNAME;
		}*/
		if (isValidPassword(request.getPassword())) {
			if (doesEmailExists(request.getEmail())) {
				return RegistrationValidities.EMAIL_EXISTS;
			}
			return RegistrationValidities.VALID;
		}
		return RegistrationValidities.INVALID_PASSWORD;
	}

	/**
	 * Retrieves user data from the repository based on the provided email or username.
	 *
	 * @param emailOrUsername The email or username of the user to retrieve.
	 * @return A map containing the user data if found, or a message indicating user not found.
	 */
	public Map<String, Object> getUserByEmail(String emailOrUsername) {
		Map<String, Object> map = new LinkedHashMap<>();
		Optional<User> userByEmail = userRepo.findByEmail(emailOrUsername);
		if (userByEmail.isPresent()) {
			map.put(API.GEN_MSG, API.GEN_FOUND);
			map.put(API.GEN_DATA, converter.toUserForVT(userByEmail.get()));
			return map;
		} else {
			map.put(API.GEN_MSG, API.USER_NOT_FOUND);
		}
		return map;
	}



	/**
	 * Checks if a username already exists.
	 * @param username The username to check.
	 * @return true if the username exists, false otherwise.

	public boolean doesUsernameExists(String username){
	return userRepo.findByUsername(username).isPresent();
	}*/

	/**
	 * Checks if an email already exists.
	 * @param email The email to check.
	 * @return true if the email exists, false otherwise.
	 */
	public boolean doesEmailExists(String email){
		return userRepo.findByEmail(email).isPresent();
	}

	/**
	 * Converts a User object to an AuthResponse object.
	 *
	 * @param user The user to convert.
	 * @return AuthResponse The resulting AuthResponse object.
	 */
	private AuthResponse convertToAuthResponseDTO(User user) {
		return new AuthResponse(
				user.getId(),
				user.getEmail(),
				user.getUsername(),
				user.isAccountNonLocked(),
				user.getRole()
		);
	}

	/**
	 * Checks if the provided string is a valid email address.
	 *
	 * @param email The string to check.
	 * @return boolean true if the string is a valid email address, false otherwise.

	private boolean isValidEmail(String email) {
	try {
	InternetAddress internetAddress = new InternetAddress(email);
	internetAddress.validate();
	return true;
	} catch (AddressException ex) {
	return false;
	}
	}*/

	/**
	 * Checks if the provided string is a valid username.
	 *
	 * @param username The string to check.
	 * @return boolean true if the string is a valid username, false otherwise.
	 */
	/*
	private boolean isValidUsername(String username) {
	//Check if the username is not null and has a length between 3 and 15 characters
	if (username != null && username.length() >= 3 && username.length() <= 15) {
	//Check if the username contains only lowercase alphanumeric characters
	return username.matches(API.USER_REGEX_USRNM);
	} else {
	return false;
	}
	}*/

	/**
	 * Checks if the provided string is a valid username.
	 *
	 * @param password The string to check.
	 * @return boolean true if the string is a valid username, false otherwise.
	 */
	private boolean isValidPassword(String password) {
		//Check if the username is not null and has a length between 3 and 15 characters
		if (password != null && password.length() >= 8 && password.length() <= 25) {
			//Check if the username contains only lowercase alphanumeric characters
			return password.matches( API.USER_REGEX_PASS);
		} else {
			return false;
		}
	}

	/**
	 * Creates a new user. Currently not implemented.
	 * @param userDTO The UserDTO object containing user data.
	 * @return ResponseEntity containing the operation result.
	 */
	@Override
	public Map<String, Object> create(UserDTO userDTO) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put(API.GEN_MSG, API.GEN_CANT_USE_API);
		return map;
	}


	/**
	 * Retrieves All users. Currently not implemented.
	 * @return ResponseEntity containing the operation result.
	 */
	@Override
	public Map<String, Object> getAll() {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put(API.GEN_MSG, API.GEN_CANT_USE_API);
		return map; //new ResponseEntity<>(map, HttpStatus.NOT_IMPLEMENTED);
	}
}
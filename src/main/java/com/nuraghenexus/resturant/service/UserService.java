package com.nuraghenexus.resturant.service;

import com.nuraghenexus.resturant.config.jwt.JwtService;
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
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
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

	@Autowired
	private Argon2PasswordEncoder encoder;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;



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
			map.put(API.GEN_STATUS, HttpStatus.ACCEPTED);
			map.put(API.GEN_MSG, "L'utente e' stato creato con successo! ");
		} catch (Exception ex) {
			map.put(API.GEN_STATUS, HttpStatus.NOT_ACCEPTABLE);
			map.put(API.GEN_MSG, API.AUTH_INVALID_EMAIL);
			return map;
		}
		return map;
	}

	public Map<String, Object> authenticate(LoginRequest userToCheck) {
		Map<String, Object> map = new LinkedHashMap<>();
		if (!isValidEmail(userToCheck.getUsername())) {
			if (doesUsernameExists(userToCheck.getUsername())) {
				Optional<User> userResultToConvert = userRepo.findByUsername(userToCheck.getUsername());
				return userChecksForLog(userToCheck, map, userResultToConvert);
			}
			map.put(API.GEN_MSG, API.AUTH_USERNAME_ERR);
		} else if (isValidEmail(userToCheck.getUsername())) {
			if (doesEmailExists(userToCheck.getUsername())) {
				Optional<User> userResultToConvert = userRepo.findByEmail(userToCheck.getUsername());
				return userChecksForLog(userToCheck, map, userResultToConvert);
			}
			map.put(API.GEN_MSG, API.AUTH_EMAIL_ERR);
		} else {
			map.put(API.GEN_MSG, API.AUTH_ACCOUNT_NOT_EXISTS);
		}
		map.put(API.GEN_STATUS, HttpStatus.NOT_ACCEPTABLE);
		return map;
	}

	private Map<String, Object> userChecksForLog(LoginRequest userToCheck, Map<String, Object> map, Optional<User> userResultToConvert) {
		if (userResultToConvert.isPresent()) {
			User userConverted = userResultToConvert.get();
			AuthResponse authResponse = convertToAuthResponseDTO(userConverted);
			if (encoder.matches(userToCheck.getPassword(), userConverted.getPassword())) {
				if (userConverted.isEnabled()) {
					String jwtToken = jwtService.generateToken(userConverted);
					map.put(API.GEN_DATA, authResponse);
					map.put(API.GEN_MSG, API.AUTH_LOGGED_IN);
					map.put(API.GEN_TKN, jwtToken);
					map.put(API.GEN_STATUS, HttpStatus.ACCEPTED);
                } else {
					map.put(API.GEN_MSG, API.AUTH_GO_TO_MAIL);
					map.put(API.GEN_STATUS, HttpStatus.NOT_ACCEPTABLE);
                }
            } else {
				map.put(API.GEN_MSG, API.AUTH_PASS_ERR);
				map.put(API.GEN_STATUS, HttpStatus.NOT_ACCEPTABLE);
            }
            return map;
        }
		map.put(API.GEN_MSG, API.AUTH_ACCOUNT_NOT_EXISTS);
		map.put(API.GEN_STATUS, HttpStatus.NOT_ACCEPTABLE);
		return map;
	}

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

	private RegistrationValidities checkRegistrationValidity(RegisterRequest request) {
		if (isValidEmail(request.getEmail())) {
			if (isValidUsername(request.getUsername())) {
				if (isValidPassword(request.getPassword())) {
					if (doesUsernameExists(request.getUsername()) && doesEmailExists(request.getEmail())) {
						return RegistrationValidities.USERNAME_AND_EMAIL_EXISTS;
					}
					if (doesUsernameExists(request.getUsername())) {
						return RegistrationValidities.USERNAME_EXISTS;
					}
					if (doesEmailExists(request.getEmail())) {
						return RegistrationValidities.EMAIL_EXISTS;
					}
					return RegistrationValidities.VALID;
				}
				return RegistrationValidities.INVALID_PASSWORD;
			}
			return RegistrationValidities.INVALID_USERNAME;
		}
		return RegistrationValidities.INVALID_EMAIL;
	}

	public Map<String, Object> getUserByEmailOrUsername(String emailOrUsername) {
		Map<String, Object> map = new LinkedHashMap<>();
		Optional<User> userByEmail = userRepo.findByEmail(emailOrUsername);
		Optional<User> userByUsername = userRepo.findByUsername(emailOrUsername);
		if (userByEmail.isPresent()) {
			map.put(API.GEN_MSG, API.GEN_FOUND);
			map.put(API.GEN_DATA, converter.toUserForVT(userByEmail.get()));
			return map;
		} else if (userByUsername.isPresent()) {
			map.put(API.GEN_MSG, API.GEN_FOUND);
			map.put(API.GEN_DATA, converter.toUserForVT(userByUsername.get()));
			return map;
		} else {
			map.put(API.GEN_MSG, API.USER_NOT_FOUND);
		}
		return map;
	}


	public boolean doesUsernameExists(String username){
		return userRepo.findByUsername(username).isPresent();
	}

	public boolean doesEmailExists(String email){
		return userRepo.findByEmail(email).isPresent();
	}

	private AuthResponse convertToAuthResponseDTO(User user) {
		return new AuthResponse(
				user.getId(),
				user.getEmail(),
				user.getUsername(),
				user.isAccountNonLocked(),
				user.getRole()
		);
	}

	private boolean

	isValidEmail(String email) {
		try {
			InternetAddress internetAddress = new InternetAddress(email);
			internetAddress.validate();
			return true;
		} catch (AddressException ex) {
			return false;
		}
	}

	private boolean isValidUsername(String username) {
		//Check if the username is not null and has a length between 3 and 15 characters
		if (username != null && username.length() >= 3 && username.length() <= 15) {
			//Check if the username contains only lowercase alphanumeric characters
			return username.matches(API.USER_REGEX_USRNM);
		} else {
			return false;
		}
	}

	private boolean isValidPassword(String password) {
		//Check if the username is not null and has a length between 3 and 15 characters
		if (password != null && password.length() >= 8 && password.length() <= 25) {
			//Check if the username contains only lowercase alphanumeric characters
			return password.matches( API.USER_REGEX_PASS);
		} else {
			return false;
		}
	}

	@Override
	public Map<String, Object> create(UserDTO userDTO) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put(API.GEN_MSG, API.GEN_CANT_USE_API);
		return map;
	}


	@Override
	public Map<String, Object> getAll() {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put(API.GEN_MSG, API.GEN_CANT_USE_API);
		return map; //new ResponseEntity<>(map, HttpStatus.NOT_IMPLEMENTED);
	}
}
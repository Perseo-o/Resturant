package com.nuraghenexus.resturant.service;

import com.nuraghenexus.resturant.config.jwt.JwtService;
import com.nuraghenexus.resturant.dto.utils.AuthResponse;
import com.nuraghenexus.resturant.dto.utils.SignInRequest;
import com.nuraghenexus.resturant.dto.utils.SignUpRequest;
import com.nuraghenexus.resturant.model.User;
import com.nuraghenexus.resturant.model.enumerations.Roles;
import com.nuraghenexus.resturant.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final Argon2PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    //------------------------------------------------------------------------REGISTRATION------------------------------------------------------------------------------------------------



    public AuthResponse signup(SignUpRequest request) {

        var user = User
                .builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Roles.USER)
                .build();

        user = userService.save(user);
        var jwt = jwtService.generateToken(user);
        return AuthResponse.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .token(jwt)
                .build();
    }


    //------------------------------------------------------------------------LOGIN------------------------------------------------------------------------------------------------
    public AuthResponse signin(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));

        var jwt = jwtService.generateToken(user);

        return AuthResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .token(jwt)
                .build();
    }



}
package com.nuraghenexus.resturant.controller;

import com.nuraghenexus.resturant.constants.API;
import com.nuraghenexus.resturant.dto.utils.AuthResponse;
import com.nuraghenexus.resturant.service.AuthService;
import org.springframework.web.bind.annotation.*;

import com.nuraghenexus.resturant.dto.utils.SignInRequest;
import com.nuraghenexus.resturant.dto.utils.SignUpRequest;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping(API.AUTH_REQ_MAP)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(API.AUTH_REGISTER)
    public AuthResponse signup(@RequestBody SignUpRequest request) {
        return authService.signup(request);
    }

    @PostMapping(API.AUTH_AUTHENTICATE)
    public AuthResponse signin(@RequestBody SignInRequest request) {
        return authService.signin(request);
    }

    @DeleteMapping(API.AUTH_DELETE)
    public String delete(@RequestParam("id") Long id) {
        return authService.delete(id);
    }
}
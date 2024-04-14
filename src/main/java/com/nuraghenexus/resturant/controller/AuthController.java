package com.nuraghenexus.resturant.controller;

import com.nuraghenexus.resturant.constants.API;
import com.nuraghenexus.resturant.dto.utils.LoginRequest;
import com.nuraghenexus.resturant.dto.utils.RegisterRequest;
import com.nuraghenexus.resturant.service.UserService;
import com.nuraghenexus.resturant.util.ResponseUtilController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller class for handling authentication requests.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(API.AUTH_REQ_MAP)
public class AuthController {

    private final UserService service;

    /**
     * Handles the registration request.
     * @param request The registration request body.
     * @return ResponseEntity containing the registration response.
     */
    @PostMapping(API.AUTH_REGISTER)
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest request) {
        return ResponseUtilController.handleAuthResponses(service.register(request));
    }

    /**
     * Handles the authentication request.
     * @param request The authentication request body.
     * @return ResponseEntity containing the authentication response.
     */
    @PostMapping(API.AUTH_AUTHENTICATE)
    public ResponseEntity<Map<String, Object>> authenticate(@RequestBody LoginRequest request){
        return ResponseUtilController.handleAuthResponses(service.authenticate(request));
    }
}
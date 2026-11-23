
package com.gobito.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.gobito.auth.dto.LoginRequest;
import com.gobito.auth.dto.LoginResponse;
import com.gobito.auth.service.AuthService;
import com.gobito.user.dto.RegistrationRequest;
import com.gobito.user.dto.RegistrationResponse;
import com.gobito.user.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    public AuthController(
            UserService userService,
            AuthService authService) {

        this.userService = userService;
        this.authService = authService;
    }

    // Customer registration
    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(
            @Valid @RequestBody RegistrationRequest request) {

        RegistrationResponse response =
                userService.registerUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Store owner registration
    @PostMapping("/register/store-owner")
    public ResponseEntity<RegistrationResponse> registerStoreOwner(
            @Valid @RequestBody RegistrationRequest request) {

        RegistrationResponse response =
                userService.registerStoreOwner(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }
}

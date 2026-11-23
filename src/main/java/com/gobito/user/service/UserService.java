
package com.gobito.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gobito.user.entity.User;
import com.gobito.user.entity.Role;
import com.gobito.user.repository.UserRepository;
import com.gobito.user.dto.RegistrationRequest;
import com.gobito.user.dto.RegistrationResponse;
import com.gobito.common.exception.DuplicateResourceException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Normal customer registration
    @Transactional
    public RegistrationResponse registerUser(
            RegistrationRequest request) {

        return registerUserWithRole(request, Role.CUSTOMER);
    }

    // Store owner registration
    @Transactional
    public RegistrationResponse registerStoreOwner(
            RegistrationRequest request) {

        return registerUserWithRole(request, Role.STORE_OWNER);
    }

    // Common registration logic
    private RegistrationResponse registerUserWithRole(
            RegistrationRequest request,
            Role role) {

        String email = request.getEmail()
                .trim()
                .toLowerCase();

        // Check duplicate email
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException(
                    "Email is already registered");
        }

        // Normalize phone number
        String phone = request.getPhone();

        if (phone != null) {
            phone = phone.trim();

            if (phone.isEmpty()) {
                phone = null;
            }
        }

        // Check duplicate phone
        if (phone != null && userRepository.existsByPhone(phone)) {
            throw new DuplicateResourceException(
                    "Phone number is already registered");
        }

        // Create user
        User user = new User();

        user.setName(request.getName().trim());
        user.setEmail(email);

        // Encrypt password using BCrypt
        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword()));

        user.setPhone(phone);

        // Assign role internally
        user.setRole(role);

        // Save user in PostgreSQL
        User savedUser = userRepository.save(user);

        return new RegistrationResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                "Registration successful"
        );
    }
}

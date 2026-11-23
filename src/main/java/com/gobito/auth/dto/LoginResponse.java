
package com.gobito.auth.dto;

public class LoginResponse {

    private String accessToken;
    private String tokenType;
    private Long userId;
    private String name;
    private String email;
    private String role;

    public LoginResponse(
            String accessToken,
            Long userId,
            String name,
            String email,
            String role) {

        this.accessToken = accessToken;
        this.tokenType = "Bearer";
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}

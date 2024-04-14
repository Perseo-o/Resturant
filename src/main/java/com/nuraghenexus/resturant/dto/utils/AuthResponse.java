package com.nuraghenexus.resturant.dto.utils;

import com.nuraghenexus.resturant.model.enumerations.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private Long id;
    private String email;
    private String username;
    private boolean isAccountNonLocked;
    private Roles role;
}
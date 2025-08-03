package org.example.cinema_reservation_system.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private final DecodedJWT decodedJWT;

    public JwtAuthenticationToken(UserDetails principal, DecodedJWT decodedJWT) {
        super(principal, null, principal.getAuthorities());
        this.decodedJWT = decodedJWT;
    }
}

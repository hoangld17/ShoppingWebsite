package com.example.shoesmanagement.security;

import com.example.shoesmanagement.exception.ApplicationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.shoesmanagement.model.util.SecurityConstant.*;


@Component
public class JwtTokenProvider {

    /**
     * THIS IS NOT A SECURE PRACTICE! For simplicity, we are storing a static key here. Ideally, in a
     * microservices environment, this key would be kept on a config-server.
     */
    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.expire-length:43200000}")
    private long validityInMilliseconds = 43200000; // 1h

    @Autowired
    private MyUserDetails myUserDetails;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(UserDetails principal) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(principal.getUsername())
                .claim(AUTHORITIES, principal.getAuthorities())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        return new UsernamePasswordAuthenticationToken(
                getUsername(token), null, getAuthorities(token));
    }

    public String getUsername(String token) {
        System.out.println(getClaims(token).getSubject());
        return getClaims(token).getSubject();
    }

    private Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    private Set<SimpleGrantedAuthority> getAuthorities(String token) {
        final var authorities = (ArrayList<Map<String, String>>) getClaims(token).get(AUTHORITIES);
        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.get(AUTHORITY)))
                .collect(Collectors.toSet());
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader(AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new ApplicationException(TOKEN_CANNOT_BE_VERIFIED, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

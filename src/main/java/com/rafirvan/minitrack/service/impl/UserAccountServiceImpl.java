package com.rafirvan.minitrack.service.impl;

import com.rafirvan.minitrack.constant.Gender;
import com.rafirvan.minitrack.dto.request.LoginRequest;
import com.rafirvan.minitrack.dto.request.UserRegistrationRequest;
import com.rafirvan.minitrack.dto.response.UserRegistrationResponse;
import com.rafirvan.minitrack.service.UserAccountService;
import com.rafirvan.minitrack.util.ValidationUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService, UserDetailsService {

    @PersistenceContext
    private final EntityManager entityManager;
    private final PasswordEncoder passwordEncoder;
    private final ValidationUtil validationUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserRegistrationResponse registerUser(UserRegistrationRequest request) {
        validationUtil.validate(request);

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        try {
            entityManager.createNativeQuery("INSERT INTO user_accounts (username, password, role) VALUES (?, ?, ?)")
                    .setParameter(1, request.getUsername())
                    .setParameter(2, hashedPassword)
                    .setParameter(3, "ROLE_USER")
                    .executeUpdate();
        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is already in use");
        }


        Long userAccountId = (Long) entityManager.createNativeQuery("SELECT id FROM user_accounts WHERE username = ?")
                .setParameter(1, request.getUsername())
                .getSingleResult();


        try {
            entityManager.createNativeQuery("INSERT INTO user_profiles (user_account_id, full_name, gender, email) VALUES (?, ?, ?, ?)")
                    .setParameter(1, userAccountId)
                    .setParameter(2, request.getFullName())
                    .setParameter(3, Gender.valueOf(request.getGender().toUpperCase()).toString())
                    .setParameter(4, request.getEmail())
                    .executeUpdate();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid gender value. Allowed values are 'MALE' or 'FEMALE'.", e);
        }

        UserRegistrationResponse response = new UserRegistrationResponse();
        response.setUserId(userAccountId);
        response.setUsername(request.getUsername());
        response.setFullName(request.getFullName());
        response.setGender(request.getGender());
        response.setEmail(request.getEmail());

        return response;
    }

    @Override
    public void loginUser(LoginRequest loginRequest) {
        try {
            UserDetails userDetails = loadUserByUsername(loginRequest.getUsername());

            if (!passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
                throw new BadCredentialsException("Invalid password");
            }

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (UsernameNotFoundException | BadCredentialsException e) {
            log.error("Invalid credentials for username: " + loginRequest.getUsername(), e);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password", e);
        } catch (Exception e) {
            log.error("Authentication error for username: " + loginRequest.getUsername(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Authentication failed", e);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Object[] result = (Object[]) entityManager.createNativeQuery(
                            "SELECT id, username, password, role FROM user_accounts WHERE username = ?")
                    .setParameter(1, username)
                    .getSingleResult();

            return User.builder()
                    .username((String) result[1])
                    .password((String) result[2])
                    .roles(((String) result[3]).replace("ROLE_", ""))
                    .build();
        } catch (NoResultException e) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }
}

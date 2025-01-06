package com.jwt.project.jwtauthentication.controller;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.jwt.project.jwtauthentication.Services.UserService;
import com.jwt.project.jwtauthentication.entity.User;
import com.jwt.project.jwtauthentication.model.JWTRequest;
import com.jwt.project.jwtauthentication.model.JWTResponse;
import com.jwt.project.jwtauthentication.security.JwtHelper;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserDetailsService userDetailsService;
    private final AuthenticationManager manager;
    private final UserService userService;
    private final JwtHelper helper;

    public AuthController(UserDetailsService userDetailsService, AuthenticationManager manager, UserService userService,
            JwtHelper helper) {
        this.userDetailsService = userDetailsService;
        this.manager = manager;
        this.userService = userService;
        this.helper = helper;
    }

    // private org.slf4j.Logger logger =
    // LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<JWTResponse> login(@RequestBody JWTRequest request) {

        // this will get email and password from header.
        this.doAuthenticate(request.getEmail(), request.getPassword());

        // If all ok and validated by manager class then we get user here
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        // Generating tocken
        String token = this.helper.generateToken(userDetails);

        JWTResponse response = JWTResponse.builder()
                .jwtToken(token)
                .username(userDetails.getUsername()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void doAuthenticate(String email, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            manager.authenticate(authentication);

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }

    }

    /*
     * @ExceptionHandler(BadCredentialsException.class)
     * public String exceptionHandler() {
     * return "Credentials Invalid !!";
     * }
     */

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> exceptionHandler() {
        return new ResponseEntity<>("Credentials Invalid !!", HttpStatus.UNAUTHORIZED);
    }

    // mapping for creating user
    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        // Check if the email already exists
        if (userService.existsByEmail(user.getEmail())) {
            return new ResponseEntity<>("Email already exists!", HttpStatus.BAD_REQUEST);
        }

        // Save the user
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

}

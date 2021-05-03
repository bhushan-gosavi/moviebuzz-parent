package com.moviebuzz.users.controller;


import com.moviebuzz.users.model.UserCreationRequest;
import com.moviebuzz.users.models.AuthenticationRequest;
import com.moviebuzz.users.models.AuthenticationResponse;
import com.moviebuzz.users.service.UserAuthService;
import com.moviebuzz.users.util.JwtUtil;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("Users_Auth_Controller")
@RequestMapping("/v1.0")
public class UserAuthController
{
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @RequestMapping(method = RequestMethod.POST, path = "/user")
    public ResponseEntity createUser(@RequestBody UserCreationRequest userCreationRequest)
    {
        if(Objects.isNull(userCreationRequest.getRoles()))
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Provide User Roles!");
        }
        log.info("Creating user with userName: {}", userCreationRequest.getUsername());
        try {
            userAuthService.createUser(userCreationRequest);
            log.info("User created with username: {}", userCreationRequest.getUsername());
            return ResponseEntity.ok().body(userCreationRequest.getUsername());
        } catch (Exception exception) {
            log.error("Unable to create User!", exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unable to create User: " + userCreationRequest.getUsername());
        }
    }

    @RequestMapping(value = "/user/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest,
        HttpServletRequest request) throws Exception {

        log.info("Got request from: " + request.getRemoteHost());
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        }
        catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid creds or user not found!");
        }
        catch (Exception exception)
        {
            log.error("Exception occurred while Authenticating", exception);
        }

        final UserDetails userDetails = userAuthService
            .loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @RequestMapping(path = "/user/validate")
    public ResponseEntity userAuthenticated()
    {
        try
        {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
            String username = userDetails.getUsername();

            return ResponseEntity.ok("User Validated: "+ username);
        }
        catch (Exception exception)
        {
            log.error("Unable to verify user!", exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unable to verify user!");
        }
    }

}

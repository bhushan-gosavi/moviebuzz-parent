package com.moviebuzz.users.service;

import com.moviebuzz.database.cassandra.models.UserEntity;
import com.moviebuzz.database.service.UserService;
import com.moviebuzz.users.model.UserCreationRequest;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAuthService implements UserDetailsService
{
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        UserEntity userEntity = userService.getUser(username);

        return User.builder()
            .username(userEntity.getUsername())
            .password(userEntity.getEncodedPassword())
            .accountExpired(Objects.nonNull(userEntity.getIsAccountNonExpired()) && !userEntity
                .getIsAccountNonExpired())
            .accountLocked(Objects.nonNull(userEntity.getIsAccountNonLocked()) && !userEntity
                .getIsAccountNonLocked())
            .credentialsExpired(Objects.nonNull(userEntity.getIsCredentialsNonExpired()) && !userEntity
                .getIsCredentialsNonExpired())
            .disabled(Objects.nonNull(userEntity.getIsEnabled()) && !userEntity
                .getIsEnabled())
            .authorities(userEntity.getRoles().stream()
                .map(role -> (GrantedAuthority) () -> role).collect(Collectors.toList()))
            .build();
    }

    public void createUser(UserCreationRequest userCreationRequest)
    {
        UserEntity userEntity = UserEntity.builder()
            .username(userCreationRequest.getUsername())
            .email(userCreationRequest.getEmail())
            .name(userCreationRequest.getName())
            .encodedPassword(passwordEncoder.encode(userCreationRequest.getPassword()))
            .roles(userCreationRequest.getRoles())
            .build();
        userService.addUser(userEntity);
    }
}

package com.moviebuzz.users.model;

import java.util.Set;
import lombok.Data;

@Data
public class UserCreationRequest
{
    private String username;
    private String email;
    private String name;
    private String password;
    private Set<String> roles;
}

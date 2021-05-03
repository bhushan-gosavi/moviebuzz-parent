package com.moviebuzz.users.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationResponse
{
    private final String jwt;
}

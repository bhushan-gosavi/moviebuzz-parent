package com.moviebuzz.database.models;

import java.util.UUID;
import lombok.Data;

@Data
public class UserEntity
{
    private UUID uuid;

    private String username;

    private String name;

    private String email;

}

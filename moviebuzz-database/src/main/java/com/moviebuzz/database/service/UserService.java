package com.moviebuzz.database.service;

import com.moviebuzz.database.cassandra.models.UserEntity;
import info.archinnov.achilles.generated.manager.UserEntity_Manager;
import java.security.InvalidParameterException;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService
{
    @Autowired
    private UserEntity_Manager userEntityManager;

    public UserEntity getUser(String username)
    {
        return userEntityManager.crud().findById(username).get();
    }

    public void addUser(UserEntity userEntity)
    {
        if(Objects.nonNull(getUser(userEntity.getUsername())))
        {
            throw new InvalidParameterException("Username Already Exists! Username: "
                + userEntity.getUsername());
        }

        userEntityManager.crud().insert(userEntity).execute();
    }
}

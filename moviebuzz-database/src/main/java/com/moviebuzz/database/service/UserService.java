package com.moviebuzz.database.service;

import com.moviebuzz.database.cassandra.models.UserEntity;
import info.archinnov.achilles.generated.manager.UserEntity_Manager;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService
{
    @Autowired
    private UserEntity_Manager userEntityManager;

    public UserEntity getUser(UUID uuid)
    {
        return userEntityManager.crud().findById(uuid).get();
    }

    public void addUser(UserEntity userEntity)
    {
        userEntity.setUserUUID();
        userEntityManager.crud().insert(userEntity).execute();
    }
}

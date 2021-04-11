package com.moviebuzz.api.controllers.v1_0;

import com.moviebuzz.database.cassandra.models.UserEntity;
import com.moviebuzz.database.service.UserService;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RefreshScope
@RestController("Users_Controller")
@RequestMapping("/v1.0")
public class UsersController
{
    @Autowired
    private UserService userService;


    @RequestMapping(method = RequestMethod.POST, path = "/users")
    public ResponseEntity addUser(@RequestBody UserEntity userEntity)
    {
        log.info("Adding user in db with userName: {}", userEntity.getUsername());
        try {
            userService.addUser(userEntity);
            log.info("Added user in db with username: {}", userEntity.getUsername());
            return ResponseEntity.ok().body(userEntity.getUsername());
        } catch (Exception exception) {
            log.error("Unable to add User!", exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to add User: " + userEntity.getUsername());
        }
    }

    @RequestMapping(value = "/users/{userName}", method = RequestMethod.GET)
    public ResponseEntity getUser(@PathVariable String userName) {
        log.info("Get user by id: {}", userName);
        try {
            UserEntity entity = userService.getUser(userName);
            return ResponseEntity.ok(entity);
        } catch (Exception exception) {
            log.error("Unable to fetch user from Cassandra UUID: {}", userName, exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to fetch user details: " + userName.toString());
        }
    }
}

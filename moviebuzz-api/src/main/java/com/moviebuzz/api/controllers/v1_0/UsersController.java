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
        log.info("Adding user in db with userUuid: {}", userEntity.getUuid());
        try {
            userService.addUser(userEntity);
            return ResponseEntity.ok().body(userEntity.getUuid());
        } catch (Exception exception) {
            log.error("Unable to add User!", exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to add User: " + userEntity.getUuid().toString());
        }
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    public ResponseEntity getMovie(@PathVariable UUID userId) {
        log.info("Get user by id: {}", userId);
        try {
            UserEntity entity = userService.getUser(userId);
            return ResponseEntity.ok(entity);
        } catch (Exception exception) {
            log.error("Unable to fetch user from Cassandra UUID: {}", userId, exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to fetch user details: " + userId.toString());
        }
    }
}

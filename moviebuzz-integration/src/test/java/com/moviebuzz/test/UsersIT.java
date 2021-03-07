package com.moviebuzz.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.restassured.http.ContentType;
import com.moviebuzz.database.cassandra.models.UserEntity;
import java.io.IOException;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;

public class UsersIT extends TestBaseIT
{

    public UsersIT() throws IOException
    {
    }

    @Test
    public void createUserIT() throws JsonProcessingException, InterruptedException
    {
        UUID uuid = UUID.randomUUID();
        String email = uuid.toString() + "@gmail.com";
        UserEntity userEntity = UserEntity.builder()
            .uuid(uuid)
            .name("dummy-user")
            .email(email)
            .build();

        api()
            .body(getJson(userEntity))
            .contentType(ContentType.JSON)
            .post("/moviebuzz/v1.0/users")
            .then().statusCode(200);

        UUID generatedUserId = UUID.nameUUIDFromBytes(email.getBytes());
        Thread.sleep(5_000);
        String response = api().get("/moviebuzz/v1.0/users/" + generatedUserId.toString()).asString();
        UserEntity dbUserEntity = objectMapper.readValue(response, UserEntity.class);
        Assert.assertNotNull(dbUserEntity);
        Assert.assertEquals(generatedUserId, dbUserEntity.getUuid());
    }


}

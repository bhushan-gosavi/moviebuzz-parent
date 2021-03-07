package com.moviebuzz.test;

import static com.jayway.restassured.RestAssured.given;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.specification.RequestSpecification;
import com.moviebuzz.database.cassandra.models.UserEntity;
import java.io.IOException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

@Slf4j
public class TestBaseIT
{
    Configuration configuration = Configuration.getInstance();
    public static ObjectMapper objectMapper = new ObjectMapper();

    public TestBaseIT() throws IOException
    {
        log.info("MovieBuzz API Server: " + configuration.getApiServer());
        log.info("MovieBuzz API Port: " + configuration.getApiPort());
    }

    protected RequestSpecification api() {
        RequestSpecification requestSpecification = given().baseUri(configuration.getApiServer());
        requestSpecification.port(Integer.parseInt(configuration.getApiPort()));
        return requestSpecification;
    }

    protected String getJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert object to json");
        }
        return null;
    }

    @Test
    public void testIT()
    {
        UUID uuid = UUID.randomUUID();
        UserEntity userEntity = UserEntity.builder()
            .uuid(uuid)
            .name("dummy-user")
            .email("dummy@gmail.com")
            .build();

        api()
            .body(getJson(userEntity))
            .contentType(ContentType.JSON)
            .post("/moviebuzz/v1.0/users")
            .then().statusCode(200);
        String name = "Bhush";
        Assert.assertEquals("Bhush", name);
    }
}

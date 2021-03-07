package com.moviebuzz.test;

import static com.jayway.restassured.RestAssured.given;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.specification.RequestSpecification;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

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

}

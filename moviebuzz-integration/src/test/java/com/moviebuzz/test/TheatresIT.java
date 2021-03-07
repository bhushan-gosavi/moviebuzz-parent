package com.moviebuzz.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.restassured.http.ContentType;
import com.moviebuzz.database.cassandra.models.TheatreEntity;
import java.io.IOException;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;

public class TheatresIT extends TestBaseIT
{

    public TheatresIT() throws IOException
    {
    }

    @Test
    public void addTheaterIT() throws JsonProcessingException, InterruptedException
    {
        UUID uuid = UUID.randomUUID();
        String name = "my-theatre";
        String city = "my-city";

        TheatreEntity theatreEntity = new TheatreEntity();
        theatreEntity.setUuid(uuid);
        theatreEntity.setCity(city);
        theatreEntity.setName(name);



        UUID generatedTheatreUuid = TheatreEntity.generateUUID(city, name);

        api()
            .body(getJson(theatreEntity))
            .contentType(ContentType.JSON)
            .post("/moviebuzz/v1.0/theatres")
            .then().statusCode(200);
        Thread.sleep(5_000);
        String response = api().get("/moviebuzz/v1.0/theatres/" + generatedTheatreUuid.toString()).asString();
        TheatreEntity dbTheatreEntity = objectMapper.readValue(response, TheatreEntity.class);
        Assert.assertNotNull(dbTheatreEntity);
        Assert.assertEquals(generatedTheatreUuid, dbTheatreEntity.getUuid());
        Assert.assertEquals(name, dbTheatreEntity.getName());

    }
}

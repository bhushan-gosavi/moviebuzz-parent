package com.moviebuzz.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.restassured.http.ContentType;
import com.moviebuzz.database.cassandra.models.MovieEntity;
import com.moviebuzz.database.cassandra.models.MovieReviewEntity;
import com.moviebuzz.kafka.model.UserReview;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

@Slf4j
public class MoviesIT extends TestBaseIT
{

    public MoviesIT() throws IOException
    {
    }

    @Test
    public void addMovieIT() throws JsonProcessingException, InterruptedException
    {
        String name = "New Movie";
        String description = "Best Movie";
        Date released = new Date();
        MovieEntity movieEntity = new MovieEntity();
        movieEntity.setName(name);
        movieEntity.setReleased(released);
        movieEntity.setDescription(description);



        UUID generatedMovieUuid = MovieEntity.generateUUID(name, description);

        api()
            .body(getJson(movieEntity))
            .contentType(ContentType.JSON)
            .post("/moviebuzz/v1.0/movies")
            .then().statusCode(200);
        Thread.sleep(5_000);
        String response = api().get("/moviebuzz/v1.0/movies/" + generatedMovieUuid.toString()).asString();
        MovieEntity dbMovieEntity = objectMapper.readValue(response, MovieEntity.class);
        Assert.assertNotNull(dbMovieEntity);
        Assert.assertEquals(generatedMovieUuid, dbMovieEntity.getUuid());
        Assert.assertEquals(name, dbMovieEntity.getName());

    }

    @Test
    public void publishReview() throws JsonProcessingException, InterruptedException
    {
        UUID movieId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String review = "Best Movie";


        UserReview userReview = new UserReview();
        userReview.setUserUuid(userId);
        userReview.setMovieUuid(movieId);
        userReview.setDate(new Date());
        userReview.setReview(review);
        userReview.setRating(10L);

        api()
            .body(getJson(userReview))
            .contentType(ContentType.JSON)
            .post("/moviebuzz/v1.0/reviews/publish")
            .then().statusCode(200);

        Thread.sleep(10_000);
        String rating = api().get("/moviebuzz/v1.0/movies/" + movieId.toString() + "/rating").asString();
        String reviews = api().get("/moviebuzz/v1.0/reviews/" + movieId.toString()).asString();
        Assert.assertEquals("10", rating);
        Assert.assertTrue(objectMapper.readValue(reviews, java.util.List.class).size()>0);
    }
}

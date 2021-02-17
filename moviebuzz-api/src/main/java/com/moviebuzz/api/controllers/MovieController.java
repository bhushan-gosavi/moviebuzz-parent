package com.moviebuzz.api.controllers;

import com.moviebuzz.database.models.MovieEntity;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MovieController
{

    MovieEntity movie1 =
        MovieEntity.builder()
            .uuid(UUID.randomUUID())
            .name("ddlj")
            .actors(Arrays.asList("Shahrukh", "Kajol"))
            .averageRating(3.5f)
            .releasedDate(new Date())
            .description("best movie")
            .build();

    MovieEntity movie2 =
        MovieEntity.builder()
            .uuid(UUID.randomUUID())
            .name("kkhh")
            .actors(Arrays.asList("Shahrukh", "kajol", "rani"))
            .averageRating(4.5f)
            .releasedDate(new Date())
            .description("good movie")
            .build();

    private List<MovieEntity> movies = Arrays.asList(movie1, movie2);

    @RequestMapping("/movies")
    public List<MovieEntity> getMovies()
    {
        return movies;
    }

}

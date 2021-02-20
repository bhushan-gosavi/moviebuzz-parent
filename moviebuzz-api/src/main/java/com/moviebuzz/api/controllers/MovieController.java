package com.moviebuzz.api.controllers;

import com.moviebuzz.database.cassandra.models.MovieEntity;
import info.archinnov.achilles.generated.manager.MovieEntity_Manager;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class MovieController
{

    @Autowired
    private MovieEntity_Manager movieEntityManager;


    @RequestMapping("/movies/{movieId}")
    public MovieEntity getMovie(@PathVariable UUID movieId)
    {
        return movieEntityManager.crud().findById(movieId).get();
    }


    @RequestMapping(method = RequestMethod.POST, path = "/movies")
    public ResponseEntity addMovie(@RequestBody MovieEntity movie)
    {
        movieEntityManager.crud().insert(movie).execute();
        return ResponseEntity.ok().body("Created: " + movie.getUuid());
    }

}

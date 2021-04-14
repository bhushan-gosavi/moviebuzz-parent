package com.moviebuzz.front.api.controllers.v1_0;

import com.moviebuzz.database.cassandra.models.MovieEntity;
import com.moviebuzz.front.api.models.v1_0.MovieModel;
import com.moviebuzz.front.api.service.APIService;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("Movies_Front_Controller")
@RequestMapping("/v1.0")
public class MovieFrontController
{
    @RequestMapping(path = "/hello")
    public String test()
    {
        return "Bhushan";
    }

    @Autowired
    private APIService apiService;

    @RequestMapping(value = "/movies/{movieId}", method = RequestMethod.GET)
    public ResponseEntity getMovie(@PathVariable UUID movieId) {
        log.info("Get movie by id: {}", movieId);
        try {
            MovieModel entity = apiService.getMovie(movieId);
            return ResponseEntity.ok(entity);
        } catch (Exception exception) {
            log.error("Unable to fetch movie from Cassandra UUID: {}", movieId, exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unable to fetch movie details: " + movieId.toString());
        }
    }
}

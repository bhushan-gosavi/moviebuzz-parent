package com.moviebuzz.api.controllers.v1_0;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.moviebuzz.api.requests.model.MovieCreateRequest;
import com.moviebuzz.database.cassandra.models.MovieEntity;
import com.moviebuzz.database.elasticsearch.models.EsMovieMapping;
import com.moviebuzz.database.service.MovieService;
import com.moviebuzz.database.service.RatingService;
import com.moviebuzz.kafka.constant.Constants;
import com.moviebuzz.kafka.model.BookingConfirmation;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.rest.RestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("Movies_Controller")
@RequestMapping("/v1.0")
public class MoviesController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private RatingService ratingService;

    @RequestMapping(value = "/movies/{movieId}", method = RequestMethod.GET)
    public ResponseEntity getMovie(@PathVariable UUID movieId) {
        log.info("Get movie by id: {}", movieId);
        try {
            MovieEntity entity = movieService.getMovie(movieId);
            return ResponseEntity.ok(entity);
        } catch (Exception exception) {
            log.error("Unable to fetch movie from Cassandra UUID: {}", movieId, exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to fetch movie details: " + movieId.toString());
        }
    }

    @RequestMapping(value = "/movies/{movieId}/rating", method = RequestMethod.GET)
    public ResponseEntity getMovieRating(@PathVariable UUID movieId) {
        log.info("Get movie by id: {}", movieId);
        try {
            Long rating = ratingService.getMovieRating(movieId);
            return ResponseEntity.ok(rating);
        } catch (Exception exception) {
            log.error("Unable to fetch movie rating from Cassandra movieUUID: {}", movieId, exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unable to fetch movie rating: " + movieId.toString());
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "/movies")
    public ResponseEntity addMovie(@RequestBody MovieCreateRequest movie)
            throws JsonProcessingException, ExecutionException, InterruptedException {
        log.info("Adding movie in db MovieName: {}", movie.getName());
        try {
            RestStatus indexStatus = movieService.addMovie(movie);
            if (!(indexStatus.equals(RestStatus.CREATED) || indexStatus.equals(RestStatus.OK))) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Unable to Index movie! Status: " + indexStatus.toString());
            }
            return ResponseEntity.ok().body(movie.getUuid());
        } catch (Exception exception) {
            log.error("Unable to add movie!", exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to add movie!");
        }
    }

    @RequestMapping(path = "/movies", method = RequestMethod.GET)
    public ResponseEntity getMovies(@RequestParam(required = false) Integer from,
                                    @RequestParam(required = false) Integer size,
                                    @RequestParam(required = true) Boolean isBookingActive,
                                    @RequestParam(required = true) String city) {
        try {
            List<EsMovieMapping> movies = null;

            if(isBookingActive)
            {
                movies = movieService.getAllActiveMoviesByReleasedDate(from, size, city);
            }
            else
            {
                movies = movieService.getAllMoviesByReleasedDate(from, size, city);
            }

            return ResponseEntity.ok(movies);
        } catch (Exception exception) {
            log.error("Unable to fetch movie from ElasticSearch", exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to fetch movies! ");
        }
    }

    @RequestMapping(path = "/movies/name/{movieName}", method = RequestMethod.GET)
    public ResponseEntity getMoviesByName(@PathVariable String movieName,
                                          @RequestParam(required = true) String city)
    {
        log.info("Get movie by name: {}", movieName);

        try {
            List<EsMovieMapping> movies = movieService.getAllMoviesByName(movieName, city);
            return ResponseEntity.ok(movies);
        } catch (Exception exception) {
            log.error("Unable to fetch movie from ElasticSearch by Name: {}",movieName, exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to fetch movies for name: " + movieName);
        }
    }

}

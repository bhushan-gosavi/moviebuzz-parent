package com.moviebuzz.api.controllers.v1_0;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.moviebuzz.database.cassandra.models.TheatreEntity;
import com.moviebuzz.database.elasticsearch.models.EsTheatreMapping;
import com.moviebuzz.database.service.TheatreService;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.rest.RestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RefreshScope
@RestController("Theatres_Controller")
@RequestMapping("/v1.0")
public class TheatresController
{
    @Autowired
    private TheatreService theatreService;

    @RequestMapping(value = "/theatres/{theatreId}", method = RequestMethod.GET)
    public ResponseEntity getMovie(@PathVariable UUID theatreId) {
        log.info("Get theatre by id: {}", theatreId);
        try {
            TheatreEntity entity = theatreService.getTheater(theatreId);
            return ResponseEntity.ok(entity);
        } catch (Exception exception) {
            log.error("Unable to fetch Theatre from Cassandra UUID: {}", theatreId, exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unable to fetch Theatre details: " + theatreId.toString());
        }
    }


    @RequestMapping(method = RequestMethod.POST, path = "/theatres")
    public ResponseEntity addTheater(@RequestBody TheatreEntity theatreEntity)
            throws JsonProcessingException, ExecutionException, InterruptedException {
        log.info("Adding theater in db TheaterName: {}", theatreEntity.getName());
        try {
            RestStatus indexStatus = theatreService.addTheater(theatreEntity);
            if (!(indexStatus.equals(RestStatus.CREATED) || indexStatus.equals(RestStatus.OK))) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Unable to Index movie! Status: " + indexStatus.toString());
            }
            return ResponseEntity.ok().body(theatreEntity.getUuid());
        } catch (Exception exception) {
            log.error("Unable to add theater!", exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to add theater!");
        }
    }

    @RequestMapping(path = "/theatres", method = RequestMethod.GET)
    public ResponseEntity getTheaters(@RequestParam(required = false) Integer from,
                                    @RequestParam(required = false) Integer size) {
        try {
            List<EsTheatreMapping> theaters = theatreService.getAllTheaters(from, size);
            return ResponseEntity.ok(theaters);
        } catch (Exception exception) {
            log.error("Unable to fetch movie from ElasticSearch", exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to fetch movies! ");
        }
    }

    @RequestMapping(path = "/theatres/city/{cityName}/movies/{movieId}", method = RequestMethod.GET)
    public ResponseEntity getMovies(@RequestParam(required = false) Integer from,
                                    @RequestParam(required = false) Integer size,
                                    @RequestParam(required = false) Double lat,
                                    @RequestParam(required = false) Double lon,
                                    @PathVariable String cityName,
                                    @PathVariable UUID movieId) {
        try {
            GeoPoint geoPoint =
                    (Objects.nonNull(lat) && Objects.nonNull(lon)) ? new GeoPoint(lat, lon) : null;
            List<EsTheatreMapping> theaters =
                    theatreService.getTheatersNearUserByCityAndMovie(cityName, movieId, geoPoint, from, size);
            return ResponseEntity.ok(theaters);
        } catch (Exception exception) {
            log.error("Unable to fetch Theaters from ElasticSearch", exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to fetch ! ");
        }
    }

}

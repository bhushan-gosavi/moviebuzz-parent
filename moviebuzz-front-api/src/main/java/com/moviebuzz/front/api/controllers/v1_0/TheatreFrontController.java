package com.moviebuzz.front.api.controllers.v1_0;


import com.moviebuzz.database.elasticsearch.models.EsTheatreMapping;
import com.moviebuzz.front.api.models.v1_0.TheaterModel;
import com.moviebuzz.front.api.service.APIService;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("Theatre_Front_Controller")
@RequestMapping("/v1.0")
public class TheatreFrontController
{
    @Autowired
    private APIService apiService;

    @RequestMapping(path = "/theatres/city/{cityName}/movies/{movieId}", method = RequestMethod.GET)
    public ResponseEntity getMovies(@RequestParam(required = false) Integer from,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) Double lat,
        @RequestParam(required = false) Double lon,
        @PathVariable String cityName,
        @PathVariable UUID movieId) {
        try {
            List<TheaterModel> theaters =
                apiService.getTheatersNearUserByCityAndMovie(cityName, movieId, lat, lon, from, size);
            return ResponseEntity.ok(theaters);
        } catch (Exception exception) {
            log.error("Unable to fetch Theaters from ElasticSearch", exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unable to fetch ! ");
        }
    }

}

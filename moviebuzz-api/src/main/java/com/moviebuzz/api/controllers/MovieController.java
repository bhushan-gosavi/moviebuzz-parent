package com.moviebuzz.api.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.moviebuzz.database.cassandra.models.MovieEntity;
import com.moviebuzz.database.elasticsearch.index.EsIndexMapping;
import com.moviebuzz.database.elasticsearch.index.EsIndexMapping.MovieBuzzIndex;
import com.moviebuzz.database.elasticsearch.service.ElasticsearchService;
import info.archinnov.achilles.generated.manager.MovieEntity_Manager;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.Client;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RefreshScope
@RestController
public class MovieController
{

    @Autowired
    private MovieEntity_Manager movieEntityManager;

    @Autowired
    private Client client;


    @Autowired
    private ElasticsearchService elasticsearchService;

    @RequestMapping("/movies/{movieId}")
    public MovieEntity getMovie(@PathVariable UUID movieId)
    {
        return movieEntityManager.crud().findById(movieId).get();
    }


    @RequestMapping("/movies")
    public ResponseEntity getMovies(@RequestParam(required = false) Integer from,
        @RequestParam(required = false) Integer size)
    {
        List<MovieEntity> movies =
            elasticsearchService.getAllDocs(EsIndexMapping.getIndex(MovieBuzzIndex.MOVIES), from,
                size, "name.keyword", SortOrder.ASC, MovieEntity.class);
        return ResponseEntity.ok().body(movies);
    }


    @RequestMapping(method = RequestMethod.POST, path = "/movies")
    public ResponseEntity addMovie(@RequestBody MovieEntity movie)
        throws JsonProcessingException, ExecutionException, InterruptedException
    {
        movieEntityManager.crud().insert(movie).execute();
        RestStatus indexStatus =
            elasticsearchService.indexDoc(movie.getUuid().toString(), movie,
                EsIndexMapping.getIndex(MovieBuzzIndex.MOVIES));
        log.info("Index status: " + indexStatus);
        return ResponseEntity.ok().body("Created: " + movie.getUuid());
    }

}

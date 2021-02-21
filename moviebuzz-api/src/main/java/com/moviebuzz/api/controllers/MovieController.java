package com.moviebuzz.api.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviebuzz.database.cassandra.models.MovieEntity;
import info.archinnov.achilles.generated.manager.MovieEntity_Manager;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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


    @RequestMapping("/movies/{movieId}")
    public MovieEntity getMovie(@PathVariable UUID movieId)
    {
        return movieEntityManager.crud().findById(movieId).get();
    }


    @RequestMapping("/movies")
    public ResponseEntity getMovies()
    {
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest).actionGet();
        List<MovieEntity> movies =
            Arrays.stream(searchResponse.getHits().getHits()).map(searchHit -> {
                try
                {
                    return new ObjectMapper().readValue(searchHit.getSourceAsString(),
                        MovieEntity.class);
                } catch (JsonProcessingException e)
                {
                    e.printStackTrace();
                    return null;
                }
            }).collect(Collectors.toList());
        return ResponseEntity.ok().body(movies);
    }


    @RequestMapping(method = RequestMethod.POST, path = "/movies")
    public ResponseEntity addMovie(@RequestBody MovieEntity movie)
        throws JsonProcessingException, ExecutionException, InterruptedException
    {
        movieEntityManager.crud().insert(movie).execute();
        IndexRequest indexRequest = new IndexRequest("moviebuzz_movies", "movies", movie.getUuid().toString());
        indexRequest.source(new ObjectMapper().writeValueAsString(movie), XContentType.JSON);
        IndexResponse response = client.index(indexRequest).get();
        log.info("Index status: " + response.status());
        return ResponseEntity.ok().body("Created: " + movie.getUuid());
    }

}

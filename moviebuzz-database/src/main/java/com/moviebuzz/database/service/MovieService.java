package com.moviebuzz.database.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.moviebuzz.database.cassandra.models.MovieEntity;
import com.moviebuzz.database.elasticsearch.index.EsIndexMapping;
import com.moviebuzz.database.elasticsearch.index.EsIndexMapping.MovieBuzzIndex;
import com.moviebuzz.database.elasticsearch.models.EsMovieMapping;
import com.moviebuzz.database.elasticsearch.service.ElasticsearchService;
import info.archinnov.achilles.generated.manager.MovieEntity_Manager;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieService
{
    @Autowired
    private MovieEntity_Manager movieEntityManager;

    @Autowired
    private ElasticsearchService elasticsearchService;


    public MovieEntity getMovie(UUID movieId)
    {
        return movieEntityManager.crud().findById(movieId).get();
    }

    public RestStatus addMovie(MovieEntity movieEntity)
            throws InterruptedException, ExecutionException, JsonProcessingException
    {
        movieEntity.setMovieUUID();
        movieEntityManager.crud().insert(movieEntity).execute();
        EsMovieMapping movieMapping = new EsMovieMapping();
        BeanUtils.copyProperties(movieEntity, movieMapping);
        return elasticsearchService.indexDoc(movieEntity.getUuid().toString(), movieEntity,
                EsIndexMapping.getIndex(MovieBuzzIndex.MOVIES));
    }

    public List<EsMovieMapping> getAllMoviesByReleasedDate(Integer from, Integer size)
    {
        return elasticsearchService.getAllDocs(EsIndexMapping.getIndex(MovieBuzzIndex.MOVIES),
                null, null, null,
                from, size, "released", SortOrder.DESC, EsMovieMapping.class);
    }


    public List<EsMovieMapping> getAllActiveMoviesByReleasedDate(Integer from, Integer size)
    {
        return elasticsearchService.getAllDocs(EsIndexMapping.getIndex(MovieBuzzIndex.MOVIES),
                Collections.singletonMap("isBookingActive", true), null, null,
                from, size, "released", SortOrder.DESC, EsMovieMapping.class);
    }

    public List<EsMovieMapping> getAllMoviesByName(String name)
    {
        return elasticsearchService.getAllDocs(EsIndexMapping.getIndex(MovieBuzzIndex.MOVIES),
                Collections.singletonMap("name", name), null, null,
                null, null, "released", SortOrder.DESC, EsMovieMapping.class);
    }


}

package com.moviebuzz.database.service;

import com.google.common.cache.LoadingCache;
import com.moviebuzz.database.cassandra.models.MovieRatingEntity;
import info.archinnov.achilles.generated.manager.MovieRatingEntity_Manager;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingService
{
    @Autowired
    private MovieRatingEntity_Manager ratingManager;

    @Autowired
    private LoadingCache<UUID, Long> movieRatingsCache;

    public void incrementMovieRating(UUID movieId, Long incrementTotalRatingsBy, Long incrementRatingCounterBy)
    {
        ratingManager.dsl().update().fromBaseTable()
            .totalRatings().Incr(incrementTotalRatingsBy)
            .ratingsCounter().Incr(incrementRatingCounterBy)
            .where().movieUuid().Eq(movieId)
            .execute();

    }

    public MovieRatingEntity getMovieRatingEntity(UUID movieId)
    {
        return ratingManager.crud().findById(movieId).get();
    }

    public Long getMovieRating(UUID movieId) throws ExecutionException
    {
        return movieRatingsCache.get(movieId);
    }

}

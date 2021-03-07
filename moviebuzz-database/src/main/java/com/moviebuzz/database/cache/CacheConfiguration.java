package com.moviebuzz.database.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.moviebuzz.database.cassandra.models.MovieRatingEntity;
import com.moviebuzz.database.service.RatingService;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

@Configuration
public class CacheConfiguration
{
    @Autowired
    private RatingService ratingService;


    @Value("${cache.movieRatings.size}")
    private long cacheMaxSize;

    @Value("${cache.movieRatings.expireAfterHours}")
    private long cacheExpireHours;


    @Bean
    @Lazy
    @Scope("singleton")
    public LoadingCache<UUID, Long> movieRatingsCache()
    {
        return CacheBuilder.newBuilder()
            .maximumSize(cacheMaxSize)
            .expireAfterWrite(cacheExpireHours, TimeUnit.HOURS)
            .build(new CacheLoader<UUID, Long>()
            {
                @Override
                public Long load(UUID uuid) throws Exception
                {
                    MovieRatingEntity ratingEntity = ratingService.getMovieRatingEntity(uuid);
                    if(Objects.isNull(ratingEntity))
                    {
                        return null;
                    }
                    return ratingEntity.getTotalRatings()/ratingEntity.getRatingsCounter();
                }
            });
    }

}

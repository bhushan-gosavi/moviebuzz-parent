package com.moviebuzz.database.service;

import com.moviebuzz.database.cassandra.models.MovieReviewEntity;
import com.moviebuzz.database.cassandra.models.UserBookingEntity;
import info.archinnov.achilles.generated.manager.MovieReviewEntity_Manager;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService
{

    @Autowired
    private MovieReviewEntity_Manager movieReviewsManager;

    public void addReview(MovieReviewEntity movieReview)
    {
        movieReviewsManager.crud().insert(movieReview);
    }

    public List<MovieReviewEntity> getMovieReviews(UUID movieId)
    {
        return movieReviewsManager.dsl().select().allColumns_FromBaseTable()
            .where().movieUuid().Eq(movieId)
            .orderByDateDescending()
            .getList();
    }

}

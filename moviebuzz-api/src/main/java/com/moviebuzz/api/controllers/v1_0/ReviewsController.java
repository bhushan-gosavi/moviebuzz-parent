package com.moviebuzz.api.controllers.v1_0;


import com.moviebuzz.database.cassandra.models.MovieReviewEntity;
import com.moviebuzz.database.service.ReviewService;
import com.moviebuzz.kafka.constant.Constants;
import com.moviebuzz.kafka.model.UserReview;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("Reviews_Controller")
@RequestMapping("/v1.0")
public class ReviewsController
{
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private ReviewService reviewService;

    @RequestMapping(value = "/reviews/publish", method = RequestMethod.POST)
    public ResponseEntity publishReview(@RequestBody UserReview review)
    {
        try
        {
            log.info("Publishing Review by user: {} for movie: {}",
                review.getUsername(), review.getMovieUuid());

            ListenableFuture<SendResult<String, Object>> future = kafkaTemplate
                    .send(Constants.USER_REVIEWS, UUID.randomUUID().toString(), review);

            return ResponseEntity.ok().body(future.get());
        }
        catch (Exception exception)
        {
            log.error("Unable to Publish Booking Confirmation!", exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unable to Publish Booking Confirmation!");
        }
    }

    @RequestMapping(value = "/reviews/{movieId}", method = RequestMethod.GET)
    public ResponseEntity getMovieReviews(@PathVariable UUID movieId) {
        log.info("Get Reviews by movieId: {}", movieId);
        try {

            List<MovieReviewEntity> reviews = reviewService.getMovieReviews(movieId);
            return ResponseEntity.ok(reviews);

        } catch (Exception exception) {
            log.error("Unable to fetch Reviews from Cassandra for movieId: {}", movieId, exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unable to fetch Reviews from Cassandra for movieId: " + movieId);
        }
    }
}

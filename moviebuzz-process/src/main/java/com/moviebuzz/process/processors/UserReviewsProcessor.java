package com.moviebuzz.process.processors;

import com.moviebuzz.database.cassandra.models.MovieReviewEntity;
import com.moviebuzz.database.service.RatingService;
import com.moviebuzz.database.service.ReviewService;
import com.moviebuzz.kafka.constant.Constants;
import com.moviebuzz.kafka.model.UserReview;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserReviewsProcessor
{
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private RatingService ratingService;

    @KafkaListener(topics = Constants.USER_REVIEWS, clientIdPrefix = "user-reviews-processor",
        containerFactory = "kafkaListenerUserReviewFactory")
    public void process(ConsumerRecord<String, UserReview> consumerRecord,
        @Payload UserReview userReview)
    {
        log.info("Processing Kafka Records! Topic:{} Partition:{} Offset:{}",
            consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset());
        try
        {
            MovieReviewEntity reviewEntity = MovieReviewEntity.builder()
                .movieUuid(userReview.getMovieUuid())
                .userUuid(userReview.getUserUuid())
                .review(userReview.getReview())
                .date(userReview.getDate())
                .rating(userReview.getRating())
                .build();

            reviewService.addReview(reviewEntity);

            ratingService.incrementMovieRating(userReview.getMovieUuid(),userReview.getRating(), 1L);

        }
        catch (Exception exception)
        {
            log.error("Exception occured while processing User Review Kafka Record! "
                    + "Topic:{} Partition:{} Offset:{}",
                consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset(), exception);
        }

    }
}

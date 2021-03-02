package com.moviebuzz.kafka.config;

import com.moviebuzz.kafka.constant.Constants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicsConfig
{
    @Value("${kafka.topics.bookingConfirmation.partitions}")
    private Integer bookingConfirmationPartitions;

    @Value("${kafka.topics.bookingConfirmation.replicas}")
    private Short bookingConfirmationReplicas;


    @Value("${kafka.topics.userReviews.partitions}")
    private Integer userReviewsPartitions;

    @Value("${kafka.topics.userReviews.replicas}")
    private Short userReviewsReplicas;


    @Bean
    public NewTopic bookingConfirmationTopic() {
        return new NewTopic(Constants.BOOKING_CONFIRMATION, bookingConfirmationPartitions, bookingConfirmationReplicas);
    }

    @Bean
    public NewTopic userReviewsTopic() {
        return new NewTopic(Constants.USER_REVIEWS, userReviewsPartitions, userReviewsReplicas);
    }

}

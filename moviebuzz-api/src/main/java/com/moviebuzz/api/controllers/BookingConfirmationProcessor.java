package com.moviebuzz.api.controllers;

import com.moviebuzz.kafka.constant.Constants;
import com.moviebuzz.kafka.model.BookingConfirmation;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableKafka
public class BookingConfirmationProcessor {

    @KafkaListener(topics = Constants.BOOKING_CONFIRMATION, clientIdPrefix = "json",
            containerFactory = "kafkaListenerBookingConfirmationFactory")
    public void listenAsObject(ConsumerRecord<String, BookingConfirmation> cr,
                               @Payload BookingConfirmation payload)
    {
        log.info("Logger [JSON] received key {} | Payload: {}", cr.key(), payload.toString());
    }
}

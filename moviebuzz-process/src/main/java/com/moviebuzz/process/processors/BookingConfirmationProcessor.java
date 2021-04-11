package com.moviebuzz.process.processors;

import com.moviebuzz.database.cassandra.models.UserBookingEntity;
import com.moviebuzz.database.service.BookingService;
import com.moviebuzz.kafka.constant.Constants;
import com.moviebuzz.kafka.model.BookingConfirmation;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BookingConfirmationProcessor
{

    @Autowired
    private BookingService bookingService;

    @KafkaListener(topics = Constants.BOOKING_CONFIRMATION, clientIdPrefix = "booking-confirmation-processor",
        containerFactory = "kafkaListenerBookingConfirmationFactory")
    public void process(ConsumerRecord<String, BookingConfirmation> consumerRecord,
        @Payload BookingConfirmation bookingConfirmation)
    {
        log.info("Processing Kafka Record! Topic:{} Partition:{} Offset:{}",
            consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset());
        try
        {
            UserBookingEntity userBookingEntity = new UserBookingEntity();
            BeanUtils.copyProperties(bookingConfirmation, userBookingEntity);
            bookingService.addBooking(userBookingEntity);

            log.info("Booking details stored in Cassandra! userId:{} bookingId:{}",
                bookingConfirmation.getMovieName(), bookingConfirmation.getBookingId());

        }
        catch (Exception exception)
        {
            log.error("Exception occured while processing Booking Confirmation Kafka Record! "
                    + "Topic:{} Partition:{} Offset:{}",
                consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset(), exception);
        }
    }

}

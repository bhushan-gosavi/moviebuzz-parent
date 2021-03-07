package com.moviebuzz.api.controllers.v1_0;

import com.moviebuzz.database.cassandra.models.UserBookingEntity;
import com.moviebuzz.database.service.BookingService;
import com.moviebuzz.kafka.constant.Constants;
import com.moviebuzz.kafka.model.BookingConfirmation;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
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
@RefreshScope
@RestController("Bookings_Controller")
@RequestMapping("/v1.0")
public class BookingsController
{

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private BookingService bookingService;

    @RequestMapping(value = "/bookings/confirmed", method = RequestMethod.POST)
    public ResponseEntity markBookingConfirmed(@RequestBody BookingConfirmation bookingConfirmation)
    {
        try
        {
            log.info("Marking Booking as Confirmed! BookingId:{} userId:{}",
                bookingConfirmation.getBookingId(), bookingConfirmation.getUserId());

            ListenableFuture<SendResult<String, Object>> future = kafkaTemplate
                .send(Constants.BOOKING_CONFIRMATION, UUID.randomUUID().toString(), bookingConfirmation);

            return ResponseEntity.ok().body(future.get());
        }
        catch (Exception exception)
        {
            log.error("Unable to Publish Booking Confirmation!", exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unable to Publish Booking Confirmation!");
        }
    }

    @RequestMapping(value = "/bookings/{userId}", method = RequestMethod.GET)
    public ResponseEntity getUserBookings(@PathVariable UUID userId) {
        log.info("Get Bookings by userId: {}", userId);
        try {

            List<UserBookingEntity> userBookings = bookingService.getUserBookings(userId);
            return ResponseEntity.ok(userBookings);

        } catch (Exception exception) {
            log.error("Unable to fetch user Bookings from Cassandra for userId: {}", userId, exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unable to fetch user Bookings from Cassandra for userId: " + userId);
        }
    }

}

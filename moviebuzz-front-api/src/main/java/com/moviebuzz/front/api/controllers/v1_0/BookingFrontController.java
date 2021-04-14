package com.moviebuzz.front.api.controllers.v1_0;


import com.moviebuzz.database.cassandra.models.UserBookingEntity;
import com.moviebuzz.front.api.models.v1_0.BookingModel;
import com.moviebuzz.front.api.service.APIService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("Booking_Front_Controller")
@RequestMapping("/v1.0")
public class BookingFrontController
{
    @Autowired
    private APIService apiService;

    @RequestMapping(value = "/bookings", method = RequestMethod.GET)
    public ResponseEntity getUserBookings() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();
        String userName = userDetails.getUsername();

        log.info("Get Bookings by userId: {}", userName);
        try {

            List<BookingModel> userBookings = apiService.getBookings(userName);
            return ResponseEntity.ok(userBookings);

        } catch (Exception exception) {
            log.error("Unable to fetch user Bookings from Cassandra for userName: {}", userName, exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unable to fetch user Bookings from Cassandra for userName: " + userName);
        }
    }
}

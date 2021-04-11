package com.moviebuzz.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.restassured.http.ContentType;
import com.moviebuzz.kafka.model.BookingConfirmation;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;

public class BookingIT extends TestBaseIT
{

    public BookingIT() throws IOException
    {
    }

    @Test
    public void bookingConfirmationTest() throws JsonProcessingException, InterruptedException
    {
        UUID bookingId = UUID.randomUUID();
        UUID movieId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        BookingConfirmation confirmation = new BookingConfirmation();
        confirmation.setBookingId(bookingId);
        confirmation.setUsername("test-user-" + userId.toString());
        confirmation.setMovieId(movieId);
        confirmation.setMovieName("my-movie");
        confirmation.setShowTime(new Date());


        api()
            .body(getJson(confirmation))
            .contentType(ContentType.JSON)
            .post("/moviebuzz/v1.0/bookings/confirmed")
            .then().statusCode(200);
        Thread.sleep(10_000);
        String userBookings = api().get("/moviebuzz/v1.0/bookings/" + "test-user-" + userId.toString()).asString();
        Assert.assertTrue(objectMapper.readValue(userBookings, java.util.List.class).size()>0);
    }
}

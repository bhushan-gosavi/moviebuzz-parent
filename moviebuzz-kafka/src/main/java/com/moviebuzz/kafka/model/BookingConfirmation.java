package com.moviebuzz.kafka.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingConfirmation
{
    private UUID bookingId;

    private UUID userId;
    private String userName;

    private String userEmail;
    private Long userMobileNumber;

    private UUID movieId;
    private String movieName;

    private UUID theaterId;
    private String theaterName;

    private String screen;
    private Set<String> seats;
    private Date showTime;


}

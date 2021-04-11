package com.moviebuzz.kafka.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingConfirmation
{
    private UUID bookingId;

    private String username;

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

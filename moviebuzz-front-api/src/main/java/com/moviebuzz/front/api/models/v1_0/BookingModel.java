package com.moviebuzz.front.api.models.v1_0;

import java.util.Date;
import java.util.Set;
import java.util.UUID;
import lombok.Data;

@Data
public class BookingModel
{
    private String username;
    private UUID bookingId;
    private UUID movieId;
    private String movieName;
    private UUID theaterId;
    private String theaterName;
    private String screen;
    private Set<String> seats;
    private Date showTime;
}

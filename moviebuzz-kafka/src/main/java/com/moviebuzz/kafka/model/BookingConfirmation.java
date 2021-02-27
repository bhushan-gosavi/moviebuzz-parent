package com.moviebuzz.kafka.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.UUID;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingConfirmation
{
    private UUID customerId;
    private String customerName;

    private UUID movieId;
    private String movieName;

}

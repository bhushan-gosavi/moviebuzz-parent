package com.moviebuzz.database.elasticsearch.models;

import java.util.Date;
import java.util.UUID;
import lombok.Data;

@Data
public class EsMovieMapping
{
    private UUID uuid;
    private String name;
    private Boolean isBookingActive;
    private Date released;
    private String imageUrl;
}

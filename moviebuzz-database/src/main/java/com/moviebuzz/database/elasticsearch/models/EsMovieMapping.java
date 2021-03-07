package com.moviebuzz.database.elasticsearch.models;

import java.util.Date;
import java.util.Set;
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
    private Set<String> runningCities;
}

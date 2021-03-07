package com.moviebuzz.database.elasticsearch.models;

import com.moviebuzz.database.cassandra.models.udt.Location;
import com.moviebuzz.database.cassandra.models.udt.Movie;
import java.util.Set;
import java.util.UUID;
import lombok.Data;

@Data
public class EsTheatreMapping
{
    private UUID uuid;
    private String name;
    private String city;
    private Location location;
    private Set<Movie> activeMovies;
}

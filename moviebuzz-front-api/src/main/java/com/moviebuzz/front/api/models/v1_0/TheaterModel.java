package com.moviebuzz.front.api.models.v1_0;

import com.moviebuzz.database.cassandra.models.udt.Location;
import com.moviebuzz.database.cassandra.models.udt.Movie;
import java.util.Set;
import java.util.UUID;
import lombok.Data;

@Data
public class TheaterModel
{
    private UUID uuid;
    private String name;
    private String city;
    private Location location;
    private Set<Movie> activeMovies;
}

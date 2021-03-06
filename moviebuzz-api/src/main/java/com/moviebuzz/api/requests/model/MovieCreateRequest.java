package com.moviebuzz.api.requests.model;

import com.moviebuzz.database.cassandra.models.MovieEntity;
import java.util.Set;
import lombok.Data;

@Data
public class MovieCreateRequest extends MovieEntity
{
    private Set<String> runningCities;
}

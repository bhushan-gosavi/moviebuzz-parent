package com.moviebuzz.api.requests.model;

import com.moviebuzz.database.cassandra.models.MovieEntity;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MovieCreateRequest extends MovieEntity
{
    private Set<String> runningCities;
}

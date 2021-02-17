package com.moviebuzz.database.models;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovieEntity
{
    private UUID uuid;

    private String name;

    private List<String> actors;

    private Date releasedDate;

    private String description;

    private Float averageRating;

}

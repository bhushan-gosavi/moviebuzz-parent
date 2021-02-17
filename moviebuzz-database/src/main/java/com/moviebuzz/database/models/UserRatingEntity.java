package com.moviebuzz.database.models;

import java.util.UUID;
import lombok.Data;

@Data
public class UserRatingEntity
{
    private UUID userUuid;

    private UUID movieUuid;

    private Float rating;

}

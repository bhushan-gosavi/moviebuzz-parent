package com.moviebuzz.database.cassandra.models.udt;

import info.archinnov.achilles.annotations.Column;
import info.archinnov.achilles.annotations.UDT;
import java.util.UUID;
import lombok.Data;

@Data
@UDT(keyspace = "moviebuzz", name = "movie_udt")
public class Movie
{
    @Column
    UUID movieId;

    @Column
    String name;
}

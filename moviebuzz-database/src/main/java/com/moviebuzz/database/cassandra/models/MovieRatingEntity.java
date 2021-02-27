package com.moviebuzz.database.cassandra.models;

import info.archinnov.achilles.annotations.Column;
import info.archinnov.achilles.annotations.Counter;
import info.archinnov.achilles.annotations.PartitionKey;
import info.archinnov.achilles.annotations.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  All the average movie ratings will be stored in cassandra moviebuzz.movie_ratings table
 *  With PartitionKey as Movie UUID
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(keyspace = "moviebuzz", table = "movie_ratings")
public class MovieRatingEntity
{
    @PartitionKey
    @Column
    private UUID movieUuid;

    @Counter
    private Double totalRatings;

    @Counter
    private Long ratingsCounter;

}

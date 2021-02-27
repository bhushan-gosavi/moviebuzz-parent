package com.moviebuzz.database.cassandra.models;

import info.archinnov.achilles.annotations.Column;
import info.archinnov.achilles.annotations.PartitionKey;
import info.archinnov.achilles.annotations.Table;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  All the movies details will be stored in cassandra moviebuzz.movies table
 *  With PartitionKey as Movie UUID
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(keyspace = "moviebuzz", table = "movies")
public class MovieEntity
{
    @PartitionKey(1)
    @Column
    private UUID uuid;

    @Column
    private String name;

    @Column
    private Set<String> actors;

    @Column
    private Date releasedDate;

    @Column
    private String description;

    @Column
    private Float averageRating;

    @Column
    private Integer likes;


}

package com.moviebuzz.database.cassandra.models;

import com.moviebuzz.database.cassandra.models.udt.Location;
import com.moviebuzz.database.cassandra.models.udt.Movie;
import info.archinnov.achilles.annotations.Column;
import info.archinnov.achilles.annotations.Frozen;
import info.archinnov.achilles.annotations.PartitionKey;
import info.archinnov.achilles.annotations.Table;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  All the Theater details will be stored in cassandra moviebuzz.theaters table
 *  With PartitionKey as Movie UUID
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(keyspace = "moviebuzz", table = "theaters")
public class TheatreEntity
{
    @PartitionKey
    @Column
    private UUID uuid;

    @Column
    private String name;

    @Column
    private String city;

    @Column
    private String address;

    @Column
    private @Frozen Location location;

    @Column
    private Set<@Frozen Movie> movies;

}

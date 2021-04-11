package com.moviebuzz.database.cassandra.models;

import info.archinnov.achilles.annotations.ClusteringColumn;
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
 * All user tickets will be stored in moviebuzz.user_bookings table
 *  With PartitionKey as userId and bookingId as clusteringColumn
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(keyspace = "moviebuzz", table = "user_bookings")
public class UserBookingEntity
{
    @PartitionKey
    private String username;

    @ClusteringColumn
    private UUID bookingId;

    @Column
    private UUID movieId;

    @Column
    private String movieName;

    @Column
    private UUID theaterId;

    @Column
    private String theaterName;

    @Column
    private String screen;

    @Column
    private Set<String> seats;

    @Column
    private Date showTime;

}

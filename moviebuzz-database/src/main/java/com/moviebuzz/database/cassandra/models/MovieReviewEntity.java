package com.moviebuzz.database.cassandra.models;

import info.archinnov.achilles.annotations.ClusteringColumn;
import info.archinnov.achilles.annotations.Column;
import info.archinnov.achilles.annotations.PartitionKey;
import info.archinnov.achilles.annotations.Table;
import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  All the user-ratings will be stored in cassandra moviebuzz.user_ratings table
 *  With PartitionKey as User UUID and movieUuid as clustering column
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(keyspace = "moviebuzz", table = "movie_reviews")
public class MovieReviewEntity
{
    @PartitionKey(1)
    private UUID movieUuid;

    @ClusteringColumn(1)
    private Date date;

    @ClusteringColumn(2)
    private UUID userUuid;

    @Column
    private String review;

    @Column
    private Long rating;

}
